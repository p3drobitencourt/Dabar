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
import com.google.firebase.auth.UserProfileChangeRequest;

public class CadastroActivity extends AppCompatActivity {

    // Componentes da UI
    private TextInputEditText nomeEditText, emailEditText, senhaEditText, confirmarSenhaEditText;
    private Button btnCadastrar;
    private TextView linkLogin;
    // private ProgressBar progressBar; // Descomente se adicionar uma ProgressBar

    // Firebase
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);

        // Inicializa o Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        // Referencia os componentes da UI a partir do XML
        nomeEditText = findViewById(R.id.nomeEditText);
        emailEditText = findViewById(R.id.emailEditText);
        senhaEditText = findViewById(R.id.senhaEditText);
        confirmarSenhaEditText = findViewById(R.id.confirmarSenhaEditText);
        btnCadastrar = findViewById(R.id.btn_cadastrar);
        linkLogin = findViewById(R.id.link_login);
        // progressBar = findViewById(R.id.sua_progressbar_id); // Descomente e ajuste o ID

        btnCadastrar.setOnClickListener(v -> {
            // TODO: Quando a tela de login principal estiver pronta, mude para LoginActivity.class
            startActivity(new Intent(CadastroActivity.this, ConfirmacaoCadastroActivity.class));
        });

        // Configura o clique do link para ir para a tela de login
        linkLogin.setOnClickListener(view -> {
            // Cria uma Intent para abrir a MainLoginActivity
            Intent intent = new Intent(CadastroActivity.this, MainLoginActivity.class);
            startActivity(intent);
            finish(); // Opcional: fecha a tela de cadastro
        });
    }

    private void validarEcadastrarUsuario() {
        String nome = nomeEditText.getText().toString().trim();
        String email = emailEditText.getText().toString().trim();
        String senha = senhaEditText.getText().toString().trim();
        String confirmarSenha = confirmarSenhaEditText.getText().toString().trim();

        // Validações dos campos
        if (nome.isEmpty()) {
            nomeEditText.setError("O nome é obrigatório.");
            nomeEditText.requestFocus();
            return;
        }

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
            senhaEditText.setError("A senha é obrigatória.");
            senhaEditText.requestFocus();
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

        // Se todas as validações passaram, cria o usuário
        // btnCadastrar.setVisibility(View.GONE); // Opcional: esconde o botão
        // progressBar.setVisibility(View.VISIBLE); // Opcional: mostra o progresso

        mAuth.createUserWithEmailAndPassword(email, senha)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        // Adiciona o nome do usuário ao perfil dele no Firebase
                        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                .setDisplayName(nome)
                                .build();

                        if (user != null) {
                            user.updateProfile(profileUpdates).addOnCompleteListener(profileTask -> {
                                if (profileTask.isSuccessful()) {
                                    Toast.makeText(CadastroActivity.this, "Usuário cadastrado com sucesso!", Toast.LENGTH_LONG).show();
                                    // Redireciona para a tela de login ou principal
                                    finish();
                                }
                            });
                        }
                    } else {
                        // Trata os erros mais comuns
                        String erro = task.getException() != null ? task.getException().getMessage() : "Erro desconhecido";
                        Toast.makeText(CadastroActivity.this, "Falha no cadastro: " + erro, Toast.LENGTH_LONG).show();
                    }
                    // progressBar.setVisibility(View.GONE); // Opcional: esconde o progresso
                    // btnCadastrar.setVisibility(View.VISIBLE); // Opcional: mostra o botão novamente
                });
    }
}