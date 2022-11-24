package com.hardbug.productos;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.firebase.FirebaseApp;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.hardbug.productos.design.CustomAdapterHerramientas;
import com.hardbug.productos.model.Herramientas;
import com.hardbug.productos.model.UserType;

import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link fragment_crud_registro#newInstance} factory method to
 * create an instance of this fragment.
 */
public class fragment_crud_registro extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    MaterialToolbar toolbar;


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    //Variables de uso

    private FirebaseDatabase firebaseDB;
    private FirebaseFirestore firestore;

    private ListView listaHerramientas;
    private ListView listaConsumibles;

    private void iniciarFireBase(){
        FirebaseApp.initializeApp(getContext());
        firebaseDB = FirebaseDatabase.getInstance();
        firestore = FirebaseFirestore.getInstance();
    }

    public fragment_crud_registro() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment fragment_crud_registro.
     */
    // TODO: Rename and change types and number of parameters
    public static fragment_crud_registro newInstance(String param1, String param2) {
        fragment_crud_registro fragment = new fragment_crud_registro();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_crud_registro, container, false);
        listaHerramientas = root.findViewById(R.id.custom_list_view_herramientas);
        //listaConsumibles = root.findViewById(R.id.custom_list_view_consumibles);

        listaHerramientas.setOnTouchListener(new View.OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {
                v.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });

        iniciarFireBase();
        llamarHerramientas();
        //llamarConsumibles();

        toolbar = root.findViewById(R.id.toolbarcrearregistros);
        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setNavigationOnClickListener(view -> {
            fragment_registros registros = new fragment_registros();
            FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.content, registros, "");
            fragmentTransaction.setReorderingAllowed(true);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        });

        return root;
    }

    private void llamarHerramientas(){
        ArrayList<Herramientas> herramientas = new ArrayList<>();
        firestore.collection("Herramientas")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Map<String, Object> obj = document.getData();
                                Herramientas herramienta = new Herramientas();
                                herramienta.setCode(obj.get("code").toString());
                                int count = Integer.parseInt(obj.get("count").toString());
                                herramienta.setCount(count);
                                herramienta.setDescripcion(obj.get("descripcion").toString());
                                herramientas.add(herramienta);
                            }
                        }
                        CustomAdapterHerramientas adapter = new CustomAdapterHerramientas(getContext(), herramientas);
                        listaHerramientas.setAdapter(adapter);
                    }
                });
    }

    private void llamarConsumibles(){
        ArrayList<Herramientas> herramientas = new ArrayList<>();
            firestore.collection("Consumibles")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Map<String, Object> obj = document.getData();
                                Herramientas herramienta = new Herramientas();
                                herramienta.setCode(obj.get("code").toString());
                                int count = Integer.parseInt(obj.get("count").toString());
                                herramienta.setCount(count);
                                herramienta.setDescripcion(obj.get("descripcion").toString());
                                herramientas.add(herramienta);
                            }
                        }
                        CustomAdapterHerramientas adapter = new CustomAdapterHerramientas(getContext(), herramientas);
                        listaConsumibles.setAdapter(adapter);
                    }
                });
    }
}