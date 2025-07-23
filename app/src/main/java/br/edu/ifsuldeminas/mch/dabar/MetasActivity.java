package br.edu.ifsuldeminas.mch.dabar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class MetasActivity extends BaseActivity {
    // ... suas variáveis ...
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private MetasAdapter adapter;
    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_metas);

        user = FirebaseAuth.getInstance().getCurrentUser();
        setupToolbar(true);

        FloatingActionButton fabNovaMeta = findViewById(R.id.fab_nova_meta);
        fabNovaMeta.setOnClickListener(v -> startActivity(new Intent(MetasActivity.this, NovaMetaActivity.class)));

        setUpRecyclerView();
    }

    private void setUpRecyclerView() {
        // ... sua query está perfeita ...
        Query query = db.collection("metas")
                .whereEqualTo("userId", user.getUid())
                .orderBy("titulo", Query.Direction.ASCENDING);

        FirestoreRecyclerOptions<Meta> options = new FirestoreRecyclerOptions.Builder<Meta>()
                .setQuery(query, Meta.class)
                .build();

        adapter = new MetasAdapter(options);
        // ... configuração do RecyclerView ...
        RecyclerView recyclerView = findViewById(R.id.recyclerViewMetas);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        // ✅ LÓGICA COMPLETA DOS LISTENERS
        adapter.setOnItemClickListener(new MetasAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(DocumentSnapshot documentSnapshot) {
                // 'U' -> Update (Parte 1): Abre a tela de edição
                Meta meta = documentSnapshot.toObject(Meta.class);
                String id = documentSnapshot.getId();

                Intent intent = new Intent(MetasActivity.this, NovaMetaActivity.class);
                // Envia os dados da meta para a próxima tela
                intent.putExtra("ID_META", id);
                intent.putExtra("TITULO_META", meta.getTitulo());
                intent.putExtra("DESCRICAO_META", meta.getDescricao());
                startActivity(intent);
            }

            @Override
            public void onCheckboxClick(DocumentSnapshot documentSnapshot, boolean isChecked) {
                documentSnapshot.getReference().update("concluida", isChecked);
            }

            @Override
            public void onDeleteClick(DocumentSnapshot documentSnapshot) {
                documentSnapshot.getReference().delete();
            }
        });
    }

    // ... onStart e onStop continuam perfeitos ...
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