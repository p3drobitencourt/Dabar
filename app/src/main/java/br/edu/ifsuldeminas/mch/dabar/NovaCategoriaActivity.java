package br.edu.ifsuldeminas.mch.dabar;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.textfield.TextInputEditText;

// --- IMPORTAÇÕES CORRIGIDAS DO ROOM ---
// Apontando para os pacotes corretos que criamos
import br.edu.ifsuldeminas.mch.dabar.CategoriaDAO;
import br.edu.ifsuldeminas.mch.dabar.AppDatabase;

public class NovaCategoriaActivity extends AppCompatActivity {

    private TextInputEditText editTextTitulo;
    private TextInputEditText editTextDescricao;
    private Button btnCadastrar;
    private BottomNavigationView bottomNavigation;

    // --- DAO DO ROOM CORRIGIDO ---
    // A variável agora usa a interface CategoriaDao que criamos.
    private CategoriaDAO categoriaDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nova_categoria);

        // Agora o Java vai encontrar esses IDs no XML corrigido.
        editTextTitulo = findViewById(R.id.edit_text_titulo_categoria);
        editTextDescricao = findViewById(R.id.edit_text_descricao_categoria);
        btnCadastrar = findViewById(R.id.btn_cadastrar_categoria);
        bottomNavigation = findViewById(R.id.bottom_navigation);

        // --- INICIALIZAÇÃO CORRETA DO DAO DO ROOM ---
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
            categoriaDao.adicionarCategoria(novaCategoria);
            Toast.makeText(this, "Categoria cadastrada com sucesso!", Toast.LENGTH_SHORT).show();
            finish();
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
                return true;
            }
            return false;
        });
    }
}