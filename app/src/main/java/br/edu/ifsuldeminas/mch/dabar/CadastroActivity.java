package br.edu.ifsuldeminas.mch.dabar;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class CadastroActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private TextInputEditText nomeEditText, emailEditText, senhaEditText, confirmarSenhaEditText;
    private Button btnCadastrar;
    private ImageButton btnVoltar;
    private TextView linkLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);

        mAuth = FirebaseAuth.getInstance();

        // Conecta as variáveis Java com os IDs do seu XML.
        nomeEditText = findViewById(R.id.nomeEditText);
        emailEditText = findViewById(R.id.emailEditText);
        senhaEditText = findViewById(R.id.senhaEditText);
        confirmarSenhaEditText = findViewById(R.id.confirmarSenhaEditText);
        btnCadastrar = findViewById(R.id.btn_cadastrar);
        btnVoltar = findViewById(R.id.btn_voltar);
        linkLogin = findViewById(R.id.link_login);

        // Define as ações para cada botão/link.
        btnCadastrar.setOnClickListener(v -> cadastrarUsuario());
        btnVoltar.setOnClickListener(v -> finish()); // 'finish()' simplesmente fecha a tela atual.
        linkLogin.setOnClickListener(v -> {
            // TODO: Mudar para a tela de Login principal quando estiver pronta.
            startActivity(new Intent(CadastroActivity.this, MainActivity.class));
        });
    }

    private void cadastrarUsuario() {
        String nome = nomeEditText.getText().toString().trim();
        String email = emailEditText.getText().toString().trim();
        String senha = senhaEditText.getText().toString().trim();
        String confirmarSenha = confirmarSenhaEditText.getText().toString().trim();

        // --- VALIDAÇÕES ---
        if (TextUtils.isEmpty(nome) || TextUtils.isEmpty(email) || TextUtils.isEmpty(senha)) {
            Toast.makeText(this, "Por favor, preencha todos os campos.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!senha.equals(confirmarSenha)) {
            Toast.makeText(this, "As senhas não coincidem.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (senha.length() < 6) {
            Toast.makeText(this, "A senha deve ter no mínimo 6 caracteres.", Toast.LENGTH_SHORT).show();
            return;
        }

        // --- CRIAÇÃO DO USUÁRIO NO FIREBASE ---
        mAuth.createUserWithEmailAndPassword(email, senha)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        Log.d("CADASTRO_SUCCESS", "createUserWithEmail:success");
                        FirebaseUser user = mAuth.getCurrentUser();
                        // Adiciona o nome do usuário ao perfil do Firebase
                        if (user != null) {
                            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                    .setDisplayName(nome)
                                    .build();
                            user.updateProfile(profileUpdates);
                        }
                        // Envia para a tela de confirmação
                        startActivity(new Intent(CadastroActivity.this, ConfirmacaoCadastroActivity.class));
                        finish();
                    } else {
                        Log.w("CADASTRO_FAILURE", "createUserWithEmail:failure", task.getException());
                        Toast.makeText(CadastroActivity.this, "Falha no cadastro: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }
}