package br.edu.ifsuldeminas.mch.dabar;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class AdapterResumos extends RecyclerView.Adapter<AdapterResumos.ViewHolder> {

    private List<Resumo> listaResumos;
    private Context context;

    public AdapterResumos(Context context, List<Resumo> listaResumos) {
        this.context = context;
        this.listaResumos = listaResumos;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Infla (cria) a view a partir do nosso layout de item customizado.
        View view = LayoutInflater.from(context).inflate(R.layout.item_resumo, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // Pega o resumo da posição atual na lista.
        Resumo resumo = listaResumos.get(position);

        // Preenche os componentes do layout com os dados do resumo.
        holder.textViewTitulo.setText(resumo.getTitulo());

        // Acessa o título da categoria através do objeto Categoria aninhado.
        // É importante verificar se a categoria não é nula para evitar crashes.
        if (resumo.getCategoria() != null) {
            holder.textViewCategoria.setText(resumo.getCategoria().getTitulo());
        } else {
            holder.textViewCategoria.setText("Sem categoria");
        }

        // Preenche a descrição e só a torna visível se ela não estiver vazia.
        if (resumo.getDescricao() != null && !resumo.getDescricao().isEmpty()) {
            holder.textViewDescricao.setText(resumo.getDescricao());
            holder.textViewDescricao.setVisibility(View.VISIBLE);
        } else {
            // Se não houver descrição, o campo fica invisível para economizar espaço.
            holder.textViewDescricao.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return listaResumos.size();
    }

    /**
     * ViewHolder: Mapeia os componentes do layout do item (item_resumo.xml) para
     * que possamos acessá-los via código de forma eficiente.
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textViewTitulo;
        TextView textViewDescricao; // NOVO
        TextView textViewCategoria;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewTitulo = itemView.findViewById(R.id.textViewTitulo);
            textViewDescricao = itemView.findViewById(R.id.textViewDescricao); // NOVO
            textViewCategoria = itemView.findViewById(R.id.textViewCategoria);
        }
    }
}