package br.edu.ifsuldeminas.mch.dabar;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar; // Import para a Toolbar

import br.edu.ifsuldeminas.mch.dabar.network.RetrofitClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CitacaoDoDiaActivity extends AppCompatActivity {

    private TextView textViewCitacao;
    private TextView textViewAutor;
    private Button btnBuscarCitacao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_citacao_do_dia);

        // Configura a Toolbar
        Toolbar toolbar = findViewById(R.id.topAppBar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        // Mapeia os componentes do layout que criamos
        textViewCitacao = findViewById(R.id.text_view_citacao);
        textViewAutor = findViewById(R.id.text_view_autor);
        btnBuscarCitacao = findViewById(R.id.btn_buscar_nova_citacao);

        btnBuscarCitacao.setOnClickListener(v -> buscarCitacao());

        // Busca a primeira citação ao abrir a tela
        buscarCitacao();
    }

    private void buscarCitacao() {
        // Desativa o botão para evitar múltiplos cliques enquanto carrega
        btnBuscarCitacao.setEnabled(false);
        textViewCitacao.setText("Buscando uma nova dica...");
        textViewAutor.setText("");

        // Usa a sua classe RetrofitClient para criar o serviço
        QuoteApiService apiService = RetrofitClient.getClient().create(QuoteApiService.class);
        Call<Citacao> call = apiService.getRandomQuote();

        // Executa a chamada em segundo plano
        call.enqueue(new Callback<Citacao>() {
            @Override
            public void onResponse(Call<Citacao> call, Response<Citacao> response) {
                // Reativa o botão após a resposta
                btnBuscarCitacao.setEnabled(true);

                if (response.isSuccessful() && response.body() != null) {
                    Citacao citacao = response.body();
                    textViewCitacao.setText(String.format("“%s”", citacao.getContent()));
                    textViewAutor.setText(String.format("— %s", citacao.getAuthor()));
                } else {
                    textViewCitacao.setText("Falha ao buscar citação.");
                    Toast.makeText(CitacaoDoDiaActivity.this, "Não foi possível obter uma citação.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Citacao> call, Throwable t) {
                // Reativa o botão em caso de falha
                btnBuscarCitacao.setEnabled(true);
                textViewCitacao.setText("Erro de conexão.");

                // Mostra a mensagem de erro que vimos antes, agora de forma controlada
                Toast.makeText(CitacaoDoDiaActivity.this, "Erro de rede. Verifique sua conexão.", Toast.LENGTH_LONG).show();
                Log.e("CitacaoAPI", "Falha na chamada da API: ", t);
            }
        });
    }

    // Permite que o botão de voltar na toolbar funcione
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}