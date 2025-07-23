package br.edu.ifsuldeminas.mch.dabar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

/**
 * Controla a tela que lista todas as Metas de Estudo do usuário.
 * Responsável pelo 'R' (Read), 'U' (Update) e 'D' (Delete) do nosso CRUD com Firestore.
 */
public class MetasActivity extends BaseActivity {

    private static final String TAG = "MetasActivity";

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private MetasAdapter adapter;
    private FirebaseUser user;

    private ImageButton btnVoltar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_metas);

        user = FirebaseAuth.getInstance().getCurrentUser();

        setupToolbar(true);

        FloatingActionButton fabNovaMeta = findViewById(R.id.fab_nova_meta);
        fabNovaMeta.setOnClickListener(v -> startActivity(new Intent(MetasActivity.this, NovaMetaActivity.class)));

        // Configura a RecyclerView para exibir os dados do Firestore
        setUpRecyclerView();
    }

    private void setUpRecyclerView() {
        if (user == null) {
            Log.w(TAG, "Nenhum usuário logado para buscar metas.");
            // Opcional: Mostrar uma mensagem para o usuário
            return;
        }

        // Query para buscar apenas as metas associadas ao ID do usuário logado
        Query query = db.collection("metas")
                .whereEqualTo("userId", user.getUid())
                .orderBy("titulo", Query.Direction.ASCENDING);

        // Configura as opções para o FirestoreRecyclerAdapter
        FirestoreRecyclerOptions<Meta> options = new FirestoreRecyclerOptions.Builder<Meta>()
                .setQuery(query, Meta.class)
                .build();

        adapter = new MetasAdapter(options);

        RecyclerView recyclerView = findViewById(R.id.recyclerViewMetas);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        // Define os listeners para os eventos de clique nos itens da lista (checkbox e delete)
        adapter.setOnItemClickListener(new MetasAdapter.OnItemClickListener() {
            @Override
            public void onCheckboxClick(DocumentSnapshot documentSnapshot, boolean isChecked) {
                // 'U' -> Update: Atualiza o campo 'concluida' no documento do Firestore
                documentSnapshot.getReference().update("concluida", isChecked);
            }

            @Override
            public void onDeleteClick(DocumentSnapshot documentSnapshot) {
                // 'D' -> Delete: Deleta o documento do Firestore
                documentSnapshot.getReference().delete();
            }
        });
    }

    // O onStart e onStop são essenciais para que o FirestoreRecyclerAdapter
    // saiba quando começar e parar de "ouvir" por atualizações no banco de dados.
    @Override
    protected void onStart() {
        super.onStart();
        if (adapter != null) {
            adapter.startListening();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (adapter != null) {
            adapter.stopListening();
        }
    }
}