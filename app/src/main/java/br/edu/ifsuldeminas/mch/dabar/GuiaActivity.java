package br.edu.ifsuldeminas.mch.dabar;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem; // Importação necessária
import androidx.annotation.NonNull; // Importação necessária
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar; // Importação necessária
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class GuiaActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guia);

        // --- CÓDIGO DA TOOLBAR COM SETA DE VOLTAR ---
        // 1. Conectamos a Toolbar do XML. Use o ID que seu amigo criou.
        Toolbar toolbar = findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);

        // 2. Pedimos para o Android mostrar a seta de voltar ("Home as Up").
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false); // Mantém o título do app escondido
        }
        // --- FIM DA LÓGICA DA TOOLBAR ---

        bottomNavigation = findViewById(R.id.bottom_navigation);
        setupBottomNavigation();
    }

    // --- CÓDIGO PARA FAZER A SETA FUNCIONAR ---
    // 3. Este método é chamado quando qualquer item do menu (incluindo a seta) é clicado.
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        // Verificamos se o item clicado é o botão "home" (a seta de voltar).
        if (item.getItemId() == android.R.id.home) {
            finish(); // O comando 'finish()' fecha a tela atual, voltando para a anterior.
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    // --- FIM DO CÓDIGO DA SETA ---

    // LÓGICA ORIGINAL 100% PRESERVADA
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