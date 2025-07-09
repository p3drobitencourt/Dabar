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
        setContentView(R.layout.activity_biblioteca); // Usa o layout da Library

        // Mapeia o RecyclerView e o Botão Flutuante (FAB) do seu layout
        // ATENÇÃO: Verifique os IDs no seu arquivo activity_library.xml
        recyclerViewCategorias = findViewById(R.id.recyclerViewResumos); // MUDE O ID se for diferente
        fabNovaCategoria = findViewById(R.id.fab_nova_categoria); // ADICIONE ESTE BOTÃO NO SEU XML

        dao = new CategoriaDAO(this);
        categorias = dao.listarTodasCategorias();

        // Configura a RecyclerView com o novo Adapter de Categorias
        adapter = new AdapterCategorias(this, categorias);
        recyclerViewCategorias.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewCategorias.setAdapter(adapter);

        // Ação do botão para criar uma nova categoria
        fabNovaCategoria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(BibliotecaActivity.this, NovaCategoriaActivity.class));
            }
        });

        // Opcional: Adicionar menu de contexto para editar/deletar categorias
        registerForContextMenu(recyclerViewCategorias);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Recarrega a lista de categorias sempre que o usuário voltar para esta tela
        if (dao != null && adapter != null) {
            categorias.clear();
            categorias.addAll(dao.listarTodasCategorias());
            adapter.notifyDataSetChanged();
            if (categorias.isEmpty()) {
                Toast.makeText(this, "Nenhuma categoria encontrada. Crie uma nova!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    // A lógica do menu de contexto (clique longo) agora se aplicaria a categorias
    // (editar/deletar categoria), usando os novos métodos do CategoriaDAO.
}