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
    private int idCategoriaSelecionada;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_novo_resumo);

        buttonGravar = findViewById(R.id.btn_gravar_resumo);
        editTextTitulo = findViewById(R.id.edit_text_titulo);
        editTextDescricao = findViewById(R.id.edit_text_descricao);
        spinnerCategoria = findViewById(R.id.spinner_categoria);

        CategoriaDAO dao = new CategoriaDAO(this);
        List<Categoria> categorias = dao.listarTodasCategorias();

        // O ArrayAdapter agora é do tipo <Categoria>
        ArrayAdapter<Categoria> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item,
                categorias);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategoria.setAdapter(adapter);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategoria.setAdapter(adapter);

        // --- Listener para obter o ID ---
        spinnerCategoria.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() { // Correction here
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // Pega o OBJETO Categoria inteiro que foi selecionado
                Categoria categoriaSelecionada = (Categoria) parent.getItemAtPosition(position);

                // Extrai e guarda o ID do objeto
                if (categoriaSelecionada != null) {
                    idCategoriaSelecionada = categoriaSelecionada.getId();
                } else {
                    idCategoriaSelecionada = -1;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                idCategoriaSelecionada = -1;
            }
        });

        buttonGravar.setOnClickListener(view -> {
            String titulo = editTextTitulo.getText().toString().trim();
            String descricao = editTextDescricao.getText().toString().trim();
            String categoria = spinnerCategoria.getSelectedItem().toString().trim();

            if (titulo.isEmpty() || categoria.isEmpty()) {
                Toast.makeText(this, "Título e Categoria são obrigatórios.", Toast.LENGTH_SHORT).show();
                return;
            }

            // Empacota os dados e envia para a próxima tela
            Intent intent = new Intent(NovoResumoActivity.this, GravarActivity.class);
            intent.putExtra("EXTRA_TITULO", titulo);
            intent.putExtra("EXTRA_DESCRICAO", descricao);
            intent.putExtra("EXTRA_CATEGORIA", idCategoriaSelecionada);
            startActivity(intent);
        });
    }
}