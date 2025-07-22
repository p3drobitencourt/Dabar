package br.edu.ifsuldeminas.mch.dabar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

// --- IMPORTAÇÕES CORRIGIDAS ---
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import br.edu.ifsuldeminas.mch.dabar.Meta; // <-- ESTA LINHA FOI ADICIONADA

public class MetasActivity extends AppCompatActivity {

    private static final String TAG = "MetasActivity";

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private MetasAdapter adapter;
    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_metas);

        user = FirebaseAuth.getInstance().getCurrentUser();

        Toolbar toolbar = findViewById(R.id.topAppBar);
        toolbar.setNavigationOnClickListener(v -> finish());

        FloatingActionButton fabNovaMeta = findViewById(R.id.fab_nova_meta);
        fabNovaMeta.setOnClickListener(v -> startActivity(new Intent(MetasActivity.this, NovaMetaActivity.class)));

        setUpRecyclerView();
    }

    private void setUpRecyclerView() {
        if (user == null) {
            Log.w(TAG, "Nenhum usuário logado para buscar metas.");
            return;
        }

        Query query = db.collection("metas")
                .whereEqualTo("userId", user.getUid())
                .orderBy("titulo", Query.Direction.ASCENDING);

        FirestoreRecyclerOptions<Meta> options = new FirestoreRecyclerOptions.Builder<Meta>()
                .setQuery(query, Meta.class)
                .build();

        adapter = new MetasAdapter(options);

        RecyclerView recyclerView = findViewById(R.id.recyclerViewMetas);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(new MetasAdapter.OnItemClickListener() {
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