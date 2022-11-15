package com.hardbug.productos;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.hardbug.productos.model.UserType;

import java.util.Map;

public class MainActivity extends AppCompatActivity {

    Button login;
    Button btnNuevoUsuario;
    EditText usuario, contrasenia;
    TextView olvidasteContra;
    ProgressBar loadingProgressBar;
    private FirebaseAuth mAuth;

    public static UserType UserSys;

    private FirebaseDatabase firebaseDB;
    private FirebaseFirestore firestore;

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        signOut();
        FirebaseUser currentUser = mAuth.getCurrentUser();
    }

    public void signOut() {
        // [START auth_fui_signout]
        mAuth.getInstance().signOut();
        MainActivity.UserSys = new UserType();
        // [END auth_fui_signout]
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        login = findViewById(R.id.btnlog);
        usuario = findViewById(R.id.campousuario);
        contrasenia = findViewById(R.id.campocontra);
        loadingProgressBar = findViewById(R.id.loading);

        olvidasteContra = findViewById(R.id.btnOlvidePass);
        iniciarFireBase();

        btnNuevoUsuario = findViewById(R.id.btnNuevoUsuario);

        btnNuevoUsuario.setOnClickListener(view -> {
            Intent intent = new Intent(view.getContext(), NuevoUsuario.class);
            startActivity(intent);
        });


        olvidasteContra.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, olvideContra.class);
                startActivity(intent);
                finish();
            }
        });


        login.setOnClickListener(view -> {
            if (usuario.getText().toString().equals("") || contrasenia.getText().toString().equals("")) {
                Toast.makeText(this, "Ingresa la información", Toast.LENGTH_SHORT).show();
            }else{
                loadingProgressBar.setVisibility(View.VISIBLE);
                mAuth.signInWithEmailAndPassword(usuario.getText().toString().trim(), contrasenia.getText().toString())
                        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information

                                    FirebaseUser user = mAuth.getCurrentUser();
                                    LLamarUsuario(user.getUid());
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


    private void LLamarUsuario(String id) {
        firestore.collection("UsersType")
                .whereEqualTo("id", id)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Map<String, Object> obj = document.getData();
                                MainActivity.UserSys = new UserType();
                                MainActivity.UserSys.setEmail(obj.get("email").toString());
                                MainActivity.UserSys.setID(obj.get("id").toString());
                                MainActivity.UserSys.setTipo((Boolean) obj.get("tipo"));
                                MainActivity.UserSys.setName(obj.get("name").toString());
                            }
                        }
                        if (MainActivity.UserSys != null) {
                            Intent intent = new Intent(MainActivity.this, fragment_principal.class);
                            startActivity(intent);
                        }
                    }
                });
    }

    private void iniciarFireBase() {
        FirebaseApp.initializeApp(this);
        mAuth = FirebaseAuth.getInstance();
        firebaseDB = FirebaseDatabase.getInstance();
        firestore = FirebaseFirestore.getInstance();
    }

}