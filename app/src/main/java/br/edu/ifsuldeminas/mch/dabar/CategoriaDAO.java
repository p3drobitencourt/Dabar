package br.edu.ifsuldeminas.mch.dabar;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import java.util.List;
import br.edu.ifsuldeminas.mch.dabar.Categoria;

/**
 * Data Access Object (DAO) para a entidade Categoria.
 * O Room usa esta interface para gerar o código de acesso ao banco de dados.
 * Você define as operações do banco com anotações, sem escrever SQL manualmente (exceto para queries).
 */
@Dao
public interface CategoriaDAO {

    /**
     * Insere uma nova categoria no banco de dados.
     * @param categoria O objeto Categoria a ser inserido.
     */
    @Insert
    void adicionarCategoria(Categoria categoria);

    /**
     * Atualiza uma categoria existente no banco de dados.
     * O Room identifica a categoria a ser atualizada pela sua chave primária (id).
     * @param categoria O objeto Categoria com os dados atualizados.
     */
    @Update
    void atualizarCategoria(Categoria categoria);

    /**
     * Deleta uma categoria do banco de dados.
     * @param categoria O objeto Categoria a ser deletado.
     */
    @Delete
    void deletarCategoria(Categoria categoria);

    /**
     * Busca e retorna todas as categorias da tabela, ordenadas pelo título.
     * @return Uma lista de todos os objetos Categoria.
     */
    @Query("SELECT * FROM categorias ORDER BY titulo ASC")
    List<Categoria> listarTodasCategorias();

    /**
     * Busca uma única categoria pelo seu ID.
     * @param id O ID da categoria a ser procurada.
     * @return O objeto Categoria correspondente ou null se não for encontrado.
     */
    @Query("SELECT * FROM categorias WHERE id = :id LIMIT 1")
    Categoria findCategoriaById(int id);
}