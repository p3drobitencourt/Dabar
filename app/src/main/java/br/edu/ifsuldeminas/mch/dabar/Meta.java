package br.edu.ifsuldeminas.mch.dabar;

import com.google.firebase.firestore.Exclude;

public class Meta {

    @Exclude
    private String id;
    private String titulo;
    private String descricao;
    private boolean concluida;
    private String userId; // ✅ CAMPO ADICIONADO

    // Construtor vazio é OBRIGATÓRIO para o Firestore
    public Meta() {}

    // Getters e Setters para todos os campos
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }
    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }
    public boolean isConcluida() { return concluida; }
    public void setConcluida(boolean concluida) { this.concluida = concluida; }
    public String getUserId() { return userId; } // ✅ GETTER ADICIONADO
    public void setUserId(String userId) { this.userId = userId; } // ✅ SETTER ADICIONADO
}