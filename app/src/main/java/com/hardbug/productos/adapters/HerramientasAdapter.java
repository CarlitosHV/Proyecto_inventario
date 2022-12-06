package com.hardbug.productos.adapters;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.hardbug.productos.R;
import com.hardbug.productos.fragment_modificar;
import com.hardbug.productos.model.Herramientas;

public class HerramientasAdapter extends FirestoreRecyclerAdapter<Herramientas, HerramientasAdapter.ViewHolder> {
    private FirebaseFirestore mFirestore = FirebaseFirestore.getInstance();
    Activity activity;
    FragmentManager fm;

    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public HerramientasAdapter(@NonNull FirestoreRecyclerOptions<Herramientas> options, Activity activity, androidx.fragment.app.FragmentManager fm) {
        super(options);
        this.activity = activity;
        this.fm = fm;
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull Herramientas model) {
        DocumentSnapshot documentSnapshot = getSnapshots().getSnapshot(holder.getAbsoluteAdapterPosition());
        final String id = documentSnapshot.getId();
        holder.nombre.setText(model.getCode());
        holder.descripcion.setText(model.getDescripcion());

        holder.eliminar.setOnClickListener(view -> {
            deleteHerramienta(id);
        });

        holder.editar.setOnClickListener(view -> {
            fragment_modificar modifica = new fragment_modificar();
            Bundle bundle = new Bundle();
            bundle.putString("id_herramienta", id);
            modifica.setArguments(bundle);
            FragmentTransaction fragmentTransaction = fm.beginTransaction();
            fragmentTransaction.replace(R.id.content, modifica, "");
            fragmentTransaction.setReorderingAllowed(true);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        });
    }

    public void deleteHerramienta(String id){
        mFirestore.collection("Herramientas").document(id).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(activity, "¡Herramienta eliminada con éxito!", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(activity, "Ocurrió un error al eliminar el producto", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.cardview_categorias, viewGroup, false);
        return new HerramientasAdapter.ViewHolder(v);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView nombre, descripcion;
        Button eliminar, editar;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nombre = itemView.findViewById(R.id.nombre_prodcategoria);
            descripcion = itemView.findViewById(R.id.desc_prodcategoria);
            eliminar = itemView.findViewById(R.id.btneliminarcategoria);
            editar = itemView.findViewById(R.id.btneditarcategoria);
        }
    }
}
