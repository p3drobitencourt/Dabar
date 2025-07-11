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
    private OnItemClickListener listener; // Interface para o clique

    // Interface para o evento de clique
    public interface OnItemClickListener {
        void onItemClick(Resumo resumo);
    }

    public AdapterResumos(Context context, List<Resumo> listaResumos, OnItemClickListener listener) {
        this.context = context;
        this.listaResumos = listaResumos;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_resumo, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Resumo resumo = listaResumos.get(position);
        holder.bind(resumo, listener); // Passa o resumo e o listener
    }

    @Override
    public int getItemCount() {
        return listaResumos.size();
    }

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

        // Método para vincular os dados e o clique
        public void bind(final Resumo resumo, final OnItemClickListener listener) {
            textViewTitulo.setText(resumo.getTitulo());

            if (resumo.getCategoria() != null) {
                textViewCategoria.setText(resumo.getCategoria().getTitulo());
            } else {
                textViewCategoria.setText("Sem categoria");
            }

            if (resumo.getDescricao() != null && !resumo.getDescricao().isEmpty()) {
                textViewDescricao.setText(resumo.getDescricao());
                textViewDescricao.setVisibility(View.VISIBLE);
            } else {
                textViewDescricao.setVisibility(View.GONE);
            }

            // Configura o clique no item inteiro
            itemView.setOnClickListener(v -> listener.onItemClick(resumo));
        }
    }
}