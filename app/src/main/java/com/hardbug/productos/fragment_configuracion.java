package com.hardbug.productos;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.hardbug.productos.model.UserType;

import java.util.regex.Pattern;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link fragment_configuracion#newInstance} factory method to
 * create an instance of this fragment.
 */
public class fragment_configuracion extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    MaterialToolbar toolbar;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    //Variables propias
    private FirebaseAuth mAuth;
    private FirebaseFirestore firestore;
    Button btnconfiguracionAceptar;

    com.google.android.material.textfield.TextInputEditText nuevo_nombre;
    com.google.android.material.textfield.TextInputEditText nuevo_correo;
    com.google.android.material.textfield.TextInputEditText nueva_contrasenia;
    com.google.android.material.textfield.TextInputEditText confirmar_contrasenia;

    ProgressBar loadingProgressBar;


    public fragment_configuracion() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment fragment_configuracion.
     */
    // TODO: Rename and change types and number of parameters
    public static fragment_configuracion newInstance(String param1, String param2) {
        fragment_configuracion fragment = new fragment_configuracion();
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
        View root = inflater.inflate(R.layout.fragment_configuracion, container, false);
        iniciarFireBase();
        toolbar = root.findViewById(R.id.toolbarconfiguracion);
        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setNavigationOnClickListener(view -> {
            Intent intent = new Intent(getContext(), MainActivity.class);
            startActivity(intent);
        });

        loadingProgressBar = root.findViewById(R.id.loadingConfig);
        nuevo_nombre = root.findViewById(R.id.nombre_cliente);
        nuevo_correo = root.findViewById(R.id.correo_cliente);
        nueva_contrasenia = root.findViewById(R.id.password_cliente);
        confirmar_contrasenia = root.findViewById(R.id.confirmarpassword_cliente);

        btnconfiguracionAceptar = root.findViewById(R.id.btnconfiguracionAceptar);

        btnconfiguracionAceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validarEmail(nuevo_correo.getText().toString())){
                    if(nueva_contrasenia.getText().toString().compareTo(confirmar_contrasenia.getText().toString())==0){
                        ModificarUsuario();
                    }else{
                        Toast.makeText(getContext(),"Las contraseñas no son iguales", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(getContext(),"Email no valido", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return root;
    }






    private void ModificarUsuario(UserType modificarUsuario){
        firestore.collection("UsersType")
                .whereEqualTo("id",MainActivity.UserSys.getID())
                .add(modificarUsuario)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        //



                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getContext(), "Error en el registro",
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }














    private boolean validarEmail(String email){
        Pattern pattern = Patterns.EMAIL_ADDRESS;
        return pattern.matcher(email).matches();
    }


    private void iniciarFireBase(){
        FirebaseApp.initializeApp(getContext());
        firestore = FirebaseFirestore.getInstance();
    }

}