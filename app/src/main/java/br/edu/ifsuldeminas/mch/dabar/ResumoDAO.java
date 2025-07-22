package br.edu.ifsuldeminas.mch.dabar;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import java.util.List;
import br.edu.ifsuldeminas.mch.dabar.Resumo;

/**
 * Data Access Object (DAO) para a entidade Resumo.
 * Define os métodos para interagir com a tabela 'resumos' no banco de dados.
 */
@Dao
public interface ResumoDAO {

    /**
     * Insere um novo resumo no banco de dados.
     * @param resumo O objeto Resumo a ser inserido.
     */
    @Insert
    void adicionarResumo(Resumo resumo);

    /**
     * Atualiza um resumo existente. O Room usa a chave primária para encontrar o registro.
     * @param resumo O objeto Resumo com os dados atualizados.
     */
    @Update
    void atualizarResumo(Resumo resumo);

    /**
     * Deleta um resumo do banco de dados.
     * @param resumo O objeto Resumo a ser deletado.
     */
    @Delete
    void deletarResumo(Resumo resumo);

    /**
     * Busca e retorna todos os resumos do banco, ordenados por título.
     * Note: Esta query retorna objetos Resumo sem o campo 'categoria' preenchido.
     * Você precisará buscar a categoria separadamente usando o 'categoriaId'.
     * @return Uma lista com todos os objetos Resumo.
     */
    @Query("SELECT * FROM resumos ORDER BY titulo ASC")
    List<Resumo> listarTodosResumos();

    /**
     * Busca um único resumo pelo seu ID.
     * @param id O ID do resumo a ser procurado.
     * @return O objeto Resumo correspondente ou null se não for encontrado.
     */
    @Query("SELECT * FROM resumos WHERE id = :id LIMIT 1")
    Resumo findResumoById(int id);
}