package com.hardbug.productos.adapters;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.hardbug.productos.R;
import com.hardbug.productos.model.Herramientas;

public class RegistrosAdapter extends FirestoreRecyclerAdapter<Herramientas, RegistrosAdapter.ViewHolder> {
    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public RegistrosAdapter(@NonNull FirestoreRecyclerOptions<Herramientas> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull Herramientas model) {
        holder.nombre.setText(model.getCode());
        holder.descripcion.setText(model.getDescripcion());
        holder.cantidad.setText(String.valueOf(model.getCount()));

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.cardview_registros, viewGroup, false);
        return new ViewHolder(v);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView nombre, descripcion, cantidad;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nombre = itemView.findViewById(R.id.nombre_registro);
            descripcion = itemView.findViewById(R.id.descripcion_registro);
            cantidad = itemView.findViewById(R.id.cantidad_registro);
        }
    }
}
