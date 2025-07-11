package br.edu.ifsuldeminas.mch.dabar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    private Button btnGravarResumo;
    private Button btnBibliotecaResumos;
    private Button btnGuiaDabar;
    private BottomNavigationView bottomNavigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Mapeia os componentes do layout
        btnGravarResumo = findViewById(R.id.btn_gravar_resumo);
        btnBibliotecaResumos = findViewById(R.id.btn_biblioteca_resumos);
        btnGuiaDabar = findViewById(R.id.btn_guia_dabar);
        bottomNavigation = findViewById(R.id.bottom_navigation);

        // Configura as ações de clique para cada botão
        setupButtonListeners();
        // Configura a navegação da barra inferior
        setupBottomNavigation();
    }

    private void setupButtonListeners() {
        // Ação para o botão "Gravar novo resumo"
        btnGravarResumo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Inicia a Activity para criar um novo resumo
                startActivity(new Intent(MainActivity.this, NovoResumoActivity.class));
            }
        });

        // Ação para o botão "Biblioteca de resumos"
        btnBibliotecaResumos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Inicia a Activity da Biblioteca
                startActivity(new Intent(MainActivity.this, BibliotecaActivity.class));
            }
        });

        // Ação para o botão "Guia do dabar"
        btnGuiaDabar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Ação para o guia (pode ser uma nova Activity ou um Dialog)
                startActivity(new Intent(MainActivity.this, GuiaActivity.class));
            }
        });
    }

    private void setupBottomNavigation() {
        // Define o item "Home" como selecionado ao iniciar
        bottomNavigation.setSelectedItemId(R.id.navigation_home);

        bottomNavigation.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.navigation_home) {
                // Já estamos na tela Home, não faz nada
                return true;
            } else if (itemId == R.id.navigation_library) {
                // Vai para a tela da Biblioteca
                startActivity(new Intent(this, BibliotecaActivity.class));
                // overridePendingTransition(0, 0); // Opcional: remove a animação de transição
                return true;
            } else if (itemId == R.id.navigation_new_category) {
                // Vai para a tela de Nova Categoria
                startActivity(new Intent(this, NovaCategoriaActivity.class));
                // overridePendingTransition(0, 0); // Opcional: remove a animação de transição
                return true;
            }
            return false;
        });
    }
}