package br.edu.ifsuldeminas.mch.dabar;

public class Categoria {
        private int id;
        private String titulo;
        private String descricao;

        /**
 * Constructs a new Categoria instance with default values.
 */
public Categoria() {}

        /**
 * Returns the unique identifier of this category.
 *
 * @return the category's id
 */
public int getId() { return id; }
        /**
 * Sets the unique identifier for this category.
 *
 * @param id the identifier to assign to this category
 */
public void setId(int id) { this.id = id; }

        /**
 * Returns the title of the category.
 *
 * @return the category title
 */
public String getTitulo() { return titulo; }
        /**
 * Sets the title of the category.
 *
 * @param titulo the new title to assign
 */
public void setTitulo(String titulo) { this.titulo = titulo; }

        /**
 * Retrieves the description of the category.
 *
 * @return the description of the category
 */
public String getDescricao() { return descricao; }
        /**
 * Sets the description of the category.
 *
 * @param descricao the new description to assign
 */
public void setDescricao(String descricao) { this.descricao = descricao; }

        /**
         * Returns the title of the category as its string representation.
         *
         * @return the value of the {@code titulo} attribute
         */
        @Override
        public String toString() {
            return titulo;
        }
}

