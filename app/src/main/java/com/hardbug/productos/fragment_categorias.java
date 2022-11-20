package com.hardbug.productos;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
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

    ListView listView;
    private List<Herramientas> ListarHerramientas = new ArrayList<>();


    Boolean banderah = false, banderac = false;
    ArrayList<String> listaHerramientas = new ArrayList<String>();
    ArrayList<ListaHerramientas> listah = new ArrayList<ListaHerramientas>();
    ArrayList<ListaHerramientas> listac = new ArrayList<ListaHerramientas>();
    ArrayList<ListaHerramientas> listageneral = new ArrayList<ListaHerramientas>();
    ArrayList<Herramientas> herramientas = new ArrayList<>();
    ArrayList<Herramientas> consumibles = new ArrayList<>();
    ArrayList<Herramientas> generales = new ArrayList<>();

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

    private ArrayList<ListaHerramientas> seticonandname(){
        herramientas = new ArrayList<>();
        for (int i = 0; i < herramientas.size(); i++){
            //cargarImagen(imagenes.get(i));
            listah.add(new ListaHerramientas(herramientas.get(i).getCode()));
        }
        return listah;
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
        LLamarHerramienta();
        LLamarConsumible();
        listView = root.findViewById(R.id.custom_list_view_categorias);
        btnconsumible = root.findViewById(R.id.btnConsumibles);
        btnherramienta = root.findViewById(R.id.btnherramientas);
        CustomAdapterCategorias customAdapter = new CustomAdapterCategorias(getContext(), listageneral);
        listView.setAdapter(customAdapter);
        listView.setOnItemClickListener(this::onItemClick);

        toolbar = root.findViewById(R.id.toolbarcateprods);
        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setNavigationOnClickListener(view -> {
            Intent intent = new Intent(getContext(), MainActivity.class);
            startActivity(intent);
        });

        btnherramienta.setOnClickListener(View -> {
            CustomAdapterCategorias adapterHerramientas = new CustomAdapterCategorias(getContext(), listah);
            listView.setAdapter(adapterHerramientas);
        });

        btnconsumible.setOnClickListener(View -> {
            CustomAdapterCategorias adapterConsumibles = new CustomAdapterCategorias(getContext(), listac);
            listView.setAdapter(adapterConsumibles);
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



        return root;
    }

    public void LLamarHerramienta() {
        firestore.collection("Herramientas")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Map<String, Object> obj = document.getData();
                                Herramientas herramientasM = new Herramientas();
                                herramientasM.setCode(obj.get("code").toString());
                                herramientasM.setCount(Integer.parseInt(obj.get("count").toString()));
                                herramientasM.setDescripcion(obj.get("descripcion").toString());
                                //Date FECHA = new Date(obj.get("fecha")+"");
                                //herramientasM.setFecha(FECHA);
                                herramientas.add(herramientasM);
                                generales.add(herramientasM);
                            }
                            for (int y = 0; y < herramientas.size(); y++){
                                listah.add(new ListaHerramientas(herramientas.get(y).getCode()));
                                listageneral.add(new ListaHerramientas(herramientas.get(y).getCode()));
                            }
                            CustomAdapterCategorias customAdapter = new CustomAdapterCategorias(getContext(), listageneral);
                            listView.setAdapter(customAdapter);
                        }
                    }
                });
    }

    public void LLamarConsumible() {
        firestore.collection("Consumibles")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Map<String, Object> obj = document.getData();
                                Herramientas herra = new Herramientas();
                                herra.setCode(obj.get("code").toString());
                                herra.setCount(Integer.parseInt(obj.get("count").toString()));
                                herra.setDescripcion(obj.get("descripcion").toString());
                                //Date FECHA = new Date(obj.get("fecha")+"");
                                //herramientasM.setFecha(FECHA);
                                consumibles.add(herra);
                                generales.add(herra);
                            }
                            for (int y = 0; y < consumibles.size(); y++){
                                listac.add(new ListaHerramientas(consumibles.get(y).getCode()));
                                listageneral.add(new ListaHerramientas(consumibles.get(y).getCode()));
                            }
                            CustomAdapterCategorias customAdapter = new CustomAdapterCategorias(getContext(), listageneral);
                            listView.setAdapter(customAdapter);
                        }
                    }
                });
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