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
    private OnItemClickListener listener; // Para o clique simples
    private int longClickedPosition;      // Para o clique longo (menu)

    // Interface para o evento de clique simples
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
        // O método bind agora cuida de tudo
        holder.bind(resumo, listener);

        // Listener para o clique longo, para pegar a posição para o menu
        holder.itemView.setOnLongClickListener(v -> {
            setLongClickedPosition(holder.getAdapterPosition());
            return false; // Retornar false permite que o menu de contexto continue
        });
    }

    @Override
    public int getItemCount() {
        return listaResumos.size();
    }

    // Métodos para o menu de contexto
    public int getLongClickedPosition() {
        return longClickedPosition;
    }

    public void setLongClickedPosition(int longClickedPosition) {
        this.longClickedPosition = longClickedPosition;
    }

    // ViewHolder não precisa de mudanças, mas aqui está ele completo para referência
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

        // Método para vincular os dados e o clique simples
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

            // Configura o clique simples no item
            itemView.setOnClickListener(v -> listener.onItemClick(resumo));
        }
    }
}