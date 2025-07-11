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
        View view = LayoutInflater.from(context).inflate(R.layout.item_resumo, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Resumo resumo = listaResumos.get(position);

        holder.textViewTitulo.setText(resumo.getTitulo());

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
    }
}