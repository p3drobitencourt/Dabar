package br.edu.ifsuldeminas.mch.dabar;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton; // Importação adicionada
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import br.edu.ifsuldeminas.mch.dabar.network.ApiService;
import br.edu.ifsuldeminas.mch.dabar.network.RetrofitClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CitacaoDoDiaActivity extends AppCompatActivity {

    private TextView textViewCitacao, textViewAutor;
    private ProgressBar progressBar;
    private Button buttonNovaCitacao;
    private ImageButton btnVoltar; // Variável adicionada
    private ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_citacao_do_dia);

        // --- BOTÃO VOLTAR ADICIONADO ---
        btnVoltar = findViewById(R.id.btn_voltar_citacao);
        btnVoltar.setOnClickListener(v -> finish());
        // --- FIM DA LÓGICA DO BOTÃO ---

        textViewCitacao = findViewById(R.id.textViewCitacao);
        textViewAutor = findViewById(R.id.textViewAutor);
        progressBar = findViewById(R.id.progressBar);
        buttonNovaCitacao = findViewById(R.id.buttonNovaCitacao);

        apiService = RetrofitClient.getClient().create(ApiService.class);

        buttonNovaCitacao.setOnClickListener(v -> buscarCitacao());
        buscarCitacao();
    }
    private void buscarCitacao() {
        // Mostra a barra de progresso e esconde o texto antes de fazer a chamada
        progressBar.setVisibility(View.VISIBLE);
        textViewCitacao.setVisibility(View.GONE);
        textViewAutor.setVisibility(View.GONE);

        // Faz a chamada assíncrona para a API
        apiService.getCitacaoAleatoria().enqueue(new Callback<Citacao>() {
            @Override
            public void onResponse(@NonNull Call<Citacao> call, @NonNull Response<Citacao> response) {
                // Esconde a barra de progresso
                progressBar.setVisibility(View.GONE);

                if (response.isSuccessful() && response.body() != null) {
                    // Se a resposta for bem-sucedida, pega o objeto Citacao
                    Citacao citacao = response.body();
                    // Atualiza os TextViews com os dados recebidos
                    textViewCitacao.setText(String.format("\"%s\"", citacao.getTexto()));
                    textViewAutor.setText(String.format("- %s", citacao.getAutor()));
                    // Mostra os textos
                    textViewCitacao.setVisibility(View.VISIBLE);
                    textViewAutor.setVisibility(View.VISIBLE);
                } else {
                    // Se houver um erro na resposta do servidor
                    Toast.makeText(CitacaoDoDiaActivity.this, "Falha ao buscar citação.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<Citacao> call, @NonNull Throwable t) {
                // Esconde a barra de progresso
                progressBar.setVisibility(View.GONE);
                // Se houver uma falha de rede (ex: sem internet)
                Toast.makeText(CitacaoDoDiaActivity.this, "Erro de rede: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}