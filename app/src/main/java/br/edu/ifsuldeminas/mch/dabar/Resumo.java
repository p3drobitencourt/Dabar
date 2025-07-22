package br.edu.ifsuldeminas.mch.dabar;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

/**
 * Representa a tabela 'resumos' no banco de dados Room.
 * Define uma chave estrangeira que a conecta com a tabela 'categorias'.
 * Se uma categoria for deletada, todos os resumos associados a ela também serão (onDelete = ForeignKey.CASCADE).
 */
@Entity(tableName = "resumos",
        foreignKeys = @ForeignKey(entity = Categoria.class,
                parentColumns = "id",
                childColumns = "categoria_id",
                onDelete = ForeignKey.CASCADE))
public class Resumo {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private String titulo;

    private String descricao;

    /**
     * @ColumnInfo permite usar um nome diferente na coluna do banco de dados
     * em relação ao nome do atributo na classe.
     */
    @ColumnInfo(name = "caminho_audio")
    private String caminhoAudio;

    /**
     * Esta é a coluna que armazena a chave estrangeira.
     * Ela se conecta com o 'id' da tabela 'categorias'.
     */
    @ColumnInfo(name = "categoria_id", index = true) // 'index = true' melhora a performance de buscas
    private int categoriaId;

    /**
     * O Room não tentará salvar este campo no banco de dados.
     * Ele pode ser usado para carregar o objeto Categoria completo na lógica da sua aplicação,
     * após o Resumo ter sido recuperado do banco.
     */
    @Ignore
    private Categoria categoria;

    /**
     * Construtor vazio obrigatório para o Room.
     */
    public Resumo() {}

    // --- Getters e Setters Padrão ---

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getCaminhoAudio() {
        return caminhoAudio;
    }

    public void setCaminhoAudio(String caminhoAudio) {
        this.caminhoAudio = caminhoAudio;
    }

    public int getCategoriaId() {
        return categoriaId;
    }

    public void setCategoriaId(int categoriaId) {
        this.categoriaId = categoriaId;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
        // Garante que o ID também seja setado quando o objeto é associado
        if (categoria != null) {
            this.categoriaId = categoria.getId();
        }
    }
}