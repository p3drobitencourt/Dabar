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

    /**
     * Initializes the main activity, sets up the user interface, and configures button and bottom navigation listeners.
     *
     * @param savedInstanceState The previously saved state of the activity, or null if none exists.
     */
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

    /**
     * Sets up click listeners for the main screen buttons to launch their respective activities.
     *
     * Configures the "Gravar novo resumo", "Biblioteca de resumos", and "Guia do dabar" buttons to start
     * NovoResumoActivity, ListResumosActivity, and GuiaActivity, respectively, when clicked.
     */
    private void setupButtonListeners() {
        // Ação para o botão "Gravar novo resumo"
        btnGravarResumo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, NovoResumoActivity.class));
            }
        });

        // Ação para o botão "Biblioteca de resumos"
        btnBibliotecaResumos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, ListResumosActivity.class));
            }
        });

        // --- AQUI ESTÁ A CORREÇÃO ---
        // Ação para o botão "Guia do dabar"
        btnGuiaDabar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Inicia a GuiaActivity quando o botão for clicado
                Intent intent = new Intent(MainActivity.this, GuiaActivity.class);
                startActivity(intent);
            }
        });
    }

    /**
     * Configures the bottom navigation bar, setting the default selection and handling navigation item selections to launch the appropriate activities.
     */
    private void setupBottomNavigation() {
        // Define o item "Home" como selecionado ao iniciar
        bottomNavigation.setSelectedItemId(R.id.navigation_home);

        bottomNavigation.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.navigation_home) {
                // Já estamos na tela Home, não faz nada
                return true;
            } else if (itemId == R.id.navigation_library) {
                // Abre a tela correta da biblioteca de resumos
                startActivity(new Intent(this, BibliotecaActivity.class));
                return true;
            } else if (itemId == R.id.navigation_new_category) {
                // Abre a tela para criar uma nova categoria
                startActivity(new Intent(this, NovaCategoriaActivity.class));
                return true;
            }
            return false;
        });
    }
}