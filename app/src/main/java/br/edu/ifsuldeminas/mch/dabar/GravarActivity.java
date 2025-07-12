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

    /**
     * Initializes the activity, sets up UI components, retrieves intent extras, and configures the record button to handle audio recording actions.
     *
     * @param savedInstanceState The previously saved state of the activity, or null if none exists.
     */
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

    /**
     * Checks if the audio recording permission is granted and requests it if necessary; starts recording if permission is already granted.
     */
    private void verificarPermissaoEGravar() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO}, REQUEST_RECORD_AUDIO_PERMISSION);
        } else {
            iniciarGravacao();
        }
    }

    /**
     * Handles the result of the audio recording permission request.
     *
     * If permission is granted, starts audio recording. If denied, notifies the user that recording is not possible.
     *
     * @param requestCode  the request code passed in the permission request
     * @param permissions  the requested permissions
     * @param grantResults the grant results for the corresponding permissions
     */
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

    /**
     * Starts audio recording and updates the UI to reflect the recording state.
     *
     * Prepares and starts the MediaRecorder to capture audio from the microphone, saves the audio file to external storage with a timestamped filename, and starts the chronometer. Updates the recording button's appearance and notifies the user. If recording fails to start, displays an error message and ensures the recording state is not set.
     */
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

    /**
     * Stops the ongoing audio recording, releases resources, updates the UI, and saves the recording metadata.
     *
     * If a recording is active, this method stops and releases the MediaRecorder, stops the chronometer,
     * disables the record button, resets its background, and persists the recording information.
     * Handles potential runtime exceptions during the stop operation.
     */
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

    /**
     * Saves the recorded audio and its metadata to the database, then navigates to the list of summaries.
     *
     * Creates a new summary with the current title, description, category, and audio file path, persists it using the DAO, displays a confirmation message, and returns to the summary list activity.
     */
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

    /**
     * Handles the activity pause event by stopping audio recording if it is active.
     *
     * Ensures that recording is properly stopped and resources are released when the activity is paused.
     */
    @Override
    protected void onPause() {
        super.onPause();
        if (isRecording) {
            pararGravacao();
        }
    }
}