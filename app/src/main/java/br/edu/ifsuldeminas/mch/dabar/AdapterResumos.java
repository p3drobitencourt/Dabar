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

    // Interface para o clique simples
    public interface OnItemClickListener {
        /**
 * Called when a Resumo item is clicked.
 *
 * @param resumo The Resumo object associated with the clicked item.
 */
void onItemClick(Resumo resumo);
    }

    /**
     * Constructs an AdapterResumos with the specified context, list of Resumo objects, and item click listener.
     *
     * @param context the context in which the adapter is operating
     * @param listaResumos the list of Resumo objects to display
     * @param listener the listener to handle item click events
     */
    public AdapterResumos(Context context, List<Resumo> listaResumos, OnItemClickListener listener) {
        this.context = context;
        this.listaResumos = listaResumos;
        this.listener = listener;
    }

    /**
     * Creates and returns a new ViewHolder for an item in the RecyclerView.
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
     * Binds the data for the item at the specified position to the provided ViewHolder and sets up long-click handling.
     *
     * @param holder   The ViewHolder to bind data to.
     * @param position The position of the item within the adapter's data set.
     */
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Resumo resumo = listaResumos.get(position);
        holder.bind(resumo, listener); // O bind agora cuida de tudo

        // Configura o clique longo para obter a posição para o menu de contexto
        holder.itemView.setOnLongClickListener(v -> {
            setLongClickedPosition(holder.getAdapterPosition());
            return false; // Retornar false permite que o menu de contexto continue a ser criado
        });
    }

    /**
     * Returns the total number of Resumo items in the adapter.
     *
     * @return the number of items in the list
     */
    @Override
    public int getItemCount() {
        return listaResumos.size();
    }

    /**
     * Returns the adapter position of the item that was most recently long-clicked.
     *
     * @return the position of the long-clicked item, or -1 if no item has been long-clicked.
     */
    public int getLongClickedPosition() {
        return longClickedPosition;
    }

    /**
     * Sets the position of the item that was most recently long-clicked.
     *
     * @param longClickedPosition the adapter position of the long-clicked item
     */
    public void setLongClickedPosition(int longClickedPosition) {
        this.longClickedPosition = longClickedPosition;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textViewTitulo, textViewDescricao, textViewCategoria;

        /**
         * Initializes the ViewHolder by locating and storing references to the title, description, and category TextViews within the item layout.
         *
         * @param itemView The view representing a single item in the RecyclerView.
         */
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewTitulo = itemView.findViewById(R.id.textViewTitulo);
            textViewDescricao = itemView.findViewById(R.id.textViewDescricao);
            textViewCategoria = itemView.findViewById(R.id.textViewCategoria);
        }

        /**
         * Binds the data from a Resumo object to the item view and sets up a click listener.
         *
         * Updates the title, category, and description views based on the Resumo's properties.
         * If the description is null or empty, the description view is hidden.
         * Sets a click listener on the item view that triggers the provided OnItemClickListener with the current Resumo.
         *
         * @param resumo   the Resumo object whose data will be displayed
         * @param listener the listener to handle item click events
         */
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