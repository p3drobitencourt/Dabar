package br.edu.ifsuldeminas.mch.dabar;

import android.Manifest;
import android.content.pm.PackageManager;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.SystemClock;
import android.widget.Chronometer;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.IOException;

public class GravarActivity extends AppCompatActivity {

    private boolean isRecording = false;
    private FrameLayout buttonGravar;
    private Chronometer chronometer;

    // --- Dados recebidos da tela anterior ---
    private String titulo, descricao;
    private int idCategoria;

    // --- Lógica de Gravação ---
    private MediaRecorder mediaRecorder;
    private String caminhoDoArquivoDeAudio = null;

    // --- Constante para o pedido de permissão ---
    private static final int REQUEST_RECORD_AUDIO_PERMISSION = 200;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gravar);

        titulo = getIntent().getStringExtra("EXTRA_TITULO");
        descricao = getIntent().getStringExtra("EXTRA_DESCRICAO");
        idCategoria = getIntent().getIntExtra("EXTRA_CATEGORIA", -1);

        chronometer = findViewById(R.id.text_timer);
        buttonGravar = findViewById(R.id.buttonGravar);

        buttonGravar.setOnClickListener(v -> {
            if (!isRecording) {
                verificarPermissaoEGravar();
            } else {
                pararGravacao();
            }
        });
    }

    private void verificarPermissaoEGravar() {
        // Verifica se a permissão para gravar áudio já foi concedida
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            // Se não foi, solicita a permissão ao usuário
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO}, REQUEST_RECORD_AUDIO_PERMISSION);
        } else {
            // Se a permissão já existe, inicia a gravação
            iniciarGravacao();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_RECORD_AUDIO_PERMISSION) {
            // Verifica se o usuário concedeu a permissão
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permissão concedida, pode iniciar a gravação
                iniciarGravacao();
            } else {
                // Permissão negada pelo usuário
                Toast.makeText(this, "Permissão de áudio negada. Não é possível gravar.", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void iniciarGravacao() {
        caminhoDoArquivoDeAudio = getExternalFilesDir(null).getAbsolutePath() + "/audio_" + System.currentTimeMillis() + ".mp3";
        mediaRecorder = new MediaRecorder();
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
        mediaRecorder.setOutputFile(caminhoDoArquivoDeAudio);

        try {
            mediaRecorder.prepare();
            mediaRecorder.start();
            isRecording = true;

            chronometer.setBase(SystemClock.elapsedRealtime());
            chronometer.start();

            buttonGravar.setBackgroundResource(R.drawable.circle_background_recording);
            Toast.makeText(this, "Gravação iniciada!", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Falha ao iniciar gravação.", Toast.LENGTH_SHORT).show();
            isRecording = false;
        }
    }

    private void pararGravacao() {
        if (mediaRecorder != null) {
            try {
                mediaRecorder.stop();
                mediaRecorder.release();
            } catch (RuntimeException e) {
                e.printStackTrace();
            } finally {
                mediaRecorder = null;
            }

            isRecording = false;
            chronometer.stop();
            buttonGravar.setEnabled(false);
            buttonGravar.setBackgroundResource(R.drawable.circle_background);

            salvarResumoDefinitivamente();
        }
    }

    private void salvarResumoDefinitivamente() {
        if (titulo == null || idCategoria == -1) {
            Toast.makeText(this, "Erro: dados do resumo incompletos.", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        ResumoDAO dao = new ResumoDAO(this);
        Resumo novoResumo = new Resumo();
        Categoria categoria = new Categoria();
        categoria.setId(idCategoria);

        novoResumo.setTitulo(titulo);
        novoResumo.setDescricao(descricao);
        novoResumo.setCategoria(categoria);
        novoResumo.setCaminhoAudio(caminhoDoArquivoDeAudio);

        dao.adicionarResumo(novoResumo);

        Toast.makeText(this, "Resumo salvo com sucesso!", Toast.LENGTH_LONG).show();
        finish();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (isRecording) {
            pararGravacao();
        }
    }
}