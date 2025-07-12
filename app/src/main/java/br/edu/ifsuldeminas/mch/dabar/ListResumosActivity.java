package br.edu.ifsuldeminas.mch.dabar;

import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.IOException;
import java.util.List;

public class ListResumosActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private AdapterResumos adapter;
    private ResumoDAO dao;
    private List<Resumo> resumos;
    private MediaPlayer mediaPlayer;

    /**
     * Initializes the activity, sets the layout, and prepares the RecyclerView and data access object for displaying resumos.
     *
     * @param savedInstanceState the previously saved state of the activity, or null if none exists
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_resumos);
        recyclerView = findViewById(R.id.recyclerViewResumos);
        dao = new ResumoDAO(this);
    }

    /**
     * Refreshes and displays the list of resumos when the activity resumes.
     */
    @Override
    protected void onResume() {
        super.onResume();
        setupRecyclerView();
    }

    /**
     * Initializes and configures the RecyclerView to display the current list of resumos.
     *
     * Retrieves all resumos from the data source, sets up the adapter with audio playback support,
     * applies a linear layout manager, and registers the RecyclerView for context menu actions.
     */
    private void setupRecyclerView() {
        resumos = dao.listarTodosResumos();
        adapter = new AdapterResumos(this, resumos, this::ouvirResumo);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        registerForContextMenu(recyclerView);
    }

    /**
     * Inflates the context menu for a resumo item when the context menu is created.
     *
     * @param menu      the context menu to be built
     * @param v         the view for which the context menu is being built
     * @param menuInfo  additional information about the context menu
     */
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getMenuInflater().inflate(R.menu.context_menu_resumo, menu);
    }

    /**
     * Handles context menu item selections for resumos, performing actions such as playing audio, editing, or deleting the selected item.
     *
     * @param item The selected menu item.
     * @return true if the menu action was handled; false otherwise.
     */
    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        int position = adapter.getLongClickedPosition();
        if (position < 0 || position >= resumos.size()) {
            return super.onContextItemSelected(item);
        }
        Resumo resumoSelecionado = resumos.get(position);

        int itemId = item.getItemId();
        if (itemId == R.id.menu_ouvir) { //
            ouvirResumo(resumoSelecionado);
            return true;
        } else if (itemId == R.id.menu_editar) { //
            Toast.makeText(this, "Funcionalidade de editar em construção...", Toast.LENGTH_SHORT).show(); //
            return true;
        } else if (itemId == R.id.menu_apagar) { //
            apagarResumo(resumoSelecionado, position);
            return true;
        } else {
            return super.onContextItemSelected(item);
        }
    }

    /**
     * Plays the audio file associated with the given resumo.
     *
     * If an audio file path is not available or playback fails, a toast message is shown to the user.
     * Releases any existing MediaPlayer instance before starting playback and ensures resources are released after playback completes.
     *
     * @param resumo The resumo whose audio should be played.
     */
    public void ouvirResumo(Resumo resumo) {
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }

        String caminhoAudio = resumo.getCaminhoAudio(); //
        if (caminhoAudio == null || caminhoAudio.isEmpty()) {
            Toast.makeText(this, "Arquivo de áudio não encontrado.", Toast.LENGTH_SHORT).show();
            return;
        }

        mediaPlayer = new MediaPlayer();

        // APRIMORAMENTO: Garante que o som saia pelo canal de mídia
        AudioAttributes audioAttributes = new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_MEDIA)
                .setContentType(AudioAttributes.CONTENT_TYPE_SPEECH)
                .build();
        mediaPlayer.setAudioAttributes(audioAttributes);

        try {
            mediaPlayer.setDataSource(caminhoAudio);
            mediaPlayer.prepare();
            mediaPlayer.start();
            Toast.makeText(this, "Tocando resumo...", Toast.LENGTH_SHORT).show();
            mediaPlayer.setOnCompletionListener(mp -> {
                mp.release();
                mediaPlayer = null;
            });
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Erro ao tentar reproduzir o áudio.", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Deletes the specified resumo from the data source and removes it from the displayed list.
     *
     * Updates the RecyclerView to reflect the removal and shows a confirmation toast.
     *
     * @param resumo   The resumo object to be deleted.
     * @param position The position of the resumo in the list.
     */
    private void apagarResumo(Resumo resumo, int position) {
        dao.deletarResumo(resumo); //
        resumos.remove(position);
        adapter.notifyItemRemoved(position);
        adapter.notifyItemRangeChanged(position, resumos.size());
        Toast.makeText(this, "Resumo apagado!", Toast.LENGTH_SHORT).show(); //
    }

    /**
     * Releases the MediaPlayer resources if active when the activity stops.
     *
     * Ensures that audio playback is properly terminated and system resources are freed when the activity transitions out of the foreground.
     */
    @Override
    protected void onStop() {
        super.onStop();
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }
}