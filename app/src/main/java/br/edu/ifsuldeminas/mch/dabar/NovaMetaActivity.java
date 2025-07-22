package br.edu.ifsuldeminas.mch.dabar;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

/**
 * Controla a tela de criação e edição de uma Meta de Estudo.
 * Responsável pelo 'C' (Create) e 'U' (Update) do nosso CRUD.
 */
public class NovaMetaActivity extends AppCompatActivity {

    private static final String TAG = "NovaMetaActivity";

    private TextInputEditText editTextTituloMeta;
    private TextInputEditText editTextDescricaoMeta;
    private Button buttonSalvarMeta;

    private FirebaseFirestore db;
    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nova_meta);

        // Inicializa o Firestore e pega o usuário logado
        db = FirebaseFirestore.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();

        // Conecta as variáveis Java com os IDs do XML
        editTextTituloMeta = findViewById(R.id.editTextTituloMeta);
        editTextDescricaoMeta = findViewById(R.id.editTextDescricaoMeta);
        buttonSalvarMeta = findViewById(R.id.buttonSalvarMeta);

        // Define a ação do botão de salvar
        buttonSalvarMeta.setOnClickListener(v -> salvarMeta());
    }

    private void salvarMeta() {
        String titulo = editTextTituloMeta.getText().toString().trim();
        String descricao = editTextDescricaoMeta.getText().toString().trim();

        // Validação simples para garantir que o título não está vazio
        if (TextUtils.isEmpty(titulo)) {
            Toast.makeText(this, "O título da meta é obrigatório.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Se não houver usuário logado, não podemos salvar
        if (user == null) {
            Toast.makeText(this, "Erro: Nenhum usuário logado.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Cria um objeto (Map) para ser salvo no Firestore
        Map<String, Object> meta = new HashMap<>();
        meta.put("titulo", titulo);
        meta.put("descricao", descricao);
        meta.put("concluida", false); // Toda nova meta começa como não concluída
        meta.put("userId", user.getUid()); // MUITO IMPORTANTE: Associa a meta ao usuário logado

        // Adiciona um novo documento à coleção 'metas'
        db.collection("metas")
                .add(meta)
                .addOnSuccessListener(documentReference -> {
                    Log.d(TAG, "Meta salva com ID: " + documentReference.getId());
                    Toast.makeText(NovaMetaActivity.this, "Meta salva com sucesso!", Toast.LENGTH_SHORT).show();
                    finish(); // Fecha a tela e volta para a lista de metas
                })
                .addOnFailureListener(e -> {
                    Log.w(TAG, "Erro ao salvar meta", e);
                    Toast.makeText(NovaMetaActivity.this, "Erro ao salvar meta.", Toast.LENGTH_SHORT).show();
                });
    }
}