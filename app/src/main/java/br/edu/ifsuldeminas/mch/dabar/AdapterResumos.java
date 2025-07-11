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
    private int longClickedPosition;

    public AdapterResumos(Context context, List<Resumo> listaResumos) {
        this.context = context;
        this.listaResumos = listaResumos;
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

        holder.textViewTitulo.setText(resumo.getTitulo());
        holder.bind(resumo);

        holder.itemView.setOnLongClickListener(v -> {
            setLongClickedPosition(holder.getAdapterPosition());
            return false; // Retornar false permite que o menu de contexto continue a ser criado
        });


        if (resumo.getCategoria() != null) {
            holder.textViewCategoria.setText(resumo.getCategoria().getTitulo());
        } else {
            holder.textViewCategoria.setText("Sem categoria");
        }

        if (resumo.getDescricao() != null && !resumo.getDescricao().isEmpty()) {
            holder.textViewDescricao.setText(resumo.getDescricao());
            holder.textViewDescricao.setVisibility(View.VISIBLE);
        } else {
            holder.textViewDescricao.setVisibility(View.GONE);
        }
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

        public void bind(Resumo resumo) {
            // Pega os dados do objeto 'resumo' e coloca nas Views

            textViewTitulo.setText(resumo.getTitulo());

            // Verifica se o objeto Categoria não é nulo antes de pegar o nome
            if (resumo.getCategoria() != null) {
                textViewCategoria.setText(resumo.getCategoria().getTitulo());
            } else {
                textViewCategoria.setText("Sem categoria");
            }

            /* Formata e exibe a duração do áudio
            long duracaoMs = resumo.getDuracao();
            String tempoFormatado = String.format("%02d:%02d",
                    java.util.concurrent.TimeUnit.MILLISECONDS.toMinutes(duracaoMs),
                    java.util.concurrent.TimeUnit.MILLISECONDS.toSeconds(duracaoMs) -
                            java.util.concurrent.TimeUnit.MINUTES.toSeconds(java.util.concurrent.TimeUnit.MILLISECONDS.toMinutes(duracaoMs))
            );
            textViewDuracao.setText(tempoFormatado);*/
        }
    }

    public int getLongClickedPosition() {
        return longClickedPosition;
    }

    public void setLongClickedPosition(int longClickedPosition) {
        this.longClickedPosition = longClickedPosition;
    }
}

