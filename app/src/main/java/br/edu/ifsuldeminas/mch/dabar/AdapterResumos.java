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
    private OnItemClickListener listener;
    private int longClickedPosition;

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
        holder.bind(resumo, listener);

        holder.itemView.setOnLongClickListener(v -> {
            setLongClickedPosition(holder.getAdapterPosition());
            return false;
        });
    }

    @Override
    public int getItemCount() {
        return listaResumos.size();
    }

    public int getLongClickedPosition() {
        return longClickedPosition;
    }

    public void setLongClickedPosition(int longClickedPosition) {
        this.longClickedPosition = longClickedPosition;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textViewTitulo, textViewDescricao, textViewCategoria;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewTitulo = itemView.findViewById(R.id.textViewTitulo);
            textViewDescricao = itemView.findViewById(R.id.textViewDescricao);
            textViewCategoria = itemView.findViewById(R.id.textViewCategoria);
        }

        public void bind(final Resumo resumo, final OnItemClickListener listener) {
            textViewTitulo.setText(resumo.getTitulo());

            // Verifica se o objeto Categoria dentro do Resumo não é nulo
            if (resumo.getCategoria() != null) {
                textViewCategoria.setText(resumo.getCategoria().getTitulo());
                textViewCategoria.setVisibility(View.VISIBLE);
            } else {
                // Se por algum motivo a categoria não for encontrada, esconde o campo
                textViewCategoria.setText("Sem categoria");
                textViewCategoria.setVisibility(View.GONE);
            }

            if (resumo.getDescricao() != null && !resumo.getDescricao().isEmpty()) {
                textViewDescricao.setText(resumo.getDescricao());
                textViewDescricao.setVisibility(View.VISIBLE);
            } else {
                textViewDescricao.setVisibility(View.GONE);
            }

            itemView.setOnClickListener(v -> listener.onItemClick(resumo));
        }
    }
}