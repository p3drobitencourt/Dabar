package br.edu.ifsuldeminas.mch.dabar;

import com.google.firebase.firestore.Exclude;

/**
 * Representa um documento na coleção 'metas' do Firestore.
 * É um Plain Old Java Object (POJO).
 */
public class Meta {

    // Usamos @Exclude para que o Firestore não tente salvar este campo,
    // pois ele será o próprio ID do documento.
    @Exclude
    private String id;

    private String titulo;
    private String descricao;
    private boolean concluida;

    // Construtor vazio é OBRIGATÓRIO para que o Firestore consiga
    // converter os documentos em objetos Java automaticamente.
    public Meta() {}

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public boolean isConcluida() {
        return concluida;
    }

    public void setConcluida(boolean concluida) {
        this.concluida = concluida;
    }
}