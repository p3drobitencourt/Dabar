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

import java.util.List;

public class NovoResumoActivity extends AppCompatActivity {

    private EditText editTextTitulo;
    private EditText editTextDescricao;
    private Spinner spinnerCategoria;
    private Button buttonGravar;
    private int idCategoriaSelecionada = -1; // Inicia com valor inválido

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_novo_resumo);

        editTextTitulo = findViewById(R.id.edit_text_titulo);
        editTextDescricao = findViewById(R.id.edit_text_descricao);
        spinnerCategoria = findViewById(R.id.spinner_categoria);
        buttonGravar = findViewById(R.id.btn_gravar_resumo);

        // Popula o Spinner com as categorias do banco de dados
        carregarCategorias();

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

        // Cria um adapter para o Spinner
        ArrayAdapter<Categoria> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item,
                categorias);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategoria.setAdapter(adapter);

        // Listener para pegar o ID da categoria selecionada
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
}