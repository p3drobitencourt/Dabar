
package br.edu.ifsuldeminas.mch.dabar;

public class Resumo {
    private int id;
    private String titulo;
    private String categoria;
    private String caminhoAudio;

    public Resumo() {}

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }

    public String getCategoria() { return categoria; }
    public void setCategoria(String categoria) { this.categoria = categoria; }

    public String getCaminhoAudio() { return caminhoAudio; }
    public void setCaminhoAudio(String caminhoAudio) { this.caminhoAudio = caminhoAudio; }
}