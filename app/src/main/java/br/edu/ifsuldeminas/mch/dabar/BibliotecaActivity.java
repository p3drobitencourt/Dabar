package br.edu.ifsuldeminas.mch.dabar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

// --- IMPORTAÇÕES DO ROOM ---
import br.edu.ifsuldeminas.mch.dabar.CategoriaDAO;
import br.edu.ifsuldeminas.mch.dabar.AppDatabase;

public class BibliotecaActivity extends AppCompatActivity {

    private RecyclerView recyclerViewCategorias;
    private AdapterCategorias adapter;
    private List<Categoria> categorias;
    private FloatingActionButton fabNovaCategoria;

    // --- DAO DO ROOM ---
    private CategoriaDAO categoriaDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_biblioteca);

        recyclerViewCategorias = findViewById(R.id.recyclerViewCategorias);
        fabNovaCategoria = findViewById(R.id.fab_nova_categoria);

        // --- INICIALIZAÇÃO DO DAO ---
        categoriaDao = AppDatabase.getDatabase(this).categoriaDao();

        // A busca inicial dos dados pode ser movida para o onResume para garantir
        // que a lista seja sempre atualizada.

        fabNovaCategoria.setOnClickListener(v ->
                startActivity(new Intent(BibliotecaActivity.this, NovaCategoriaActivity.class))
        );

        registerForContextMenu(recyclerViewCategorias);
    }

    @Override
    protected void onResume() {
        super.onResume();
        carregarCategorias();
    }

    private void carregarCategorias() {
        // --- USO DO DAO DO ROOM ---
        categorias = categoriaDao.listarTodasCategorias();

        if (adapter == null) {
            adapter = new AdapterCategorias(this, categorias);
            recyclerViewCategorias.setLayoutManager(new LinearLayoutManager(this));
            recyclerViewCategorias.setAdapter(adapter);
        } else {
            // Se o adapter já existe, apenas atualiza a lista e notifica as mudanças.
            adapter.atualizarLista(categorias);
        }

        if (categorias.isEmpty()) {
            Toast.makeText(this, "Nenhuma categoria encontrada. Crie uma nova!", Toast.LENGTH_SHORT).show();
        }
    }
}