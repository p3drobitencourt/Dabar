package br.edu.ifsuldeminas.mch.dabar;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import br.edu.ifsuldeminas.mch.dabar.Categoria;
import br.edu.ifsuldeminas.mch.dabar.Resumo;
import br.edu.ifsuldeminas.mch.dabar.CategoriaDAO;
import br.edu.ifsuldeminas.mch.dabar.ResumoDAO;

/**
 * A classe principal do banco de dados Room.
 * Ela serve como o ponto de acesso central para a conexão com o banco de dados.
 *
 * @Database - Anotação que define a lista de entidades e a versão do banco.
 * exportSchema = false evita um aviso de build sobre o esquema do banco de dados.
 * Para um projeto de produção, é bom exportar o esquema para o controle de versão.
 */
@Database(entities = {Categoria.class, Resumo.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    // Métodos abstratos para que o Room forneça a implementação dos DAOs.
    public abstract CategoriaDAO categoriaDao();
    public abstract ResumoDAO resumoDao();

    // A palavra 'volatile' garante que a variável INSTANCE seja sempre lida da memória principal,
    // o que é importante para a segurança em ambientes com múltiplas threads.
    private static volatile AppDatabase INSTANCE;

    /**
     * Retorna a instância Singleton do banco de dados.
     * Se a instância não existir, ela é criada.
     * @param context O contexto da aplicação.
     * @return A instância única do AppDatabase.
     */
    public static AppDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            // O bloco 'synchronized' garante que apenas uma thread por vez
            // possa executar este código, evitando a criação de duas instâncias do banco.
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    AppDatabase.class, "dabar_database")
                            // Estratégia de migração destrutiva. Em um app de produção,
                            // você criaria classes de migração para não perder os dados dos usuários.
                            .fallbackToDestructiveMigration()
                            // IMPORTANTE: Permite que as queries sejam executadas na thread principal.
                            // Isso é feito aqui para SIMPLIFICAR o projeto do trabalho.
                            // Em um aplicativo real, isso pode travar a interface do usuário.
                            // O ideal é usar AsyncTask, Coroutines (Kotlin) ou RxJava para
                            // executar as operações do banco em uma thread separada.
                            .allowMainThreadQueries()
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}