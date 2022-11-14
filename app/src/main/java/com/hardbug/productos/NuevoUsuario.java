package com.hardbug.productos;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.hardbug.productos.model.UserType;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class NuevoUsuario extends AppCompatActivity {

    private static final String TAG = "EmailPassword";
    private FirebaseAuth mAuth;

    private FirebaseDatabase firebaseDB;
    private FirebaseFirestore firestore;

    private com.google.android.material.textfield.TextInputEditText usuario;
    private com.google.android.material.textfield.TextInputEditText name;
    private com.google.android.material.textfield.TextInputEditText contra;
    private com.google.android.material.textfield.TextInputEditText confirmarcontrasenia;
    private Button btnregistro;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nuevo_usuario);

        iniciarFireBase();

        usuario = findViewById(R.id.campousuarioNU);
        contra = findViewById(R.id.contraNU);
        confirmarcontrasenia = findViewById(R.id.confirmarcontraseniaNU);
        name = findViewById(R.id.usuarioNU);
        btnregistro = findViewById(R.id.btnregistroNU);

        btnregistro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validarEmail(usuario.getText().toString())){
                    if(contra.getText().toString().compareTo(confirmarcontrasenia.getText().toString())==0){
                        crearUsuario();
                    }else{
                        Toast.makeText(getBaseContext(),"Las contraseñas no son iguales", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(getBaseContext(),"Email no valido", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void crearUsuario(){
        mAuth.createUserWithEmailAndPassword(usuario.getText().toString(), contra.getText().toString())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            UserType usuarioNuevo = new UserType(usuario.getText().toString(), false, user.getUid(), name.getText().toString());
                            nuevoUsuario(usuarioNuevo);
                            Toast.makeText(NuevoUsuario.this, "Registro Exitoso",
                                    Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
                            Toast.makeText(NuevoUsuario.this, "Error en el registro",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void nuevoUsuario(UserType usuarioNuevo){
        firestore.collection("UsersType")
                .add(usuarioNuevo)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        //
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(NuevoUsuario.this, "Error en el registro",
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private boolean validarEmail(String email){
        Pattern pattern = Patterns.EMAIL_ADDRESS;
        return pattern.matcher(email).matches();
    }

    private void iniciarFireBase(){
        FirebaseApp.initializeApp(this);
        mAuth = FirebaseAuth.getInstance();
        firebaseDB = FirebaseDatabase.getInstance();
        firestore = FirebaseFirestore.getInstance();
    }
}