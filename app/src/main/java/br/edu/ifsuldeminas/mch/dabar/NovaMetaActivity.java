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

public class NovaMetaActivity extends BaseActivity {

    private static final String TAG = "NovaMetaActivity_DEBUG"; // Tag para facilitar a busca no Logcat

    private TextInputEditText editTextTituloMeta;
    private TextInputEditText editTextDescricaoMeta;
    private Button buttonSalvarMeta;

    private FirebaseFirestore db;
    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nova_meta);

        db = FirebaseFirestore.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();

        editTextTituloMeta = findViewById(R.id.editTextTituloMeta);
        editTextDescricaoMeta = findViewById(R.id.editTextDescricaoMeta);
        buttonSalvarMeta = findViewById(R.id.buttonSalvarMeta);

        buttonSalvarMeta.setOnClickListener(v -> salvarMeta());

        setupToolbar(true);
        setupBottomNavigationWithoutSelection();
    }

    private void salvarMeta() {
        // --- MENSAGEM DE DIAGNÓSTICO 1 ---
        // Veremos se o clique no botão está funcionando.
        Log.d(TAG, "Método salvarMeta() foi chamado.");
        Toast.makeText(this, "Tentando salvar...", Toast.LENGTH_SHORT).show();

        String titulo = editTextTituloMeta.getText().toString().trim();
        String descricao = editTextDescricaoMeta.getText().toString().trim();

        if (TextUtils.isEmpty(titulo)) {
            Toast.makeText(this, "O título da meta é obrigatório.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (user == null) {
            Toast.makeText(this, "Erro: Nenhum usuário logado.", Toast.LENGTH_SHORT).show();
            Log.e(TAG, "Tentativa de salvar sem usuário logado.");
            return;
        }

        Map<String, Object> meta = new HashMap<>();
        meta.put("titulo", titulo);
        meta.put("descricao", descricao);
        meta.put("concluida", false);
        meta.put("userId", user.getUid());

        Log.d(TAG, "Iniciando a escrita no Firestore...");
        db.collection("metas")
                .add(meta)
                .addOnSuccessListener(documentReference -> {
                    // --- MENSAGEM DE SUCESSO ---
                    Log.d(TAG, "SUCESSO! Meta salva com ID: " + documentReference.getId());
                    Toast.makeText(NovaMetaActivity.this, "Meta salva com sucesso!", Toast.LENGTH_LONG).show();
                    finish();
                })
                .addOnFailureListener(e -> {
                    // --- MENSAGEM DE FALHA DETALHADA ---
                    Log.e(TAG, "FALHA ao salvar meta. Erro: ", e); // Loga o erro completo
                    Toast.makeText(NovaMetaActivity.this, "ERRO ao salvar: " + e.getMessage(), Toast.LENGTH_LONG).show();
                });
    }
}