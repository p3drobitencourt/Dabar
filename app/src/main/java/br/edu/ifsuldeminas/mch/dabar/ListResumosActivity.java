package br.edu.ifsuldeminas.mch.dabar;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_resumos);

        recyclerView = findViewById(R.id.recyclerViewResumos);
        dao = new ResumoDAO(this);
        resumos = dao.listarTodosResumos();

        // Configura o adapter com o listener de clique simples
        adapter = new AdapterResumos(this, resumos, resumo -> {
            playAudio(resumo.getCaminhoAudio());
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        // Registra o RecyclerView para o menu de contexto (clique longo)
        registerForContextMenu(recyclerView);
    }

    // --- Lógica do Menu de Contexto (Clique Longo) ---

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getMenuInflater().inflate(R.menu.context_menu_resumo, menu);
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        int position = adapter.getLongClickedPosition();
        if (position < 0 || position >= resumos.size()) {
            return super.onContextItemSelected(item); // Posição inválida
        }
        Resumo resumoSelecionado = resumos.get(position);

        int itemId = item.getItemId();
        if (itemId == R.id.menu_ouvir) {
            playAudio(resumoSelecionado.getCaminhoAudio());
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

    // --- Lógica das Ações ---

    private void apagarResumo(Resumo resumo, int position) {
        dao.deletarResumo(resumo);
        resumos.remove(position);
        adapter.notifyItemRemoved(position);
        adapter.notifyItemRangeChanged(position, resumos.size()); // Atualiza as posições
        Toast.makeText(this, "Resumo apagado!", Toast.LENGTH_SHORT).show();
    }

    private void playAudio(String path) {
        if (path == null || path.isEmpty()) {
            Toast.makeText(this, "Arquivo de áudio não encontrado.", Toast.LENGTH_SHORT).show();
            return;
        }

        stopAudio(); // Para o áudio anterior, se houver

        mediaPlayer = new MediaPlayer();
        try {
            mediaPlayer.setDataSource(path);
            mediaPlayer.prepare();
            mediaPlayer.start();
            Toast.makeText(this, "Tocando resumo...", Toast.LENGTH_SHORT).show();

            mediaPlayer.setOnCompletionListener(mp -> {
                stopAudio();
                Toast.makeText(ListResumosActivity.this, "Reprodução finalizada.", Toast.LENGTH_SHORT).show();
            });

        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Não foi possível tocar o áudio.", Toast.LENGTH_SHORT).show();
        }
    }

    private void stopAudio() {
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    // --- Ciclo de Vida da Activity ---

    @Override
    protected void onStop() {
        super.onStop();
        stopAudio(); // Garante que o áudio pare se o usuário sair da tela
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Atualiza a lista caso um novo resumo tenha sido adicionado
        resumos.clear();
        resumos.addAll(dao.listarTodosResumos());
        adapter.notifyDataSetChanged();
    }
}