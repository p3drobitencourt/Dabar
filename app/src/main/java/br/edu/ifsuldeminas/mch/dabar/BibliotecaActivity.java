package br.edu.ifsuldeminas.mch.dabar;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

import br.edu.ifsuldeminas.mch.dabar.CategoriaDAO;
import br.edu.ifsuldeminas.mch.dabar.AppDatabase;

public class BibliotecaActivity extends BaseActivity {

    private RecyclerView recyclerViewCategorias;
    private AdapterCategorias adapter;
    private List<Categoria> categorias;
    private FloatingActionButton fabNovaCategoria;
    private CategoriaDAO categoriaDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_biblioteca);


        setupToolbar(true);
        setupBottomNavigationWithoutSelection();

        recyclerViewCategorias = findViewById(R.id.recyclerViewCategorias);
        fabNovaCategoria = findViewById(R.id.fab_nova_categoria);

        categoriaDao = AppDatabase.getDatabase(this).categoriaDao();

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
        categorias = categoriaDao.listarTodasCategorias();
        if (adapter == null) {
            adapter = new AdapterCategorias(this, categorias);
            recyclerViewCategorias.setLayoutManager(new LinearLayoutManager(this));
            recyclerViewCategorias.setAdapter(adapter);
        } else {
            adapter.atualizarLista(categorias);
        }
        if (categorias.isEmpty()) {
            Toast.makeText(this, "Nenhuma categoria encontrada. Crie uma nova!", Toast.LENGTH_SHORT).show();
        }
    }
}