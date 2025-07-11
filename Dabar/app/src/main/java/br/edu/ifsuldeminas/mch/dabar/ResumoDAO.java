package br.edu.ifsuldeminas.mch.dabar;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class ResumoDAO extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 2;
    private static final String DATABASE_NAME = "dabar.db";

    private static final String TABLE_RESUMOS = "resumos";
    private static final String TABLE_CATEGORIAS = "categorias";

    public ResumoDAO(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CATEGORias_TABLE = "CREATE TABLE " + TABLE_CATEGORIAS + "("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "titulo TEXT,"
                + "descricao TEXT" + ")";

        String CREATE_RESUMOS_TABLE = "CREATE TABLE " + TABLE_RESUMOS + "("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "titulo TEXT,"
                + "descricao TEXT,"
                + "caminho_audio TEXT,"
                + "categoria_id INTEGER,"
                + "FOREIGN KEY(categoria_id) REFERENCES categorias(id)" + ")";

        db.execSQL(CREATE_CATEGORias_TABLE);
        db.execSQL(CREATE_RESUMOS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RESUMOS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CATEGORIAS);
        onCreate(db);
    }

    // --- MÉTODOS CRUD (Create, Read, Update, Delete) PARA RESUMOS ---

    /**
     * Adiciona um novo resumo ao banco de dados.
     * @param resumo O objeto Resumo a ser salvo.
     */
    public void adicionarResumo(Resumo resumo) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("titulo", resumo.getTitulo());
        values.put("descricao", resumo.getDescricao());
        values.put("caminho_audio", resumo.getCaminhoAudio());
        values.put("categoria_id", resumo.getCategoria().getId());

        db.insert(TABLE_RESUMOS, null, values);
        db.close();
    }

    /**
     * Lista todos os resumos do banco, já com suas respectivas categorias preenchidas.
     * @return Uma lista de objetos Resumo.
     */
    public List<Resumo> listarTodosResumos() {
        List<Resumo> listaResumos = new ArrayList<>();
        String selectQuery = "SELECT "
                + "r.id as resumo_id, r.titulo as resumo_titulo, r.descricao as resumo_descricao, r.caminho_audio, "
                + "c.id as categoria_id, c.titulo as categoria_titulo, c.descricao as categoria_descricao "
                + "FROM " + TABLE_RESUMOS + " r "
                + "INNER JOIN " + TABLE_CATEGORIAS + " c ON r.categoria_id = c.id";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Categoria categoria = new Categoria();
                categoria.setId(cursor.getInt(cursor.getColumnIndexOrThrow("categoria_id")));
                categoria.setTitulo(cursor.getString(cursor.getColumnIndexOrThrow("categoria_titulo")));
                categoria.setDescricao(cursor.getString(cursor.getColumnIndexOrThrow("categoria_descricao")));

                Resumo resumo = new Resumo();
                resumo.setId(cursor.getInt(cursor.getColumnIndexOrThrow("resumo_id")));
                resumo.setTitulo(cursor.getString(cursor.getColumnIndexOrThrow("resumo_titulo")));
                resumo.setDescricao(cursor.getString(cursor.getColumnIndexOrThrow("resumo_descricao")));
                resumo.setCaminhoAudio(cursor.getString(cursor.getColumnIndexOrThrow("caminho_audio")));
                resumo.setCategoria(categoria);

                listaResumos.add(resumo);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return listaResumos;
    }

    /**
     * Atualiza os dados de um resumo existente no banco de dados.
     * @param resumo O objeto Resumo com os dados atualizados.
     * @return O número de linhas afetadas (deve ser 1 se a atualização foi bem-sucedida).
     */
    public int atualizarResumo(Resumo resumo) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("titulo", resumo.getTitulo());
        values.put("descricao", resumo.getDescricao());
        values.put("categoria_id", resumo.getCategoria().getId());
        // O caminho do áudio geralmente não é alterado, mas poderia ser incluído aqui se necessário.

        // Atualiza a linha onde o ID corresponde ao do resumo passado como parâmetro.
        int rowsAffected = db.update(TABLE_RESUMOS, values, "id = ?",
                new String[]{String.valueOf(resumo.getId())});
        db.close();

        return rowsAffected;
    }

    /**
     * Deleta um resumo do banco de dados com base no seu ID.
     * @param resumo O objeto Resumo a ser deletado.
     */
    public void deletarResumo(Resumo resumo) {
        SQLiteDatabase db = this.getWritableDatabase();

        // Deleta a linha onde o ID corresponde ao do resumo.
        db.delete(TABLE_RESUMOS, "id = ?",
                new String[]{String.valueOf(resumo.getId())});
        db.close();
    }
}