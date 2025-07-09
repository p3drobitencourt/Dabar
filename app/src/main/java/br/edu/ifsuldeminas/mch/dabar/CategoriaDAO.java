// CategoriaDAO.java
package br.edu.ifsuldeminas.mch.dabar;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import java.util.ArrayList;
import java.util.List;

public class CategoriaDAO {

    private ResumoDAO resumoDAO; // Usamos para pegar a instância do banco
    private SQLiteDatabase db;

    public CategoriaDAO(Context context) {
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
        // ATENÇÃO: Primeiro, é preciso lidar com os resumos que usam esta categoria.
        // A abordagem mais simples é torná-los "sem categoria" (null), mas isso
        // requer que a coluna 'categoria_id' em 'resumos' possa ser NULA.
        // Por agora, vamos apenas deletar a categoria.
        db.delete("categorias", "id = ?",
                new String[]{String.valueOf(categoria.getId())});
        db.close();
    }
}