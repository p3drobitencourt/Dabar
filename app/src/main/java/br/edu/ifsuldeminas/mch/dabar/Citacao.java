package br.edu.ifsuldeminas.mch.dabar;

import com.google.gson.annotations.SerializedName;

/**
 * Representa o "molde" para a resposta da API de citações (Quotable.io).
 * Usamos a anotação @SerializedName para garantir que o GSON associe
 * corretamente os campos do JSON com os atributos da nossa classe.
 */
public class Citacao {

    /**
     * O campo 'content' no JSON será mapeado para este atributo.
     */
    @SerializedName("content")
    private String texto;

    /**
     * O campo 'author' no JSON será mapeado para este atributo.
     */
    @SerializedName("author")
    private String autor;

    // --- Getters e Setters ---

    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }
}