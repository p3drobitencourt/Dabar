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

// --- IMPORTAÇÕES DO ROOM ---
import br.edu.ifsuldeminas.mch.dabar.CategoriaDAO;
import br.edu.ifsuldeminas.mch.dabar.AppDatabase;

public class NovoResumoActivity extends AppCompatActivity {

    private EditText editTextTitulo;
    private EditText editTextDescricao;
    private Spinner spinnerCategoria;
    private Button buttonGravar;
    private int idCategoriaSelecionada = -1;
    private BottomNavigationView bottomNavigation;

    // --- DAO DO ROOM ---
    private CategoriaDAO categoriaDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_novo_resumo);

        editTextTitulo = findViewById(R.id.edit_text_titulo);
        editTextDescricao = findViewById(R.id.edit_text_descricao);
        spinnerCategoria = findViewById(R.id.spinner_categoria);
        buttonGravar = findViewById(R.id.btn_gravar_resumo);
        bottomNavigation = findViewById(R.id.bottom_navigation);

       categoriaDao = AppDatabase.getDatabase(this).categoriaDao();

        setupBottomNavigation();

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
            // Passamos o ID da categoria, como já era feito antes.
            intent.putExtra("EXTRA_CATEGORIA_ID", idCategoriaSelecionada);
            startActivity(intent);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Recarrega as categorias toda vez para garantir que a lista esteja atualizada
        carregarCategorias();
    }

    private void carregarCategorias() {
        // --- USO DO DAO DO ROOM ---
        List<Categoria> categorias = categoriaDao.listarTodasCategorias();

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

    private void setupBottomNavigation() {
        bottomNavigation.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.navigation_home) {
                startActivity(new Intent(this, MainActivity.class));
                return true;
            } else if (itemId == R.id.navigation_library) {
                startActivity(new Intent(this, ListResumosActivity.class));
                return true;
            } else if (itemId == R.id.navigation_new_category) {
                startActivity(new Intent(this, NovaCategoriaActivity.class));
                return true;
            }
            return false;
        });
    }
}