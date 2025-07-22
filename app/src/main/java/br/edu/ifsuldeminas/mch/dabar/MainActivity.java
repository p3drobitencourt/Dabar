package br.edu.ifsuldeminas.mch.dabar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    private Button btnGravarResumo;
    private Button btnBibliotecaResumos;
    private Button btnGuiaDabar;
    private Button btnDicaEstudo; // Adicionado aqui
    private BottomNavigationView bottomNavigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Mapeia os componentes do layouta
        btnGravarResumo = findViewById(R.id.btn_gravar_resumo);
        btnBibliotecaResumos = findViewById(R.id.btn_biblioteca_resumos);
        btnGuiaDabar = findViewById(R.id.btn_guia_dabar);
        btnDicaEstudo = findViewById(R.id.btn_dica_estudo); // Adicionado aqui
        bottomNavigation = findViewById(R.id.bottom_navigation);

        setupButtonListeners();
        setupBottomNavigation();
    }

    private void setupButtonListeners() {
        btnGravarResumo.setOnClickListener(v ->
                startActivity(new Intent(MainActivity.this, NovoResumoActivity.class)));

        btnBibliotecaResumos.setOnClickListener(v ->
                startActivity(new Intent(MainActivity.this, ListResumosActivity.class)));

        btnGuiaDabar.setOnClickListener(v ->
                startActivity(new Intent(MainActivity.this, GuiaActivity.class)));

        // --- AÇÃO PARA O NOVO BOTÃO ---
        btnDicaEstudo.setOnClickListener(v ->
                startActivity(new Intent(MainActivity.this, CitacaoDoDiaActivity.class)));
    }

    private void setupBottomNavigation() {
        bottomNavigation.setSelectedItemId(R.id.navigation_home);

        bottomNavigation.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.navigation_home) {
                return true;
            } else if (itemId == R.id.navigation_library) {
                startActivity(new Intent(this, BibliotecaActivity.class));
                return true;
            } else if (itemId == R.id.navigation_new_category) {
                startActivity(new Intent(this, NovaCategoriaActivity.class));
                return true;
            }
            return false;
        });
    }
}