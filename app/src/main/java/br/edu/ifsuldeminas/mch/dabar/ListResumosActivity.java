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
import androidx.appcompat.widget.Toolbar; // Importação necessária
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.io.IOException;
import java.util.List;
import br.edu.ifsuldeminas.mch.dabar.CategoriaDAO;
import br.edu.ifsuldeminas.mch.dabar.ResumoDAO;
import br.edu.ifsuldeminas.mch.dabar.AppDatabase;

public class ListResumosActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private AdapterResumos adapter;
    private List<Resumo> resumos;
    private MediaPlayer mediaPlayer;
    private ResumoDAO resumoDao;
    private CategoriaDAO categoriaDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_resumos);

        // --- CÓDIGO DA TOOLBAR COM SETA DE VOLTAR ---
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
        // --- FIM DA LÓGICA DA TOOLBAR ---

        recyclerView = findViewById(R.id.recyclerViewResumos);
        AppDatabase db = AppDatabase.getDatabase(this);
        resumoDao = db.resumoDao();
        categoriaDao = db.categoriaDao();
    }

    // --- CÓDIGO PARA FAZER A SETA FUNCIONAR ---
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    // --- FIM DO CÓDIGO DA SETA ---

    // LÓGICA ORIGINAL 100% PRESERVADA
    @Override
    protected void onResume() {
        super.onResume();
        setupRecyclerView();
    }

    private void setupRecyclerView() {
        resumos = resumoDao.listarTodosResumos();
        for (Resumo resumo : resumos) {
            Categoria categoria = categoriaDao.findCategoriaById(resumo.getCategoriaId());
            resumo.setCategoria(categoria);
        }
        adapter = new AdapterResumos(this, resumos, this::ouvirResumo);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        registerForContextMenu(recyclerView);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getMenuInflater().inflate(R.menu.context_menu_resumo, menu);
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        int position = adapter.getLongClickedPosition();
        if (position < 0 || position >= resumos.size()) {
            return super.onContextItemSelected(item);
        }
        Resumo resumoSelecionado = resumos.get(position);

        int itemId = item.getItemId();
        if (itemId == R.id.menu_ouvir) {
            ouvirResumo(resumoSelecionado);
            return true;
        } else if (itemId == R.id.menu_editar) {
            Toast.makeText(this, "Funcionalidade de editar em construção...", Toast.LENGTH_SHORT).show();
            return true;
        } else if (itemId == R.id.menu_apagar) {
            apagarResumo(resumoSelecionado, position);
            return true;
        } else {
            return super.onContextItemSelected(item);
        }
    }

    public void ouvirResumo(Resumo resumo) {
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }

        String caminhoAudio = resumo.getCaminhoAudio();
        if (caminhoAudio == null || caminhoAudio.isEmpty()) {
            Toast.makeText(this, "Arquivo de áudio não encontrado.", Toast.LENGTH_SHORT).show();
            return;
        }

        mediaPlayer = new MediaPlayer();

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

    private void apagarResumo(Resumo resumo, int position) {
        resumoDao.deletarResumo(resumo);
        resumos.remove(position);
        adapter.notifyItemRemoved(position);
        adapter.notifyItemRangeChanged(position, resumos.size());
        Toast.makeText(this, "Resumo apagado!", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }
}