package com.hardbug.productos;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
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
import com.google.firebase.auth.UserProfileChangeRequest;
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
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

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


        nuevo_correo = root.findViewById(R.id.correo_cliente);/////
        nueva_contrasenia = root.findViewById(R.id.password_cliente);/////


        btnconfiguracionAceptar = root.findViewById(R.id.btnconfiguracionAceptar);
        btnconfiguracionAceptar.setOnClickListener(View ->{
            updatePasword();
            updateEmail();
        });



        return root;
    }



    public void updateEmail() {
        user.updateEmail(nuevo_correo.getText().toString())
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(getContext(), "Correo modificado",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public void updatePasword(){
        user.updatePassword(nueva_contrasenia.getText().toString())
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(getContext(), "Contraseña modificado",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }




    private void iniciarFireBase(){
        FirebaseApp.initializeApp(getContext());
        firestore = FirebaseFirestore.getInstance();
    }

}