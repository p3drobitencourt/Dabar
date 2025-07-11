package br.edu.ifsuldeminas.mch.dabar;

import android.Manifest;
import android.content.Intent;
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
    private String titulo, descricao;
    private int idCategoria;
    private MediaRecorder mediaRecorder;
    private String caminhoDoArquivoDeAudio = null;
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
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO}, REQUEST_RECORD_AUDIO_PERMISSION);
        } else {
            iniciarGravacao();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_RECORD_AUDIO_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                iniciarGravacao();
            } else {
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
            // Lógica CORRIGIDA: Só define como 'true' se a gravação realmente começar
            isRecording = true;
            chronometer.setBase(SystemClock.elapsedRealtime());
            chronometer.start();
            buttonGravar.setBackgroundResource(R.drawable.circle_background_recording);
            Toast.makeText(this, "Gravação iniciada!", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
            isRecording = false; // Garante que não fique em estado inconsistente
            Toast.makeText(this, "Falha ao iniciar gravação.", Toast.LENGTH_SHORT).show();
        }
    }

    private void pararGravacao() {
        if (mediaRecorder != null && isRecording) { // Checagem dupla para segurança
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

        Intent intent = new Intent(GravarActivity.this, ListResumosActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
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