package br.edu.ifsuldeminas.mch.dabar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.textfield.TextInputEditText;

public class NovaCategoriaActivity extends AppCompatActivity {

    private TextInputEditText editTextTitulo;
    private TextInputEditText editTextDescricao;
    private Button btnCadastrar;
    private CategoriaDAO dao;
    private BottomNavigationView bottomNavigation; /**
     * Initializes the activity, sets up the user interface components, and configures event listeners for creating a new category and handling bottom navigation actions.
     *
     * @param savedInstanceState the previously saved state of the activity, or null if none exists
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nova_categoria);

        editTextTitulo = findViewById(R.id.edit_text_titulo_categoria);
        editTextDescricao = findViewById(R.id.edit_text_descricao_categoria);
        btnCadastrar = findViewById(R.id.btn_cadastrar_categoria);
        bottomNavigation = findViewById(R.id.bottom_navigation); // Adicionado
        dao = new CategoriaDAO(this);

        btnCadastrar.setOnClickListener(new View.OnClickListener() {
            /**
             * Handles the click event for the "Cadastrar" button by invoking the method to save a new category.
             */
            @Override
            public void onClick(View v) {
                salvarCategoria();
            }
        });

        setupBottomNavigation(); // Adicionado
    }

    /**
     * Validates input fields and attempts to create a new category in the database.
     *
     * Shows a toast message if the title is missing or if the operation succeeds or fails. Finishes the activity upon successful creation.
     */
    private void salvarCategoria() {
        String titulo = editTextTitulo.getText().toString().trim();
        String descricao = editTextDescricao.getText().toString().trim();

        if (titulo.isEmpty()) {
            Toast.makeText(this, "O título é obrigatório!", Toast.LENGTH_SHORT).show();
            return;
        }

        Categoria novaCategoria = new Categoria();
        novaCategoria.setTitulo(titulo);
        novaCategoria.setDescricao(descricao);

        try {
            dao.adicionarCategoria(novaCategoria);
            Toast.makeText(this, "Categoria cadastrada com sucesso!", Toast.LENGTH_SHORT).show();
            finish();
        } catch (Exception e) {
            Toast.makeText(this, "Erro ao cadastrar categoria.", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    /**
     * Configures the bottom navigation bar to handle navigation between main, library, and new category activities.
     *
     * Sets the current selection to the "new category" item and defines navigation actions for each menu item.
     */
    private void setupBottomNavigation() {
        bottomNavigation.setSelectedItemId(R.id.navigation_new_category);

        bottomNavigation.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.navigation_home) {
                startActivity(new Intent(this, MainActivity.class));
                return true;
            } else if (itemId == R.id.navigation_library) {
                startActivity(new Intent(this, BibliotecaActivity.class));
                return true;
            } else if (itemId == R.id.navigation_new_category) {
                // Já estamos aqui
                return true;
            }
            return false;
        });
    }
}