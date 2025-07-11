
package br.edu.ifsuldeminas.mch.dabar;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;
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
        adapter = new AdapterResumos(this, resumos);
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
    public boolean onContextItemSelected(MenuItem item) {
        int position = adapter.getLongClickedPosition();
        Resumo resumoSelecionado = resumos.get(position);

        int itemId = item.getItemId();
        if (itemId == R.id.menu_ouvir) {
            ouvirResumo(resumoSelecionado);
            Toast.makeText(this, "Ouvindo...", Toast.LENGTH_SHORT).show();
            return true;
        } else if (itemId == R.id.menu_editar) {
            Toast.makeText(this, "Editando...", Toast.LENGTH_SHORT).show();
            return true;
        } else if (itemId == R.id.menu_apagar) {
            apagarResumo(resumoSelecionado, position);
            Toast.makeText(this, "Resumo apagado!", Toast.LENGTH_SHORT).show();
            return true;
        } else {
            return super.onContextItemSelected(item);
        }
    }

    public void ouvirResumo(Resumo resumo) {
        // Se já estiver tocando algo, pare e libere os recursos
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }

        String caminhoAudio = resumo.getCaminhoAudio();
        if (caminhoAudio == null || caminhoAudio.isEmpty()) {
            Toast.makeText(this, "Arquivo de áudio não encontrado.", Toast.LENGTH_SHORT).show();
            return;
        }

        Toast.makeText(this, "Iniciando reprodução...", Toast.LENGTH_SHORT).show();

        try {
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setDataSource(caminhoAudio); // Define o arquivo a ser tocado
            mediaPlayer.prepare(); // Prepara o player
            mediaPlayer.start();   // Inicia a reprodução

            // Listener para liberar os recursos quando a música acabar
            mediaPlayer.setOnCompletionListener(mp -> {
                mp.release();
                mediaPlayer = null;
                Toast.makeText(ListResumosActivity.this, "Reprodução finalizada.", Toast.LENGTH_SHORT).show();
            });

        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Erro ao tentar reproduzir o áudio.", Toast.LENGTH_SHORT).show();
        }
    }

    // Método para apagar (exemplo de como ficaria)
    private void apagarResumo(Resumo resumo, int position) {
        dao.deletarResumo(resumo);
        resumos.remove(position);
        adapter.notifyItemRemoved(position);
        Toast.makeText(this, "Resumo apagado!", Toast.LENGTH_SHORT).show();
    }

    // É MUITO IMPORTANTE liberar o MediaPlayer quando a activity for finalizada
    @Override
    protected void onStop() {
        super.onStop();
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }
}