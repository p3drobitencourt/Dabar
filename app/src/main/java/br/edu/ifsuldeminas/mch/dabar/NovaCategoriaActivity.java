package br.edu.ifsuldeminas.mch.dabar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.textfield.TextInputEditText;

// --- IMPORTAÇÕES DO ROOM ---
import br.edu.ifsuldeminas.mch.dabar.CategoriaDAO;
import br.edu.ifsuldeminas.mch.dabar.AppDatabase;

public class NovaCategoriaActivity extends AppCompatActivity {

    private TextInputEditText editTextTitulo;
    private TextInputEditText editTextDescricao;
    private Button btnCadastrar;
    private BottomNavigationView bottomNavigation;

    // --- DAO DO ROOM ---
    // Substituímos a implementação antiga pela interface do Room.
    private CategoriaDAO categoriaDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nova_categoria);

        editTextTitulo = findViewById(R.id.edit_text_titulo_categoria);
        editTextDescricao = findViewById(R.id.edit_text_descricao_categoria);
        btnCadastrar = findViewById(R.id.btn_cadastrar_categoria);
        bottomNavigation = findViewById(R.id.bottom_navigation);

        // --- INICIALIZAÇÃO DO DAO DO ROOM ---
        // Em vez de 'new CategoriaDAO(this)', obtemos o DAO através da classe AppDatabase.
        categoriaDao = AppDatabase.getDatabase(this).categoriaDao();

        btnCadastrar.setOnClickListener(v -> salvarCategoria());

        setupBottomNavigation();
    }

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
            // --- USO DO DAO DO ROOM ---
            // A chamada do método é a mesma, mas agora ela executa o código gerado pelo Room.
            categoriaDao.adicionarCategoria(novaCategoria);
            Toast.makeText(this, "Categoria cadastrada com sucesso!", Toast.LENGTH_SHORT).show();
            finish(); // Fecha a activity e volta para a anterior
        } catch (Exception e) {
            Toast.makeText(this, "Erro ao cadastrar categoria.", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

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
                return true; // Já estamos aqui
            }
            return false;
        });
    }
}