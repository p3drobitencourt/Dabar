// src/main/java/br/edu/ifsuldeminas/mch/dabar/BaseActivity.java

package br.edu.ifsuldeminas.mch.dabar;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

public abstract class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /**
     * Configura a Toolbar padrão para a Activity.
     * @param showBackButton True se a seta de voltar deve ser exibida, false caso contrário.
     */
    protected void setupToolbar(boolean showBackButton) {
        Toolbar toolbar = findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            if (getSupportActionBar() != null) {
                getSupportActionBar().setDisplayShowTitleEnabled(false);
                getSupportActionBar().setDisplayHomeAsUpEnabled(showBackButton);
            }
        }
    }

    /**
     * Configura o BottomNavigationView padrão para a Activity.
     * @param selectedItemId O ID do item de menu que deve aparecer como selecionado.
     */
    protected void setupBottomNavigation(int selectedItemId) {
        BottomNavigationView bottomNavigation = findViewById(R.id.bottom_navigation);

        // Evita que o listener seja acionado ao definirmos o item selecionado
        if (bottomNavigation.getSelectedItemId() != selectedItemId) {
            bottomNavigation.setSelectedItemId(selectedItemId);
        }

        bottomNavigation.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();

            // Evita recarregar a tela se o item selecionado for o atual
            if (itemId == getApplicationInfo().labelRes) { // Uma forma de checar a tela atual
                return true;
            }

            if (itemId == R.id.navigation_home) {
                startActivity(new Intent(this, MainActivity.class));
                return true;
                // Supondo que você tenha um id "navigation_new_summary" no seu menu
            } else if (itemId == R.id.navigation_new_resume) {
                startActivity(new Intent(this, NovoResumoActivity.class));
                return true;
            } else if (itemId == R.id.navigation_new_category) {
                startActivity(new Intent(this, NovaCategoriaActivity.class));
                return true;
                // Supondo que o id para a lista de categorias seja "navigation_library"
            } else if (itemId == R.id.navigation_library) {
                startActivity(new Intent(this, BibliotecaActivity.class));
                return true;
            }

            return false;
        });
    }

    protected void setupBottomNavigationWithoutSelection() {
        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        if (bottomNav != null) {
            // A GRANDE DIFERENÇA: Não chamamos bottomNav.setSelectedItemId() aqui.

            // A lógica de clique e navegação continua a mesma.
            bottomNav.setOnItemSelectedListener(item -> {
                int itemId = item.getItemId();

                // A navegação funciona normalmente quando o usuário clica em um ícone
                if (itemId == R.id.navigation_home) {
                    startActivity(new Intent(this, MainActivity.class));
                    return true;
                } else if (itemId == R.id.navigation_library) {
                    startActivity(new Intent(this, BibliotecaActivity.class));
                    return true;
                } else if (itemId == R.id.navigation_new_resume) {
                    startActivity(new Intent(this, NovoResumoActivity.class));
                    return true;
                }
                return false;
            });
        }
    }

    // Este método infla o menu superior (três pontinhos) em todas as telas
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    // Este método centraliza a lógica de clique para TODOS os itens de menu
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();

        // Lógica para a SETA DE VOLTAR
        if (itemId == android.R.id.home) {
            finish(); // Fecha a activity atual e volta para a anterior
            return true;
        }
        // Lógica para o menu de LOGOUT
        else if (itemId == R.id.action_logout) {
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(this, LoginInicioActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            return true;
        }
        // Lógica para os outros itens do menu
        else if (itemId == R.id.action_metas) {
            startActivity(new Intent(this, MetasActivity.class));
            return true;
        } else if (itemId == R.id.action_dica) {
            startActivity(new Intent(this, CitacaoDoDiaActivity.class));
            return true;
        } else if (itemId == R.id.action_guia) {
            startActivity(new Intent(this, GuiaActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}