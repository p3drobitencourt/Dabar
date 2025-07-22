package br.edu.ifsuldeminas.mch.dabar;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * Representa a tabela 'categorias' no banco de dados Room.
 * Cada instância desta classe é uma linha na tabela.
 */
@Entity(tableName = "categorias")
public class Categoria {

        /**
         * Chave primária autogerada pelo Room.
         * O Room garantirá que cada categoria tenha um ID único.
         */
        @PrimaryKey(autoGenerate = true)
        private int id;

        private String titulo;

        private String descricao;

        /**
         * Construtor vazio é necessário para o Room criar as instâncias da classe.
         */
        public Categoria() {}

        // --- Getters e Setters Padrão ---

        public int getId() {
                return id;
        }

        public void setId(int id) {
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

        /**
         * O método toString() é usado pelo ArrayAdapter do Spinner para exibir
         * o título da categoria na lista suspensa.
         * @return O título da categoria.
         */
        @Override
        public String toString() {
                return titulo;
        }
}