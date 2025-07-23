package br.edu.ifsuldeminas.mch.dabar;

import androidx.lifecycle.LiveData; // ✅ IMPORTAÇÃO ESSENCIAL
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface ResumoDAO {

    @Insert
    void adicionarResumo(Resumo resumo);

    @Update
    void atualizarResumo(Resumo resumo);

    @Delete
    void deletarResumo(Resumo resumo);

    // ✅ CORREÇÃO APLICADA AQUI
    // O método agora retorna um LiveData<List<Resumo>>,
    // permitindo que ele seja "observado" pela tela.
    @Query("SELECT * FROM resumos ORDER BY id DESC")
    LiveData<List<Resumo>> listarTodosResumos();

}