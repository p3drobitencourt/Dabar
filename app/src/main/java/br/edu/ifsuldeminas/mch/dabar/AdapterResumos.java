
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
        // Infla o layout do item (item_resumo.xml)
        View view = LayoutInflater.from(context).inflate(R.layout.item_resumo, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // Pega o resumo da posição atual
        Resumo resumo = listaResumos.get(position);
        // Define os textos
        holder.textViewTitulo.setText(resumo.getTitulo());
        holder.textViewCategoria.setText(resumo.getCategoria());
    }

    @Override
    public int getItemCount() {
        return listaResumos.size();
    }
    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textViewTitulo;
        TextView textViewCategoria;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewTitulo = itemView.findViewById(R.id.textViewTitulo);
            textViewCategoria = itemView.findViewById(R.id.textViewCategoria);
        }
    }
}