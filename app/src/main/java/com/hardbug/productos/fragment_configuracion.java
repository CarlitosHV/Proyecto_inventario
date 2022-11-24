package com.hardbug.productos;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.provider.SyncStateContract;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.internal.Constants;
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
public class fragment_configuracion extends Fragment implements LocationListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    MaterialToolbar toolbar;
    TextView coordenadas;
    protected LocationManager locationManager;
    protected LocationListener locationListener;
    protected Context context;
    static int LOCATION_PERMISSION_REQUEST = 15;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    //Variables propias
    private FirebaseAuth mAuth;
    private FirebaseFirestore firestore;
    FirebaseUser user;

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
        coordenadas = root.findViewById(R.id.coordenadas);
        toolbar = root.findViewById(R.id.toolbarconfiguracion);
        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setNavigationOnClickListener(view -> {
            Intent intent = new Intent(getContext(), MainActivity.class);
            startActivity(intent);
        });

        locationManager = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);
        if (checkLocationPermission(getActivity())){
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
        }
        nuevo_correo = root.findViewById(R.id.nombre_cliente);/////
        nueva_contrasenia = root.findViewById(R.id.confirmarpassword_cliente);/////


        btnconfiguracionAceptar = root.findViewById(R.id.btnconfiguracionAceptar);
        btnconfiguracionAceptar.setOnClickListener(View ->{
            updatePasword();
            updateEmail();
        });



        return root;
    }



    public void updateEmail() {
        String email = nuevo_correo.getText().toString().trim();
        UserProfileChangeRequest perfil = new UserProfileChangeRequest.Builder().build();
        if(email.length() > 0){
            user.updateEmail(email)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful ()) {
                                Toast.makeText (getContext(), "Mail Updated", Toast.LENGTH_SHORT).show ();
                                MainActivity.UserSys.setEmail(email);
                                firestore.collection("UsersType").document(MainActivity.UserSys.getIDD())
                                        .set(MainActivity.UserSys)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {

                                            }
                                        });
                            } else {
                                Exception e = task.getException();
                                Toast.makeText (getContext(), "Error updating email: "+e.getMessage(), Toast.LENGTH_SHORT).show();
                                Log.w("updateEmail", "Unable to update email", e);
                            }
                        }
                    });
        }
    }

    public void updatePasword(){
        String contra = nueva_contrasenia.getText().toString().trim();
        if(contra.length() > 0){
            user.updatePassword(contra)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(getContext(), "Contraseña modificado",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
    }

    @Override
    public void onLocationChanged(Location location) {
        coordenadas.setText("Latitude:" + location.getLatitude() + ", Longitude:" + location.getLongitude());
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


    private void iniciarFireBase(){
        user = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseApp.initializeApp(getContext());
        firestore = FirebaseFirestore.getInstance();
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