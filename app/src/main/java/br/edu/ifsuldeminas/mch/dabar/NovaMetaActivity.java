package br.edu.ifsuldeminas.mch.dabar;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.Toast;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class NovaMetaActivity extends BaseActivity {
    private TextInputEditText editTextTituloMeta;
    private TextInputEditText editTextDescricaoMeta;
    private Button buttonSalvarMeta;
    private FirebaseFirestore db;
    private FirebaseUser user;

    // ✅ VARIÁVEL PARA SABER SE ESTAMOS EDITANDO
    private String idMetaAtual;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nova_meta);

        db = FirebaseFirestore.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();

        editTextTituloMeta = findViewById(R.id.editTextTituloMeta);
        editTextDescricaoMeta = findViewById(R.id.editTextDescricaoMeta);
        buttonSalvarMeta = findViewById(R.id.buttonSalvarMeta);

        // ✅ LÓGICA PARA VERIFICAR SE É CRIAÇÃO OU EDIÇÃO
        if (getIntent().hasExtra("ID_META")) {
            // Se tem um ID, estamos editando
            setTitle("Editar Meta");
            idMetaAtual = getIntent().getStringExtra("ID_META");
            editTextTituloMeta.setText(getIntent().getStringExtra("TITULO_META"));
            editTextDescricaoMeta.setText(getIntent().getStringExtra("DESCRICAO_META"));
        } else {
            // Se não, estamos criando
            setTitle("Nova Meta");
        }

        buttonSalvarMeta.setOnClickListener(v -> salvarMeta());
        setupToolbar(true);
        // setupBottomNavigationWithoutSelection();
    }

    private void salvarMeta() {
        String titulo = editTextTituloMeta.getText().toString().trim();
        String descricao = editTextDescricaoMeta.getText().toString().trim();

        if (TextUtils.isEmpty(titulo)) {
            Toast.makeText(this, "O título da meta é obrigatório.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (user == null) {
            Toast.makeText(this, "Erro: Nenhum usuário logado.", Toast.LENGTH_SHORT).show();
            return;
        }

        // ✅ LÓGICA DE SALVAR ATUALIZADA
        if (idMetaAtual != null) {
            // MODO EDIÇÃO: Atualiza o documento existente
            db.collection("metas").document(idMetaAtual)
                    .update("titulo", titulo, "descricao", descricao)
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(NovaMetaActivity.this, "Meta atualizada!", Toast.LENGTH_SHORT).show();
                        finish();
                    })
                    .addOnFailureListener(e -> Toast.makeText(NovaMetaActivity.this, "Erro ao atualizar.", Toast.LENGTH_SHORT).show());
        } else {
            // MODO CRIAÇÃO: Cria um novo documento
            Meta meta = new Meta();
            meta.setTitulo(titulo);
            meta.setDescricao(descricao);
            meta.setConcluida(false);
            meta.setUserId(user.getUid());

            db.collection("metas").add(meta)
                    .addOnSuccessListener(documentReference -> {
                        Toast.makeText(NovaMetaActivity.this, "Meta salva!", Toast.LENGTH_SHORT).show();
                        finish();
                    })
                    .addOnFailureListener(e -> Toast.makeText(NovaMetaActivity.this, "Erro ao salvar.", Toast.LENGTH_SHORT).show());
        }
    }
}