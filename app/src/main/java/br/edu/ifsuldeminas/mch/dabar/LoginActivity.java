package br.edu.ifsuldeminas.mch.dabar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private EditText editTextEmail, editTextSenha;
    private Button buttonLogin;
    private TextView textViewCadastro;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Associe esta classe ao seu layout
        setContentView(R.layout.activity_login_inicio);

        mAuth = FirebaseAuth.getInstance();

        // IMPORTANTE: Use os IDs exatos que você definiu no seu arquivo XML
        editTextEmail = findViewById(R.id.id_do_seu_campo_de_email);
        editTextSenha = findViewById(R.id.id_do_seu_campo_de_senha);
        buttonLogin = findViewById(R.id.id_do_seu_botao_de_login);
        textViewCadastro = findViewById(R.id.id_do_seu_texto_para_ir_ao_cadastro);

        buttonLogin.setOnClickListener(v -> loginUser());

        textViewCadastro.setOnClickListener(v ->
                startActivity(new Intent(LoginActivity.this, CadastroActivity.class))
        );
    }

    private void loginUser() {
        String email = editTextEmail.getText().toString().trim();
        String senha = editTextSenha.getText().toString().trim();

        if (email.isEmpty() || senha.isEmpty()) {
            Toast.makeText(this, "Preencha todos os campos.", Toast.LENGTH_SHORT).show();
            return;
        }

        mAuth.signInWithEmailAndPassword(email, senha)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        Log.d("LOGIN_SUCCESS", "signInWithEmail:success");
                        Toast.makeText(LoginActivity.this, "Login bem-sucedido!", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        // Limpa o histórico de telas para que o usuário não volte para o login
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    } else {
                        Log.w("LOGIN_FAILURE", "signInWithEmail:failure", task.getException());
                        Toast.makeText(LoginActivity.this, "Falha na autenticação.", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}