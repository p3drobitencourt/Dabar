package br.edu.ifsuldeminas.mch.dabar;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class AdapterCategorias extends RecyclerView.Adapter<AdapterCategorias.ViewHolder> {

    private List<Categoria> listaCategorias;
    private Context context;

    /**
     * Constructs an AdapterCategorias instance with the specified context and list of Categoria objects.
     *
     * @param context the context used for layout inflation
     * @param listaCategorias the list of Categoria objects to display in the adapter
     */
    public AdapterCategorias(Context context, List<Categoria> listaCategorias) {
        this.context = context;
        this.listaCategorias = listaCategorias;
    }

    /**
     * Creates and returns a new ViewHolder by inflating the item layout for a category summary.
     *
     * @param parent the parent ViewGroup into which the new view will be added
     * @param viewType the view type of the new view
     * @return a new ViewHolder instance containing the inflated item view
     */
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_resumo, parent, false);
        return new ViewHolder(view);
    }

    /**
     * Binds the data from a {@link Categoria} object to the corresponding views in the ViewHolder.
     *
     * Updates the title and description fields, and manages the visibility of description and category views based on the presence of description text.
     *
     * @param holder   The ViewHolder containing the views to update.
     * @param position The position of the item within the adapter's data set.
     */
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Categoria categoria = listaCategorias.get(position);

        holder.textViewTitulo.setText(categoria.getTitulo());
        holder.textViewCategoria.setVisibility(View.GONE);

        if (categoria.getDescricao() != null && !categoria.getDescricao().isEmpty()) {
            holder.textViewDescricao.setText(categoria.getDescricao());
            holder.textViewDescricao.setVisibility(View.VISIBLE);
        } else {
            holder.textViewDescricao.setVisibility(View.GONE);
        }
    }

    /**
     * Returns the number of Categoria items in the adapter.
     *
     * @return the size of the categoria list
     */
    @Override
    public int getItemCount() {
        return listaCategorias.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textViewTitulo;
        TextView textViewDescricao;
        TextView textViewCategoria;

        /**
         * Initializes the ViewHolder by binding its TextView fields to the corresponding views in the provided item layout.
         *
         * @param itemView The view representing a single item in the RecyclerView.
         */
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewTitulo = itemView.findViewById(R.id.textViewTitulo);
            textViewDescricao = itemView.findViewById(R.id.textViewDescricao);
            textViewCategoria = itemView.findViewById(R.id.textViewCategoria);
        }
    }
}