package br.edu.ifsuldeminas.mch.dabar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class CadastroActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private EditText editTextEmail, editTextSenha;
    private Button buttonCadastrar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);

        mAuth = FirebaseAuth.getInstance();

        // IMPORTANTE: Use os IDs do seu arquivo activity_cadastro.xml
        editTextEmail = findViewById(R.id.id_do_seu_campo_de_email_no_cadastro);
        editTextSenha = findViewById(R.id.id_do_seu_campo_de_senha_no_cadastro);
        buttonCadastrar = findViewById(R.id.id_do_seu_botao_de_cadastrar);

        buttonCadastrar.setOnClickListener(v -> cadastrarUsuario());
    }

    private void cadastrarUsuario() {
        String email = editTextEmail.getText().toString().trim();
        String senha = editTextSenha.getText().toString().trim();

        if (email.isEmpty() || senha.isEmpty()) {
            Toast.makeText(this, "Preencha todos os campos.", Toast.LENGTH_SHORT).show();
            return;
        }

        mAuth.createUserWithEmailAndPassword(email, senha)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        Log.d("CADASTRO_SUCCESS", "createUserWithEmail:success");
                        // Envia para a tela de confirmação
                        startActivity(new Intent(CadastroActivity.this, ConfirmacaoCadastroActivity.class));
                        finish();
                    } else {
                        Log.w("CADASTRO_FAILURE", "createUserWithEmail:failure", task.getException());
                        Toast.makeText(CadastroActivity.this, "Falha no cadastro. Verifique os dados.", Toast.LENGTH_LONG).show();
                    }
                });
    }
}