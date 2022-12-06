package com.hardbug.productos;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.hardbug.productos.adapters.HerramientasAdapter;
import com.hardbug.productos.adapters.RegistrosAdapter;
import com.hardbug.productos.design.CustomAdapter;
import com.hardbug.productos.design.CustomAdapterCategorias;
import com.hardbug.productos.model.Consumibles;
import com.hardbug.productos.model.Herramientas;
import com.hardbug.productos.model.ListaHerramientas;
import com.hardbug.productos.model.ListaUsers;
import com.hardbug.productos.model.UserType;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link fragment_categorias#newInstance} factory method to
 * create an instance of this fragment.
 */
public class fragment_categorias extends Fragment implements AdapterView.OnItemClickListener{

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    FloatingActionButton add;

    MaterialToolbar toolbar;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private FirebaseAuth mAuth;
    private FirebaseDatabase firebaseDB;
    private FirebaseFirestore firestore;
    ImageButton btnherramienta, btnconsumible;

    RecyclerView listView;
    HerramientasAdapter herramientasAdapter;
    private SearchView buscador;
    private List<Herramientas> ListarHerramientas = new ArrayList<>();


    public fragment_categorias() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment fragment_categorias.
     */
    // TODO: Rename and change types and number of parameters
    public static fragment_categorias newInstance(String param1, String param2) {
        fragment_categorias fragment = new fragment_categorias();
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
        View root = inflater.inflate(R.layout.fragment_categorias, container, false);
        iniciarFireBase();
        listView = root.findViewById(R.id.custom_list_view_categorias);
        buscador = root.findViewById(R.id.SearchViewHerramientas);
        listView = root.findViewById(R.id.custom_list_view_categorias);
        listView.setLayoutManager(new LinearLayoutManager(getContext()));
        CollectionReference query = firestore.collection("Herramientas");
        FirestoreRecyclerOptions<Herramientas> FireHerramientasFirestoreRecyclerOptions =
                new FirestoreRecyclerOptions.Builder<Herramientas>().setQuery(query, Herramientas.class).build();

        herramientasAdapter = new HerramientasAdapter(FireHerramientasFirestoreRecyclerOptions, getActivity());
        herramientasAdapter.notifyDataSetChanged();
        listView.setAdapter(herramientasAdapter);


        toolbar = root.findViewById(R.id.toolbarcateprods);
        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setNavigationOnClickListener(view -> {
            Intent intent = new Intent(getContext(), MainActivity.class);
            startActivity(intent);
        });

        add = root.findViewById(R.id.fabcategorias);
        add.setOnClickListener(view -> {
            fragment_herramientas herramientas = new fragment_herramientas();
            FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.content, herramientas, "");
            fragmentTransaction.setReorderingAllowed(true);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
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

        herramientasAdapter = new HerramientasAdapter(firestoreRecyclerOptions, getActivity());
        herramientasAdapter.startListening();
        listView.setAdapter(herramientasAdapter);
    }

    @Override
    public void onStart() {
        super.onStart();
        herramientasAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        herramientasAdapter.stopListening();
    }

    private void iniciarFireBase() {
        FirebaseApp.initializeApp(getContext());
        mAuth = FirebaseAuth.getInstance();
        firebaseDB = FirebaseDatabase.getInstance();
        firestore = FirebaseFirestore.getInstance();
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Toast.makeText(getContext(), "Hola", Toast.LENGTH_SHORT).show();
    }
}