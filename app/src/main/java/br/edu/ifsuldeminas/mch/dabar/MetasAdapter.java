package br.edu.ifsuldeminas.mch.dabar;

import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;

public class MetasAdapter extends FirestoreRecyclerAdapter<Meta, MetasAdapter.MetaViewHolder> {

    private OnItemClickListener listener;

    public MetasAdapter(@NonNull FirestoreRecyclerOptions<Meta> options) {
        super(options);
    }

    // ... onBindViewHolder e onCreateViewHolder continuam iguais ...
    @Override
    protected void onBindViewHolder(@NonNull MetaViewHolder holder, int position, @NonNull Meta model) {
        holder.textViewTituloMeta.setText(model.getTitulo());
        holder.textViewDescricaoMeta.setText(model.getDescricao());
        holder.checkboxMeta.setChecked(model.isConcluida());

        if (model.isConcluida()) {
            holder.textViewTituloMeta.setPaintFlags(holder.textViewTituloMeta.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        } else {
            holder.textViewTituloMeta.setPaintFlags(holder.textViewTituloMeta.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
        }
    }

    @NonNull
    @Override
    public MetaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_meta, parent, false);
        return new MetaViewHolder(view);
    }


    class MetaViewHolder extends RecyclerView.ViewHolder {
        TextView textViewTituloMeta;
        TextView textViewDescricaoMeta;
        CheckBox checkboxMeta;
        ImageButton buttonDeletarMeta;

        public MetaViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewTituloMeta = itemView.findViewById(R.id.textViewTituloMeta);
            textViewDescricaoMeta = itemView.findViewById(R.id.textViewDescricaoMeta);
            checkboxMeta = itemView.findViewById(R.id.checkboxMeta);
            buttonDeletarMeta = itemView.findViewById(R.id.buttonDeletarMeta);

            // ✅ LISTENER ADICIONADO PARA O CLIQUE NO ITEM (PARA EDITAR)
            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION && listener != null) {
                    listener.onItemClick(getSnapshots().getSnapshot(position));
                }
            });

            checkboxMeta.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION && listener != null) {
                    listener.onCheckboxClick(getSnapshots().getSnapshot(position), checkboxMeta.isChecked());
                }
            });

            buttonDeletarMeta.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION && listener != null) {
                    listener.onDeleteClick(getSnapshots().getSnapshot(position));
                }
            });
        }
    }

    public interface OnItemClickListener {
        void onItemClick(DocumentSnapshot documentSnapshot); // ✅ Assinatura do método de clique
        void onCheckboxClick(DocumentSnapshot documentSnapshot, boolean isChecked);
        void onDeleteClick(DocumentSnapshot documentSnapshot);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }
}