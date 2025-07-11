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

public class BibliotecaActivity extends AppCompatActivity {

    private RecyclerView recyclerViewCategorias;
    private AdapterCategorias adapter;
    private CategoriaDAO dao;
    private List<Categoria> categorias;
    private FloatingActionButton fabNovaCategoria;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_biblioteca);

        recyclerViewCategorias = findViewById(R.id.recyclerViewCategorias);
        fabNovaCategoria = findViewById(R.id.fab_nova_categoria);

        dao = new CategoriaDAO(this);
        categorias = dao.listarTodasCategorias();

        adapter = new AdapterCategorias(this, categorias);
        recyclerViewCategorias.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewCategorias.setAdapter(adapter);
        fabNovaCategoria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(BibliotecaActivity.this, NovaCategoriaActivity.class));
            }
        });

        registerForContextMenu(recyclerViewCategorias);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (dao != null && adapter != null) {
            categorias.clear();
            categorias.addAll(dao.listarTodasCategorias());
            adapter.notifyDataSetChanged();
            if (categorias.isEmpty()) {
                Toast.makeText(this, "Nenhuma categoria encontrada. Crie uma nova!", Toast.LENGTH_SHORT).show();
            }
        }
    }
}