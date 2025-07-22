package br.edu.ifsuldeminas.mch.dabar.network;

import br.edu.ifsuldeminas.mch.dabar.Citacao;
import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Esta interface define todos os endpoints (as "rotas") da API que vamos consumir.
 * O Retrofit usará esta interface para gerar todo o código de rede necessário.
 */
public interface ApiService {

    /**
     * Define uma chamada GET para o endpoint "random" da API.
     *
     * @GET("random") - Informa ao Retrofit para fazer uma requisição do tipo GET
     * para a URL base + "random".
     *
     * Call<Citacao> - Informa ao Retrofit que esperamos receber uma resposta que pode
     * ser convertida em um objeto do nosso "molde" Citacao.java.
     *
     * @return um objeto Call que pode ser executado para fazer a requisição de rede.
     */
    @GET("random")
    Call<Citacao> getCitacaoAleatoria();

}