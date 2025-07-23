package br.edu.ifsuldeminas.mch.dabar;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity; // Mantenha esta importação, pois sua BaseActivity provavelmente a utiliza.

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.textfield.TextInputEditText;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

// ✅ CORREÇÃO: Herda da sua BaseActivity, como no resto do projeto.
public class NovaCategoriaActivity extends BaseActivity {

    private TextInputEditText editTextTitulo;
    private TextInputEditText editTextDescricao;
    private Button btnCadastrar;

    // A variável para o DAO do Room está correta.
    private CategoriaDAO categoriaDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nova_categoria);

        setupToolbar(true); // Exibe o botão de voltar.

        // ✅ CORREÇÃO FINAL: Usa o ID correto para a tela atual.
        // Isso fará com que o ícone "Nova Categoria" fique selecionado.
        setupBottomNavigation(R.id.navigation_new_category);

        // Mapeamento dos componentes visuais.
        editTextTitulo = findViewById(R.id.edit_text_titulo_categoria);
        editTextDescricao = findViewById(R.id.edit_text_descricao_categoria);
        btnCadastrar = findViewById(R.id.btn_cadastrar_categoria);

        // Inicializa o DAO do Room.
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

        // ✅ MELHORIA: Usa um Executor para rodar a operação do banco de dados
        // fora da thread principal, que é a prática moderna no Android.
        Executor executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            try {
                categoriaDao.adicionarCategoria(novaCategoria);
                // Após o sucesso, volta para a thread principal para mostrar o Toast e fechar a tela.
                runOnUiThread(() -> {
                    Toast.makeText(this, "Categoria cadastrada com sucesso!", Toast.LENGTH_SHORT).show();
                    finish(); // Fecha a tela e retorna para a BibliotecaActivity
                });
            } catch (Exception e) {
                runOnUiThread(() -> {
                    Toast.makeText(this, "Erro ao cadastrar categoria.", Toast.LENGTH_SHORT).show();
                });
                e.printStackTrace();
            }
        });
    }
}