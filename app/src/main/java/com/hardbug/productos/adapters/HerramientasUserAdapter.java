package com.hardbug.productos.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.hardbug.productos.R;
import com.hardbug.productos.model.Herramientas;

public class HerramientasUserAdapter extends FirestoreRecyclerAdapter<Herramientas, HerramientasUserAdapter.ViewHolder> {
    private FirebaseFirestore mFirestore = FirebaseFirestore.getInstance();
    Activity activity;
    FragmentManager fm;

    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public HerramientasUserAdapter(@NonNull FirestoreRecyclerOptions<Herramientas> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull HerramientasUserAdapter.ViewHolder holder, int position, @NonNull Herramientas model) {
        holder.nombre.setText(model.getCode());
        holder.descripcion.setText(model.getDescripcion());
    }

    @NonNull
    @Override
    public HerramientasUserAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.cardview_herramientasuser, viewGroup, false);
        return new HerramientasUserAdapter.ViewHolder(v);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView nombre, descripcion;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nombre = itemView.findViewById(R.id.nombre_prodcategoriauser);
            descripcion = itemView.findViewById(R.id.desc_prodcategoriauser);
        }
    }
}
