package br.edu.ifsuldeminas.mch.dabar;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

/**
 * Controla a tela inicial de boas-vindas (activity_login_inicio.xml).
 * Oferece as opções de "Entrar" ou "Cadastrar-me".
 */
public class LoginInicioActivity extends AppCompatActivity {

    private Button enterButton;
    private Button registerButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Associa esta classe ao layout XML correto.
        setContentView(R.layout.activity_login_inicio);

        // Conecta as variáveis do Java com os componentes do XML usando os IDs.
        enterButton = findViewById(R.id.enterButton);
        registerButton = findViewById(R.id.registerButton);

        // Define a ação para o botão "Entrar".
        // Ele levará para uma tela de Login que você ainda irá criar (usando activity_main_login.xml).
        // Por enquanto, vamos apontar para a MainActivity para teste.
        enterButton.setOnClickListener(v -> {
            // TODO: Quando a tela de login principal estiver pronta, mude para LoginActivity.class
            startActivity(new Intent(LoginInicioActivity.this, MainLoginActivity.class));
        });

        // Define a ação para o botão "Cadastrar-me".
        // Ele levará para a tela de Cadastro.
        registerButton.setOnClickListener(v -> {
            startActivity(new Intent(LoginInicioActivity.this, CadastroActivity.class));
        });
    }
}