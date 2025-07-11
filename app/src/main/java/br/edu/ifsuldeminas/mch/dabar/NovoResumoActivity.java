package br.edu.ifsuldeminas.mch.dabar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.List;

public class NovoResumoActivity extends AppCompatActivity {

    private EditText editTextTitulo;
    private EditText editTextDescricao;
    private Spinner spinnerCategoria;
    private Button buttonGravar;
    private int idCategoriaSelecionada = -1;
    private BottomNavigationView bottomNavigation; // Adicionado

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_novo_resumo);

        editTextTitulo = findViewById(R.id.edit_text_titulo);
        editTextDescricao = findViewById(R.id.edit_text_descricao);
        spinnerCategoria = findViewById(R.id.spinner_categoria);
        buttonGravar = findViewById(R.id.btn_gravar_resumo);
        bottomNavigation = findViewById(R.id.bottom_navigation); // Adicionado

        carregarCategorias();
        setupBottomNavigation(); // Adicionado

        buttonGravar.setOnClickListener(view -> {
            String titulo = editTextTitulo.getText().toString().trim();
            String descricao = editTextDescricao.getText().toString().trim();

            if (titulo.isEmpty()) {
                Toast.makeText(this, "O campo Título é obrigatório.", Toast.LENGTH_SHORT).show();
                return;
            }
            if (idCategoriaSelecionada == -1) {
                Toast.makeText(this, "Selecione uma categoria.", Toast.LENGTH_SHORT).show();
                return;
            }

            Intent intent = new Intent(NovoResumoActivity.this, GravarActivity.class);
            intent.putExtra("EXTRA_TITULO", titulo);
            intent.putExtra("EXTRA_DESCRICAO", descricao);
            intent.putExtra("EXTRA_CATEGORIA", idCategoriaSelecionada);
            startActivity(intent);
        });
    }

    private void carregarCategorias() {
        CategoriaDAO dao = new CategoriaDAO(this);
        List<Categoria> categorias = dao.listarTodasCategorias();

        ArrayAdapter<Categoria> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item,
                categorias);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategoria.setAdapter(adapter);

        spinnerCategoria.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Categoria categoriaSelecionada = (Categoria) parent.getItemAtPosition(position);
                if (categoriaSelecionada != null) {
                    idCategoriaSelecionada = categoriaSelecionada.getId();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                idCategoriaSelecionada = -1;
            }
        });
    }

    // Lógica de navegação adicionada
    private void setupBottomNavigation() {
        // Nenhum item selecionado por padrão aqui, pois é uma tela de ação
        bottomNavigation.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.navigation_home) {
                startActivity(new Intent(this, MainActivity.class));
                return true;
            } else if (itemId == R.id.navigation_library) {
                startActivity(new Intent(this, BibliotecaActivity.class));
                return true;
            } else if (itemId == R.id.navigation_new_category) {
                startActivity(new Intent(this, NovaCategoriaActivity.class));
                return true;
            }
            return false;
        });
    }
}