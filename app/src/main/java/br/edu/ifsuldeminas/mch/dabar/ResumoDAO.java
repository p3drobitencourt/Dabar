
package br.edu.ifsuldeminas.mch.dabar;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class ResumoDAO extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "dabar.db";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_RESUMOS = "resumos";

    private static final String KEY_ID = "id";
    private static final String KEY_TITULO = "titulo";
    private static final String KEY_CATEGORIA = "categoria";
    private static final String KEY_CAMINHO_AUDIO = "caminho_audio";

    public ResumoDAO(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE = "CREATE TABLE " + TABLE_RESUMOS + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + KEY_TITULO + " TEXT,"
                + KEY_CATEGORIA + " TEXT,"
                + KEY_CAMINHO_AUDIO + " TEXT" + ")";
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RESUMOS);
        onCreate(db);
    }
    public void adicionarResumo(Resumo resumo) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_TITULO, resumo.getTitulo());
        values.put(KEY_CATEGORIA, resumo.getCategoria());
        values.put(KEY_CAMINHO_AUDIO, resumo.getCaminhoAudio());

        db.insert(TABLE_RESUMOS, null, values);
        db.close();
    }

    public List<Resumo> listarTodosResumos() {
        List<Resumo> listaResumos = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_RESUMOS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Resumo resumo = new Resumo();
                resumo.setId(cursor.getInt(cursor.getColumnIndexOrThrow(KEY_ID)));
                resumo.setTitulo(cursor.getString(cursor.getColumnIndexOrThrow(KEY_TITULO)));
                resumo.setCategoria(cursor.getString(cursor.getColumnIndexOrThrow(KEY_CATEGORIA)));
                resumo.setCaminhoAudio(cursor.getString(cursor.getColumnIndexOrThrow(KEY_CAMINHO_AUDIO)));
                listaResumos.add(resumo);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return listaResumos;
    }

    public int atualizarResumo(Resumo resumo) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_TITULO, resumo.getTitulo());
        values.put(KEY_CATEGORIA, resumo.getCategoria());

        return db.update(TABLE_RESUMOS, values, KEY_ID + " = ?",
                new String[]{String.valueOf(resumo.getId())});
    }

    public void deletarResumo(Resumo resumo) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_RESUMOS, KEY_ID + " = ?",
                new String[]{String.valueOf(resumo.getId())});
        db.close();
    }
}