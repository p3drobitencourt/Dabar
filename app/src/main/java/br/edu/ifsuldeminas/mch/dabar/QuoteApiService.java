package br.edu.ifsuldeminas.mch.dabar;

import retrofit2.Call;
import retrofit2.http.GET;

public interface QuoteApiService {
    @GET("random")
    Call<Citacao> getRandomQuote();
}