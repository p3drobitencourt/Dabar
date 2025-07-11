package br.edu.ifsuldeminas.mch.dabar;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class GuiaActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guia);

        // --- INÍCIO DA CORREÇÃO ---

        // 1. Conecta a variável ao componente do layout
        // (Supondo que no seu 'activity_guia.xml' a barra de navegação tenha o id 'bottom_navigation')
        bottomNavigation = findViewById(R.id.bottom_navigation);

        // 2. Chama o método para fazer a mágica acontecer
        setupBottomNavigation();

        // --- FIM DA CORREÇÃO ---
    }

    private void setupBottomNavigation() {
        // Agora esta linha não dará erro, pois 'bottomNavigation' foi inicializada
        bottomNavigation.setSelectedItemId(R.id.navigation_home);

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