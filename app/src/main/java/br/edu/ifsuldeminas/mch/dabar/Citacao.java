package br.edu.ifsuldeminas.mch.dabar;

import com.google.gson.annotations.SerializedName;

public class Citacao {
    // Usa o nome exato que a API retorna ("content")
    @SerializedName("content")
    private String content;

    // Usa o nome exato que a API retorna ("author")
    @SerializedName("author")
    private String author;

    public String getContent() {
        return content;
    }

    public String getAuthor() {
        return author;
    }
}