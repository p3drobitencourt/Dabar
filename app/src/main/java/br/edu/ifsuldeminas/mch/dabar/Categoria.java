package br.edu.ifsuldeminas.mch.dabar;

public class Categoria {
        private int id;
        private String titulo;
        private String descricao;

        // Construtor, Getters e Setters
        public Categoria() {}

        public int getId() { return id; }
        public void setId(int id) { this.id = id; }

        public String getTitulo() { return titulo; }
        public void setTitulo(String titulo) { this.titulo = titulo; }

        public String getDescricao() { return descricao; }
        public void setDescricao(String descricao) { this.descricao = descricao; }

        @Override
        public String toString() {
            return titulo;
        }
}

