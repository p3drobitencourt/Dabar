package br.edu.ifsuldeminas.mch.dabar;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import androidx.appcompat.app.AppCompatActivity;

// Importações do Firebase
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // setContentView(R.layout.activity_splash); // Você pode ter um layout aqui

        // ===================================================================
        // ✅ PASSO CRÍTICO: INICIALIZAÇÃO DO FIREBASE
        // Esta linha "liga" o Firebase para todo o aplicativo.
        // Ela deve ser a primeira coisa a ser executada no seu app.
        FirebaseApp.initializeApp(this);
        // ===================================================================

        // Atraso de 2 segundos para mostrar a tela de splash
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            // Agora, com o Firebase inicializado, podemos usá-lo com segurança.
            FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

            if (currentUser != null) {
                // Se o usuário já está logado, vai para a tela principal.
                startActivity(new Intent(SplashActivity.this, MainActivity.class));
            } else {
                // Se não está logado, vai para a tela de login.
                startActivity(new Intent(SplashActivity.this, LoginInicioActivity.class));
            }
            // Fecha a SplashActivity para que o usuário não possa voltar para ela.
            finish();
        }, 2000); // 2 segundos
    }
}