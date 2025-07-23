package br.edu.ifsuldeminas.mch.dabar;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.textfield.TextInputEditText;

// Importações corretas para o Room


public class NovaCategoriaActivity extends BaseActivity {

    private TextInputEditText editTextTitulo;
    private TextInputEditText editTextDescricao;
    private Button btnCadastrar;
    private BottomNavigationView bottomNavigation;

    // A variável agora usa a interface CategoriaDao do Room.
    private CategoriaDAO categoriaDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // O setContentView aponta para o layout que seu amigo criou, que está correto.
        setContentView(R.layout.activity_nova_categoria);

        setupToolbar(true);
        setupBottomNavigationWithoutSelection();

        // O Java vai encontrar esses IDs no XML do seu amigo.
        editTextTitulo = findViewById(R.id.edit_text_titulo_categoria);
        editTextDescricao = findViewById(R.id.edit_text_descricao_categoria);
        btnCadastrar = findViewById(R.id.btn_cadastrar_categoria);
        bottomNavigation = findViewById(R.id.bottom_navigation);

        // INICIALIZAÇÃO CORRETA DO DAO DO ROOM
        categoriaDao = AppDatabase.getDatabase(this).categoriaDao();

        btnCadastrar.setOnClickListener(v -> salvarCategoria());
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
            // A chamada agora é para o DAO do Room.
            categoriaDao.adicionarCategoria(novaCategoria);
            Toast.makeText(this, "Categoria cadastrada com sucesso!", Toast.LENGTH_SHORT).show();
            finish();
        } catch (Exception e) {
            Toast.makeText(this, "Erro ao cadastrar categoria.", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

}