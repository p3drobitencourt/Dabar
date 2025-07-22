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

// --- IMPORTAÇÕES DO ROOM ---
import br.edu.ifsuldeminas.mch.dabar.ResumoDAO;
import br.edu.ifsuldeminas.mch.dabar.CategoriaDAO;
import br.edu.ifsuldeminas.mch.dabar..AppDatabase;

public class ListResumosActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private AdapterResumos adapter;
    private List<Resumo> resumos;
    private MediaPlayer mediaPlayer;

    // --- DAOs DO ROOM ---
    private ResumoDAO resumoDao;
    private CategoriaDAO categoriaDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_resumos);
        recyclerView = findViewById(R.id.recyclerViewResumos);

        // --- INICIALIZAÇÃO DOS DAOs ---
        AppDatabase db = AppDatabase.getDatabase(this);
        resumoDao = db.resumoDao();
        categoriaDao = db.categoriaDao();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setupRecyclerView();
    }

    private void setupRecyclerView() {
        // 1. Busca todos os resumos do banco.
        resumos = resumoDao.listarTodosResumos();

        // 2. Para cada resumo, busca e associa sua respectiva categoria.
        for (Resumo resumo : resumos) {
            Categoria categoria = categoriaDao.findCategoriaById(resumo.getCategoriaId());
            resumo.setCategoria(categoria);
        }

        // 3. Configura o adapter com a lista completa.
        adapter = new AdapterResumos(this, resumos, this::ouvirResumo);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        registerForContextMenu(recyclerView);
    }

    private void apagarResumo(Resumo resumo, int position) {
        // --- USO DO DAO DO ROOM ---
        resumoDao.deletarResumo(resumo);
        resumos.remove(position);
        adapter.notifyItemRemoved(position);
        adapter.notifyItemRangeChanged(position, resumos.size());
        Toast.makeText(this, "Resumo apagado!", Toast.LENGTH_SHORT).show();
    }

    // ... O restante da classe (lógica de menu de contexto, player de áudio) permanece o mesmo ...

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

    @Override
    protected void onStop() {
        super.onStop();
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }
}