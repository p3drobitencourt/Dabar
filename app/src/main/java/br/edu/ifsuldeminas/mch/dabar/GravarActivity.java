package br.edu.ifsuldeminas.mch.dabar;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.MenuItem;
import android.widget.Chronometer;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.IOException;

import br.edu.ifsuldeminas.mch.dabar.ResumoDAO;
import br.edu.ifsuldeminas.mch.dabar.AppDatabase;

public class GravarActivity extends AppCompatActivity {

    private boolean isRecording = false;
    private FrameLayout buttonGravar;
    private Chronometer chronometer;
    private String titulo, descricao;
    private int idCategoria;
    private MediaRecorder mediaRecorder;
    private String caminhoDoArquivoDeAudio = null;
    private static final int REQUEST_RECORD_AUDIO_PERMISSION = 200;
    private ResumoDAO resumoDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gravar);

        // --- LÓGICA DA TOOLBAR COM BOTÃO VOLTAR ---
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
        // --- FIM DA LÓGICA DA TOOLBAR ---

        titulo = getIntent().getStringExtra("EXTRA_TITULO");
        descricao = getIntent().getStringExtra("EXTRA_DESCRICAO");
        idCategoria = getIntent().getIntExtra("EXTRA_CATEGORIA_ID", -1);

        chronometer = findViewById(R.id.text_timer);
        buttonGravar = findViewById(R.id.buttonGravar);

        resumoDao = AppDatabase.getDatabase(this).resumoDao();

        buttonGravar.setOnClickListener(v -> {
            if (!isRecording) {
                verificarPermissaoEGravar();
            } else {
                pararGravacao();
            }
        });
    }

    // --- MÉTODO PARA LIDAR COM O CLIQUE NA SETA DA TOOLBAR ---
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // ... (resto da sua classe com a lógica de gravação permanece o mesmo)

    private void salvarResumoDefinitivamente() {
        Resumo novoResumo = new Resumo();
        novoResumo.setTitulo(titulo);
        novoResumo.setDescricao(descricao);
        novoResumo.setCategoriaId(idCategoria);
        novoResumo.setCaminhoAudio(caminhoDoArquivoDeAudio);
        resumoDao.adicionarResumo(novoResumo);
        Toast.makeText(this, "Resumo salvo com sucesso!", Toast.LENGTH_LONG).show();
        Intent intent = new Intent(GravarActivity.this, ListResumosActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
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
            isRecording = true;
            chronometer.setBase(SystemClock.elapsedRealtime());
            chronometer.start();
            buttonGravar.setBackgroundResource(R.drawable.circle_background_recording);
            Toast.makeText(this, "Gravação iniciada!", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
            isRecording = false;
            Toast.makeText(this, "Falha ao iniciar gravação.", Toast.LENGTH_SHORT).show();
        }
    }

    private void pararGravacao() {
        if (mediaRecorder != null && isRecording) {
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

    @Override
    protected void onPause() {
        super.onPause();
        if (isRecording) {
            pararGravacao();
        }
    }
}