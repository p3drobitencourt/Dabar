package br.edu.ifsuldeminas.mch.dabar;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

/**
 * Adapter para a RecyclerView que exibe a lista de Categorias.
 * Ele usa o layout 'item_resumo.xml' para cada item, mas o adapta
 * para mostrar apenas as informações relevantes de uma Categoria.
 */
public class AdapterCategorias extends RecyclerView.Adapter<AdapterCategorias.ViewHolder> {

    private List<Categoria> listaCategorias;
    private Context context;

    /**
     * Construtor do adapter.
     * @param context O contexto da Activity que está usando o adapter.
     * @param listaCategorias A lista inicial de categorias a ser exibida.
     */
    public AdapterCategorias(Context context, List<Categoria> listaCategorias) {
        this.context = context;
        this.listaCategorias = listaCategorias;
    }

    /**
     * Chamado quando a RecyclerView precisa de um novo ViewHolder para representar um item.
     * Ele infla (cria) a view a partir do arquivo XML do layout do item.
     * @param parent O ViewGroup ao qual a nova view será adicionada.
     * @param viewType O tipo da view.
     * @return Um novo objeto ViewHolder que contém a view para o item.
     */
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_resumo, parent, false);
        return new ViewHolder(view);
    }

    /**
     * Chamado pela RecyclerView para exibir os dados na posição especificada.
     * Este método pega os dados da categoria na 'position' e preenche as views
     * dentro do ViewHolder.
     * @param holder O ViewHolder que deve ser atualizado.
     * @param position A posição do item na lista.
     */
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Categoria categoria = listaCategorias.get(position);

        holder.textViewTitulo.setText(categoria.getTitulo());

        // Como este adapter é para categorias, escondemos o campo de categoria do item.
        holder.textViewCategoria.setVisibility(View.GONE);

        // Se a descrição da categoria existir, a exibe. Senão, esconde o campo de descrição.
        if (categoria.getDescricao() != null && !categoria.getDescricao().isEmpty()) {
            holder.textViewDescricao.setText(categoria.getDescricao());
            holder.textViewDescricao.setVisibility(View.VISIBLE);
        } else {
            holder.textViewDescricao.setVisibility(View.GONE);
        }
    }

    /**
     * Retorna o número total de itens na lista.
     * @return O tamanho da lista de categorias.
     */
    @Override
    public int getItemCount() {
        return listaCategorias.size();
    }

    /**
     * Método auxiliar para atualizar a lista de categorias do adapter
     * e notificar a RecyclerView para que ela se redesenhe.
     * @param novaLista A nova lista de categorias a ser exibida.
     */
    public void atualizarLista(List<Categoria> novaLista) {
        this.listaCategorias.clear();
        this.listaCategorias.addAll(novaLista);
        notifyDataSetChanged(); // Este comando é vital para a atualização da UI.
    }

    /**
     * Classe interna que representa a view de cada item na RecyclerView.
     * Ela armazena as referências para as views (TextViews) para evitar
     * chamadas repetidas a 'findViewById'.
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textViewTitulo;
        TextView textViewDescricao;
        TextView textViewCategoria;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewTitulo = itemView.findViewById(R.id.textViewTitulo);
            textViewDescricao = itemView.findViewById(R.id.textViewDescricao);
            textViewCategoria = itemView.findViewById(R.id.textViewCategoria);
        }
    }
}