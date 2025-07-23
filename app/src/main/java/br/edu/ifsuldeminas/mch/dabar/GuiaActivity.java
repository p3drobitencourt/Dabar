package br.edu.ifsuldeminas.mch.dabar;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem; // Importação adicionada
import androidx.annotation.NonNull; // Importação adicionada
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar; // Importação adicionada
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class GuiaActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guia);

        // --- LÓGICA DA TOOLBAR COM BOTÃO VOLTAR ---
        Toolbar toolbar = findViewById(R.id.app_bar); // Use o ID da sua toolbar
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true); // Mostra a seta de voltar
            getSupportActionBar().setDisplayShowTitleEnabled(false); // Esconde o título do app
        }
        // --- FIM DA LÓGICA DA TOOLBAR ---

        bottomNavigation = findViewById(R.id.bottom_navigation);
        setupBottomNavigation();
    }

    // --- MÉTODO PARA LIDAR COM O CLIQUE NA SETA DA TOOLBAR ---
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish(); // Fecha a activity
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setupBottomNavigation() {
        bottomNavigation.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.navigation_home) {
                startActivity(new Intent(this, MainActivity.class));
                return true;
            } else if (itemId == R.id.navigation_library) {
                startActivity(new Intent(this, ListResumosActivity.class));
                return true;
            } else if (itemId == R.id.navigation_new_category) {
                startActivity(new Intent(this, NovaCategoriaActivity.class));
                return true;
            }
            return false;
        });
    }
}