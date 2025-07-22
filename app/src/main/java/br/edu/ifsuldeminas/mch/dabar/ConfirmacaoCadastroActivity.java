package br.edu.ifsuldeminas.mch.dabar;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class ConfirmacaoCadastroActivity extends AppCompatActivity {

    private Button btnFazerLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirmacao_cadastro);

        // Conecta a variável Java com o ID do seu XML.
        btnFazerLogin = findViewById(R.id.btn_fazer_login);

        btnFazerLogin.setOnClickListener(v -> {
            // Envia o usuário de volta para a tela de login inicial.
            Intent intent = new Intent(ConfirmacaoCadastroActivity.this, LoginInicioActivity.class);
            // Limpa o histórico de telas para que o usuário não volte para a confirmação.
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        });
    }
}