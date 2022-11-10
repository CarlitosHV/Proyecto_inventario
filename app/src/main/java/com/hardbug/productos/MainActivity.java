package com.hardbug.productos;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    Button login;
    EditText usuario, contrasenia;
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        login = findViewById(R.id.btnlog);
        usuario = findViewById(R.id.campousuario);
        contrasenia = findViewById(R.id.campocontra);
        mAuth = FirebaseAuth.getInstance();


        login.setOnClickListener(view -> {
            if (usuario.getText().toString().equals("") || contrasenia.getText().toString().equals("")){
                Toast.makeText(this, "Ingresa la información", Toast.LENGTH_SHORT).show();
            }else{
                mAuth.signInWithEmailAndPassword(usuario.getText().toString().trim(), contrasenia.getText().toString())
                        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    Toast.makeText(getBaseContext(), "Bienvenido",
                                            Toast.LENGTH_SHORT).show();
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    Intent intent = new Intent(view.getContext(), fragment_principal.class);
                                    startActivity(intent);
                                    //updateUI(user);
                                } else {
                                    // If sign in fails, display a message to the user.
                                    Log.w(TAG, "signInWithEmail:failure", task.getException());
                                    Toast.makeText(getBaseContext(), "Usuario no registrado",
                                            Toast.LENGTH_SHORT).show();
                                    //updateUI(null);
                                }
                            }
                        });
            }
        });
    }


}