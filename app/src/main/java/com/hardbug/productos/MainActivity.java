package com.hardbug.productos;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.color.DynamicColors;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.hardbug.productos.model.LocationsUsers;
import com.hardbug.productos.model.UserType;

import org.checkerframework.common.returnsreceiver.qual.This;

import java.util.Map;

public class MainActivity extends AppCompatActivity implements LocationListener {

    Button login;
    Button btnNuevoUsuario;
    EditText usuario, contrasenia;
    TextView olvidasteContra;
    ProgressBar loadingProgressBar;
    private FirebaseAuth mAuth;

    public static UserType UserSys;

    private FirebaseDatabase firebaseDB;
    private FirebaseFirestore firestore;

    static int LOCATION_PERMISSION_REQUEST = 15;
    protected LocationManager locationManager;
    protected LocationListener locationListener;
    private Location location;

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        signOut();
        FirebaseUser currentUser = mAuth.getCurrentUser();
    }

    public void signOut() {
        // [START auth_fui_signout]
        FirebaseAuth.getInstance().signOut();
        MainActivity.UserSys = new UserType();
        // [END auth_fui_signout]
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

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

        locationManager = (LocationManager) MainActivity.this.getSystemService(Context.LOCATION_SERVICE);
        if (checkLocationPermission(MainActivity.this)){
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
        }


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
                                MainActivity.UserSys.setIDD(document.getId());
                            }
                        }
                        if (MainActivity.UserSys != null) {
                            LocationsUsers loca = new LocationsUsers(location.getLatitude(), location.getLongitude(), MainActivity.UserSys.getID(), MainActivity.UserSys.getEmail());
                            nuevaLocacion(loca);
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

    @Override
    public void onLocationChanged(Location location) {
        //coordenadas.setText("Latitude:" + location.getLatitude() + ", Longitude:" + location.getLongitude());
        this.location = location;

    }

    private void nuevaLocacion(LocationsUsers locacion){
        firestore.collection("LocationsUsers")
                .add(locacion)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        //
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(MainActivity.this, "Error en el registro",
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    public void onProviderDisabled(String provider) {
        Log.d("Latitude","disable");
    }

    @Override
    public void onProviderEnabled(String provider) {
        Log.d("Latitude","enable");
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        Log.d("Latitude","status");
    }

    public static boolean checkLocationPermission(Activity activity){
        if(ActivityCompat.checkSelfPermission(activity, android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(activity, android.Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED){

            ActivityCompat.requestPermissions(activity, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION,
                    android.Manifest.permission.ACCESS_COARSE_LOCATION}, LOCATION_PERMISSION_REQUEST);
            return false;
        }
        return true;
    }

}