package br.edu.ifsuldeminas.mch.dabar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainLoginActivity extends AppCompatActivity {

    private TextInputEditText emailEditText, passwordEditText;
    private Button loginButton;
    private TextView signUpLink;
    private ImageButton backArrow;
    private FirebaseAuth mAuth;

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            abrirTelaPrincipal();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_login);

        mAuth = FirebaseAuth.getInstance();

        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        loginButton = findViewById(R.id.loginButton);
        signUpLink = findViewById(R.id.signUpLink);
        backArrow = findViewById(R.id.backArrow);

        backArrow.setOnClickListener(view -> finish());
        loginButton.setOnClickListener(view -> autenticarUsuario());
        signUpLink.setOnClickListener(view -> startActivity(new Intent(MainLoginActivity.this, CadastroActivity.class)));
    }

    private void autenticarUsuario() {
        String email = emailEditText.getText().toString().trim();
        String senha = passwordEditText.getText().toString().trim();

        if (email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailEditText.setError("Insira um email válido.");
            emailEditText.requestFocus();
            return;
        }

        if (senha.isEmpty()) {
            passwordEditText.setError("A senha é obrigatória.");
            passwordEditText.requestFocus();
            return;
        }

        mAuth.signInWithEmailAndPassword(email, senha)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        abrirTelaPrincipal();
                    } else {
                        String erro = task.getException() != null ? task.getException().getMessage() : "Erro desconhecido";
                        Toast.makeText(MainLoginActivity.this, "Falha na autenticação: " + erro, Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void abrirTelaPrincipal() {
        Intent intent = new Intent(MainLoginActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}