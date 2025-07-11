package br.edu.ifsuldeminas.mch.dabar;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import java.util.ArrayList;
import java.util.List;

public class CategoriaDAO {

    private ResumoDAO resumoDAO; // Usado para ter acesso à instância do banco
    private SQLiteDatabase db;

    public CategoriaDAO(Context context) {
        // Pega a instância do helper principal para acessar o mesmo banco
        resumoDAO = new ResumoDAO(context);
    }

    public void adicionarCategoria(Categoria categoria) {
        db = resumoDAO.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("titulo", categoria.getTitulo());
        values.put("descricao", categoria.getDescricao());

        db.insert("categorias", null, values);
        db.close();
    }

    public List<Categoria> listarTodasCategorias() {
        List<Categoria> listaCategorias = new ArrayList<>();
        db = resumoDAO.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM categorias", null);

        if (cursor.moveToFirst()) {
            do {
                Categoria categoria = new Categoria();
                categoria.setId(cursor.getInt(cursor.getColumnIndexOrThrow("id")));
                categoria.setTitulo(cursor.getString(cursor.getColumnIndexOrThrow("titulo")));
                categoria.setDescricao(cursor.getString(cursor.getColumnIndexOrThrow("descricao")));
                listaCategorias.add(categoria);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return listaCategorias;
    }

    /**
     * NOVO MÉTODO: Busca uma única categoria no banco de dados pelo seu ID.
     * @param id O ID da categoria a ser procurada.
     * @return Um objeto Categoria se encontrado, ou null se não houver categoria com esse ID.
     */
    public Categoria findCategoriaById(int id) {
        db = resumoDAO.getReadableDatabase();
        Cursor cursor = db.query("categorias", // Nome da tabela
                new String[]{"id", "titulo", "descricao"}, // Colunas que queremos buscar
                "id = ?", // Cláusula WHERE
                new String[]{String.valueOf(id)}, // Valor para o WHERE
                null, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            Categoria categoria = new Categoria();
            categoria.setId(cursor.getInt(cursor.getColumnIndexOrThrow("id")));
            categoria.setTitulo(cursor.getString(cursor.getColumnIndexOrThrow("titulo")));
            categoria.setDescricao(cursor.getString(cursor.getColumnIndexOrThrow("descricao")));

            cursor.close();
            db.close();
            return categoria;
        }

        if (cursor != null) {
            cursor.close();
        }
        db.close();
        // Retorna null se nenhuma categoria for encontrada com o ID especificado
        return null;
    }


    public int atualizarCategoria(Categoria categoria) {
        db = resumoDAO.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("titulo", categoria.getTitulo());
        values.put("descricao", categoria.getDescricao());

        int rowsAffected = db.update("categorias", values, "id = ?",
                new String[]{String.valueOf(categoria.getId())});
        db.close();
        return rowsAffected;
    }

    public void deletarCategoria(Categoria categoria) {
        db = resumoDAO.getWritableDatabase();
        db.delete("categorias", "id = ?",
                new String[]{String.valueOf(categoria.getId())});
        db.close();
    }
}