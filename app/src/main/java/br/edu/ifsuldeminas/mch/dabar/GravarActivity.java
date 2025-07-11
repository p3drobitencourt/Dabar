package br.edu.ifsuldeminas.mch.dabar;

import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class GravarActivity extends AppCompatActivity {

    // Variáveis para a contagem do tempo
    private Handler timerHandler = new Handler();
    private long startTime = 0L;
    private long duracaoEmMs = 0L;

    private boolean isRecording = false;

    private FrameLayout buttonGravar;

    private TextView textView_timer;

    private String titulo, descricao;

    private int categoriaId;

    private MediaRecorder mediaRecorder;
    private String caminhoDoArquivoDeAudio = null;
    private String tempoFormatado = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gravar);

        titulo = getIntent().getStringExtra("EXTRA_TITULO");
        descricao = getIntent().getStringExtra("EXTRA_DESCRICAO");
        categoriaId = getIntent().getIntExtra("EXTRA_CATEGORIA", 0);

        textView_timer = findViewById(R.id.text_timer);
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

        // --- INICIA O TIMER ---
        startTime = System.currentTimeMillis();
        timerHandler.post(updateTimerRunnable);

        try {
            mediaRecorder.prepare();
            mediaRecorder.start();
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
            buttonGravar.setEnabled(false); // Desabilita o botão para evitar cliques duplos

            // --- PARA O TIMER ---
            timerHandler.removeCallbacks(updateTimerRunnable);

            // AGORA, SALVA AUTOMATICAMENTE
            salvarResumoDefinitivamente();
        }
    }

    private void salvarResumoDefinitivamente() {

        ResumoDAO dao = new ResumoDAO(this);
        Resumo novoResumo = new Resumo();

        CategoriaDAO daoCategoria = new CategoriaDAO(this);
        //Categoria categoria = daoCategoria.getCategoriaById(categoriaId);

        novoResumo.setTitulo(titulo);
        novoResumo.setDescricao(descricao);
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

    private Runnable updateTimerRunnable = new Runnable() {
        @Override
        public void run() {
            duracaoEmMs = System.currentTimeMillis() - startTime;

            // Formata os milissegundos para o formato MM:SS
            tempoFormatado = String.format("%02d:%02d",
                    TimeUnit.MILLISECONDS.toMinutes(duracaoEmMs),
                    TimeUnit.MILLISECONDS.toSeconds(duracaoEmMs) -
                            TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(duracaoEmMs))
            );

            textView_timer.setText(String.format("Gravando... %s", tempoFormatado));

            // Agenda a próxima execução para daqui a 1 segundo
            timerHandler.postDelayed(this, 1000);
        }
    };
}

