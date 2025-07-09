package br.edu.ifsuldeminas.mch.dabar;

import android.media.MediaRecorder;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;

public class GravarActivity extends AppCompatActivity {

    private boolean isRecording = false;

    private Button buttonGravar;

    private TextView textView_timer;

    // --- Dados recebidos da tela anterior ---
    private String titulo, descricao;

    private int categoria;

    // --- Lógica de Gravação ---
    private MediaRecorder mediaRecorder;
    private String caminhoDoArquivoDeAudio = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gravar);

        titulo = getIntent().getStringExtra("EXTRA_TITULO");
        descricao = getIntent().getStringExtra("EXTRA_DESCRICAO");
        categoria = getIntent().getIntExtra("EXTRA_CATEGORIA", 0);

        textView_timer = findViewById(R.id.textView_timer);
        buttonGravar = findViewById(R.id.buttonGravar);
        buttonGravar.setOnClickListener(v -> {
            if (!isRecording) {
                iniciarGravacao();
                isRecording = true;
            }else{
                pararGravacao();
                isRecording = false;
            }
        });
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
            buttonGravar.setBackgroundColor(0xFF18494E);
            Toast.makeText(this, "Gravação iniciada!", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    private void pararGravacao() {
        if (mediaRecorder != null) {
            mediaRecorder.stop();
            mediaRecorder.release();
            mediaRecorder = null;
            isRecording = false;
            buttonGravar.setEnabled(false); // Desabilita o botão para evitar cliques duplos

            // AGORA, SALVA AUTOMATICAMENTE
            salvarResumoDefinitivamente();
        }
    }

    private void salvarResumoDefinitivamente() {

        ResumoDAO dao = new ResumoDAO(this);
        Resumo novoResumo = new Resumo();

        novoResumo.setTitulo(titulo);
        // novoResumo.setDescricao(descricao);
        //novoResumo.setCategoria(categoria);
        novoResumo.setCaminhoAudio(caminhoDoArquivoDeAudio);

        dao.adicionarResumo(novoResumo);

        Toast.makeText(this, "Resumo salvo com sucesso!", Toast.LENGTH_LONG).show();

        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        isRecording = false;
    }
}

