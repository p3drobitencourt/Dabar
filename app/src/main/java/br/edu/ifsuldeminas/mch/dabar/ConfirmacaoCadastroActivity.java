package br.edu.ifsuldeminas.mch.dabar;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class ConfirmacaoCadastroActivity extends AppCompatActivity {

    private Button buttonVoltarLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirmacao_cadastro);

        // IMPORTANTE: Use o ID do seu botão no activity_confirmacao_cadastro.xml
        buttonVoltarLogin = findViewById(R.id.id_do_seu_botao_de_voltar);

        buttonVoltarLogin.setOnClickListener(v -> {
            startActivity(new Intent(ConfirmacaoCadastroActivity.this, LoginActivity.class));
            finish();
        });
    }
}