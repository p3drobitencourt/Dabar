package br.edu.ifsuldeminas.mch.dabar;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ProgressBar; // Opcional: Adicione uma ProgressBar ao seu XML para feedback

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainLoginActivity extends AppCompatActivity {

    // Componentes da UI
    private TextInputEditText emailEditText, passwordEditText;
    private Button loginButton;
    private TextView signUpLink;
    // private ProgressBar progressBar; // Descomente se adicionar uma ProgressBar

    // Firebase
    private FirebaseAuth mAuth;

    @Override
    protected void onStart() {
        super.onStart();
        // Verifica se o usuário já está logado. Se sim, redireciona para a tela principal.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            abrirTelaPrincipal();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_login);

        // Inicializa o Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        // Referencia os componentes da UI a partir do XML
        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        loginButton = findViewById(R.id.loginButton);
        signUpLink = findViewById(R.id.signUpLink);
        // progressBar = findViewById(R.id.sua_outra_progressbar_id); // Descomente e ajuste o ID

        // Configura o clique do botão de login
        loginButton.setOnClickListener(view -> {
            autenticarUsuario();
        });

        // Configura o clique do link para ir para a tela de cadastro
        signUpLink.setOnClickListener(view -> {
            Intent intent = new Intent(MainLoginActivity.this, CadastroActivity.class);
            startActivity(intent);
        });
    }

    private void autenticarUsuario() {
        String email = emailEditText.getText().toString().trim();
        String senha = passwordEditText.getText().toString().trim();

        // Validações
        if (email.isEmpty()) {
            emailEditText.setError("O email é obrigatório.");
            emailEditText.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailEditText.setError("Por favor, insira um email válido.");
            emailEditText.requestFocus();
            return;
        }

        if (senha.isEmpty()) {
            passwordEditText.setError("A senha é obrigatória.");
            passwordEditText.requestFocus();
            return;
        }

        // loginButton.setVisibility(View.GONE); // Opcional
        // progressBar.setVisibility(View.VISIBLE); // Opcional

        mAuth.signInWithEmailAndPassword(email, senha)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Login bem-sucedido, redireciona para a tela principal
                        abrirTelaPrincipal();
                    } else {
                        // Se o login falhar, exibe uma mensagem ao usuário.
                        String erro = task.getException() != null ? task.getException().getMessage() : "Erro desconhecido";
                        Toast.makeText(MainLoginActivity.this, "Falha na autenticação: " + erro, Toast.LENGTH_LONG).show();
                    }
                    // progressBar.setVisibility(View.GONE); // Opcional
                    // loginButton.setVisibility(View.VISIBLE); // Opcional
                });
    }

    private void abrirTelaPrincipal() {
        // Altere "SuaTelaPrincipalActivity.class" para o nome da sua Activity principal
        Intent intent = new Intent(MainLoginActivity.this, MainActivity.class);
        // Essas flags limpam a pilha de atividades, impedindo o usuário de voltar para a tela de login
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}