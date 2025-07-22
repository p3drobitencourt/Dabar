package br.edu.ifsuldeminas.mch.dabar.network;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Classe responsável por criar e configurar uma instância única (Singleton) do Retrofit.
 * Centraliza a configuração do cliente HTTP, facilitando a manutenção e o reuso.
 */
public class RetrofitClient {

    // A URL base da API que vamos consumir. Toda chamada será relativa a esta URL.
    private static final String BASE_URL = "https://api.quotable.io/";

    // A instância única e volátil do Retrofit.
    private static volatile Retrofit retrofitInstance = null;

    /**
     * Retorna a instância única do cliente Retrofit.
     * Se a instância ainda não existir, ela é criada de forma segura (thread-safe).
     *
     * @return A instância do Retrofit.
     */
    public static Retrofit getClient() {
        if (retrofitInstance == null) {
            // O bloco 'synchronized' garante que, mesmo com múltiplas threads,
            // a instância seja criada apenas uma vez.
            synchronized (RetrofitClient.class) {
                if (retrofitInstance == null) {
                    retrofitInstance = new Retrofit.Builder()
                            // 1. Define o endereço base da API.
                            .baseUrl(BASE_URL)
                            // 2. Adiciona o conversor GSON, que transformará a resposta JSON
                            // em nossos objetos Java (neste caso, na classe Citacao).
                            .addConverterFactory(GsonConverterFactory.create())
                            // 3. Constrói o objeto Retrofit.
                            .build();
                }
            }
        }
        return retrofitInstance;
    }
}