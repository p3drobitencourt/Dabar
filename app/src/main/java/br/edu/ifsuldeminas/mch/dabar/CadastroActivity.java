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
import com.google.firebase.auth.UserProfileChangeRequest;

public class CadastroActivity extends AppCompatActivity {

    private TextInputEditText nomeEditText, emailEditText, senhaEditText, confirmarSenhaEditText;
    private Button btnCadastrar;
    private TextView linkLogin;
    private ImageButton btnVoltar;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);

        mAuth = FirebaseAuth.getInstance();

        nomeEditText = findViewById(R.id.nomeEditText);
        emailEditText = findViewById(R.id.emailEditText);
        senhaEditText = findViewById(R.id.senhaEditText);
        confirmarSenhaEditText = findViewById(R.id.confirmarSenhaEditText);
        btnCadastrar = findViewById(R.id.btn_cadastrar);
        linkLogin = findViewById(R.id.link_login);
        btnVoltar = findViewById(R.id.btn_voltar);

        btnVoltar.setOnClickListener(v -> finish());
        btnCadastrar.setOnClickListener(v -> validarEcadastrarUsuario());
        linkLogin.setOnClickListener(view -> {
            startActivity(new Intent(CadastroActivity.this, MainLoginActivity.class));
        });
    }

    private void validarEcadastrarUsuario() {
        String nome = nomeEditText.getText().toString().trim();
        String email = emailEditText.getText().toString().trim();
        String senha = senhaEditText.getText().toString().trim();
        String confirmarSenha = confirmarSenhaEditText.getText().toString().trim();

        if (nome.isEmpty() || email.isEmpty() || senha.isEmpty() || confirmarSenha.isEmpty()) {
            Toast.makeText(this, "Preencha todos os campos.", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailEditText.setError("Por favor, insira um email válido.");
            emailEditText.requestFocus();
            return;
        }
        if (senha.length() < 6) {
            senhaEditText.setError("A senha deve ter no mínimo 6 caracteres.");
            senhaEditText.requestFocus();
            return;
        }
        if (!senha.equals(confirmarSenha)) {
            confirmarSenhaEditText.setError("As senhas não coincidem.");
            confirmarSenhaEditText.requestFocus();
            return;
        }

        mAuth.createUserWithEmailAndPassword(email, senha)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder().setDisplayName(nome).build();
                        if (user != null) {
                            user.updateProfile(profileUpdates).addOnCompleteListener(profileTask -> {
                                if (profileTask.isSuccessful()) {
                                    startActivity(new Intent(CadastroActivity.this, ConfirmacaoCadastroActivity.class));
                                    finish();
                                }
                            });
                        }
                    } else {
                        String erro = task.getException() != null ? task.getException().getMessage() : "Erro desconhecido";
                        Toast.makeText(CadastroActivity.this, "Falha no cadastro: " + erro, Toast.LENGTH_LONG).show();
                    }
                });
    }
}