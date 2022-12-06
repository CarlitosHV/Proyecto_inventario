package com.hardbug.productos;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SearchView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.hardbug.productos.adapters.RegistrosAdapter;
import com.hardbug.productos.design.CustomAdapterHerramientas;
import com.hardbug.productos.model.Herramientas;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
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
    EditText fechap, fechad;
    String FechaPrestamo;
    String FechaDevolucion;
    Context context;


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    //Variables de uso

    private FirebaseDatabase firebaseDB;
    private FirebaseFirestore firestore;

    private RecyclerView listaHerramientas;
    private RegistrosAdapter registrosAdapter;
    private ListView listaConsumibles;
    private SearchView buscador;

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
        listaHerramientas.setLayoutManager(new LinearLayoutManager(getContext()));
        iniciarFireBase();
        CollectionReference query = firestore.collection("Herramientas");
        FirestoreRecyclerOptions<Herramientas> FireHerramientasFirestoreRecyclerOptions =
                new FirestoreRecyclerOptions.Builder<Herramientas>().setQuery(query, Herramientas.class).build();

        registrosAdapter = new RegistrosAdapter(FireHerramientasFirestoreRecyclerOptions);
        registrosAdapter.notifyDataSetChanged();
        listaHerramientas.setAdapter(registrosAdapter);

        fechap = root.findViewById(R.id.fecha_prestamo_registro);
        fechap.setEnabled(false);
        fechad = root.findViewById(R.id.fecha_devuelto);
        fechad.setEnabled(false);

        String DATE_FORMAT = "MMM dd yyyy";
        DateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
        Calendar cal = Calendar.getInstance();
        Date currentDate = new Date();

        String FECHA_PRESTAMO = dateFormat.format(currentDate);
        fechap.setText(FECHA_PRESTAMO);
        cal.setTime(currentDate);
        cal.add(Calendar.DAY_OF_MONTH, 7);
        Date currentDateDev = cal.getTime();
        String FECHA_DEVOLUCION = dateFormat.format(currentDateDev);
        fechad.setText(FECHA_DEVOLUCION);


        buscador = root.findViewById(R.id.SearchViewRegistros);
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

        registrosAdapter = new RegistrosAdapter(firestoreRecyclerOptions);
        registrosAdapter.startListening();
        listaHerramientas.setAdapter(registrosAdapter);
    }

    @Override
    public void onStart() {
        super.onStart();
        registrosAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        registrosAdapter.stopListening();
    }
}