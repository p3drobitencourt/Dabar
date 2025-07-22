package br.edu.ifsuldeminas.mch.dabar;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * Activity de "Splash" ou "Carregamento".
 * Sua única responsabilidade é verificar se o usuário está logado
 * e redirecioná-lo para a tela correta. Ela não tem layout visual.
 */
public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Verifica o usuário logado no Firebase
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

        // Se não houver usuário logado, vá para a tela de login.
        if (currentUser == null) {
            startActivity(new Intent(this, LoginInicioActivity.class));
        } else {
            // Se houver um usuário logado, vá para a tela principal.
            startActivity(new Intent(this, MainActivity.class));
        }

        // Finaliza esta activity para que o usuário não possa voltar para ela
        finish();
    }
}