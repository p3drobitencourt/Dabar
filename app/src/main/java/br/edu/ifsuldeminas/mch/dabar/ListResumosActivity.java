package br.edu.ifsuldeminas.mch.dabar;

import android.content.Intent;
import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer; // Importação correta para o Observer
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class ListResumosActivity extends BaseActivity { // Ou sua BaseActivity

    private RecyclerView recyclerView;
    private AdapterResumos adapter;
    private ResumoDAO resumoDao;
    private List<Resumo> resumos = new ArrayList<>();
    private MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_resumos);

        resumoDao = AppDatabase.getDatabase(getApplicationContext()).resumoDao();

        setupToolbar(true);
        setupRecyclerView();
        setupBottomNavigation();

        // ✅ AGORA ESTA LINHA FUNCIONA PERFEITAMENTE
        // O método 'listarTodosResumos()' retorna um LiveData, que tem o método 'observe'.
        resumoDao.listarTodosResumos().observe(this, new Observer<List<Resumo>>() {
            @Override
            public void onChanged(List<Resumo> listaDeResumos) {
                // Quando o banco de dados muda, este código é executado automaticamente.
                resumos.clear();
                resumos.addAll(listaDeResumos);
                adapter.notifyDataSetChanged();
            }
        });
    }

    private void setupRecyclerView() {
        adapter = new AdapterResumos(this, resumos, this::ouvirResumo);
        recyclerView = findViewById(R.id.recyclerViewResumos);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        registerForContextMenu(recyclerView);
    }

    private void setupBottomNavigation() {
        BottomNavigationView bottomNavigation = findViewById(R.id.bottom_navigation);
        bottomNavigation.setSelectedItemId(R.id.navigation_library);

        bottomNavigation.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.navigation_library) return true;

            if (itemId == R.id.navigation_home) {
                startActivity(new Intent(this, MainActivity.class));
                return true;
            } else if (itemId == R.id.navigation_new_category) {
                startActivity(new Intent(this, NovaCategoriaActivity.class));
                return true;
            }
            return false;
        });
    }

    private void apagarResumo(Resumo resumo) {
        Executor executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            resumoDao.deletarResumo(resumo);
            runOnUiThread(() -> Toast.makeText(this, "Resumo apagado!", Toast.LENGTH_SHORT).show());
        });
    }

    // --- O resto da classe (ouvir, menus, etc.) continua igual ---

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getMenuInflater().inflate(R.menu.context_menu_resumo, menu);
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        int position = adapter.getLongClickedPosition();
        if (position < 0 || position >= resumos.size()) return super.onContextItemSelected(item);
        Resumo resumoSelecionado = resumos.get(position);
        int itemId = item.getItemId();
        if (itemId == R.id.menu_ouvir) {
            ouvirResumo(resumoSelecionado);
            return true;
        } else if (itemId == R.id.menu_editar) {
            Toast.makeText(this, "Funcionalidade de editar em construção...", Toast.LENGTH_SHORT).show();
            return true;
        } else if (itemId == R.id.menu_apagar) {
            apagarResumo(resumoSelecionado);
            return true;
        }
        return super.onContextItemSelected(item);
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
        mediaPlayer.setAudioAttributes(new AudioAttributes.Builder().setUsage(AudioAttributes.USAGE_MEDIA).setContentType(AudioAttributes.CONTENT_TYPE_SPEECH).build());
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