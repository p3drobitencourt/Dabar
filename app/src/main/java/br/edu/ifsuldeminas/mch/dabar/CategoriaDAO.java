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

    /**
     * Constructs a CategoriaDAO using the provided context to initialize database access.
     *
     * @param context the application context used to obtain the database helper
     */
    public CategoriaDAO(Context context) {
        // Pega a instância do helper principal para acessar o mesmo banco
        resumoDAO = new ResumoDAO(context);
    }

    /**
     * Inserts a new category into the "categorias" table using the provided Categoria object's title and description.
     *
     * @param categoria the Categoria object containing the title and description to be added
     */
    public void adicionarCategoria(Categoria categoria) {
        db = resumoDAO.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("titulo", categoria.getTitulo());
        values.put("descricao", categoria.getDescricao());

        db.insert("categorias", null, values);
        db.close();
    }

    /**
     * Retrieves all categories from the database.
     *
     * @return a list of all Categoria objects stored in the "categorias" table
     */
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
     * Retrieves a category from the database by its ID.
     *
     * @param id the unique identifier of the category to retrieve
     * @return a Categoria object if found, or null if no category exists with the specified ID
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


    /**
     * Updates the title and description of an existing category in the database.
     *
     * @param categoria The category object containing the updated information and its unique ID.
     * @return The number of rows affected by the update operation.
     */
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

    /**
     * Deletes the specified category from the "categorias" table in the database.
     *
     * @param categoria the category to be deleted
     */
    public void deletarCategoria(Categoria categoria) {
        db = resumoDAO.getWritableDatabase();
        db.delete("categorias", "id = ?",
                new String[]{String.valueOf(categoria.getId())});
        db.close();
    }
}