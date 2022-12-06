package com.hardbug.productos;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.SearchView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.hardbug.productos.adapters.HerramientasAdapter;
import com.hardbug.productos.adapters.HerramientasUserAdapter;
import com.hardbug.productos.model.Herramientas;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link fragment_herramientas_user#newInstance} factory method to
 * create an instance of this fragment.
 */
public class fragment_herramientas_user extends Fragment {

    MaterialToolbar toolbar;
    private FirebaseAuth mAuth;
    private FirebaseDatabase firebaseDB;
    private FirebaseFirestore firestore;
    ImageButton btnherramienta, btnconsumible;

    RecyclerView listView;
    HerramientasUserAdapter herramientasUserAdapter;
    private SearchView buscador;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public fragment_herramientas_user() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment fragment_herramientas_user.
     */
    // TODO: Rename and change types and number of parameters
    public static fragment_herramientas_user newInstance(String param1, String param2) {
        fragment_herramientas_user fragment = new fragment_herramientas_user();
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

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_herramientas_user, container, false);
        iniciarFireBase();
        listView = root.findViewById(R.id.custom_list_view_categoriasuser);
        buscador = root.findViewById(R.id.SearchViewHerramientasUser);
        listView = root.findViewById(R.id.custom_list_view_categoriasuser);
        listView.setLayoutManager(new LinearLayoutManager(getContext()));
        CollectionReference query = firestore.collection("Herramientas");
        FirestoreRecyclerOptions<Herramientas> FireHerramientasFirestoreRecyclerOptions =
                new FirestoreRecyclerOptions.Builder<Herramientas>().setQuery(query, Herramientas.class).build();

        herramientasUserAdapter = new HerramientasUserAdapter(FireHerramientasFirestoreRecyclerOptions);
        herramientasUserAdapter.notifyDataSetChanged();
        listView.setAdapter(herramientasUserAdapter);


        toolbar = root.findViewById(R.id.toolbarcateprodsuser);
        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setNavigationOnClickListener(view -> {
            Intent intent = new Intent(getContext(), MainActivity.class);
            startActivity(intent);
        });

        searcview();
        return root;
    }

    private void searcview(){
        buscador.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                textSearch(s);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                textSearch(s);
                return false;
            }
        });
    }

    public void textSearch(String s){
        CollectionReference consulta = firestore.collection("Herramientas");
        FirestoreRecyclerOptions<Herramientas> firestoreRecyclerOptions =
                new FirestoreRecyclerOptions.Builder<Herramientas>().
                        setQuery(consulta.orderBy("code").
                                startAt(s).endAt(s+"~"), Herramientas.class).build();

        herramientasUserAdapter = new HerramientasUserAdapter(firestoreRecyclerOptions);
        herramientasUserAdapter.startListening();
        listView.setAdapter(herramientasUserAdapter);
    }

    @Override
    public void onStart() {
        super.onStart();
        herramientasUserAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        herramientasUserAdapter.stopListening();
    }

    private void iniciarFireBase() {
        FirebaseApp.initializeApp(getContext());
        mAuth = FirebaseAuth.getInstance();
        firebaseDB = FirebaseDatabase.getInstance();
        firestore = FirebaseFirestore.getInstance();
    }
}