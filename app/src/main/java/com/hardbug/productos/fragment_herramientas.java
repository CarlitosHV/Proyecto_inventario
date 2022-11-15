package com.hardbug.productos;

import static android.app.Activity.RESULT_OK;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.hardbug.productos.model.Herramientas;
import com.hardbug.productos.model.UserType;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link fragment_herramientas#newInstance} factory method to
 * create an instance of this fragment.
 */
public class fragment_herramientas extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    MaterialToolbar toolbar;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    //VAriables propias
    private FirebaseFirestore firestore;
    private FirebaseStorage storage;
    private StorageReference storageRef;

    Button btnguardarherramienta, tomarfoto;
    public String img;
    public String rutaImagen;
    ImageView imagen;
    CheckBox consumible, herramienta;

    com.google.android.material.textfield.TextInputEditText nombre_herramienta;
    com.google.android.material.textfield.TextInputEditText descherramienta;
    com.google.android.material.textfield.TextInputEditText cantidad_herramienta;


    private Bitmap imgBitMap;

    ProgressBar loadingProgressBar;

    public fragment_herramientas() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment fragment_herramientas.
     */
    // TODO: Rename and change types and number of parameters
    public static fragment_herramientas newInstance(String param1, String param2) {
        fragment_herramientas fragment = new fragment_herramientas();
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
        View root = inflater.inflate(R.layout.fragment_herramientas, container, false);
        iniciarFireBase();
        toolbar = root.findViewById(R.id.toolbaragregarherra);
        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setNavigationOnClickListener(view -> {
            fragment_categorias categorias = new fragment_categorias();
            FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.content, categorias, "");
            fragmentTransaction.setReorderingAllowed(true);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        });

        loadingProgressBar = root.findViewById(R.id.loadingCH);
        consumible = root.findViewById(R.id.checkconsumible);
        herramienta = root.findViewById(R.id.checkherramienta);
        nombre_herramienta = root.findViewById(R.id.nombre_herramienta);
        descherramienta = root.findViewById(R.id.descherramienta);
        cantidad_herramienta = root.findViewById(R.id.cantidad_herramienta);
        imagen = root.findViewById(R.id.ivFotoherra);
        tomarfoto = root.findViewById(R.id.btntomarfotoherramienta);

        herramienta.setChecked(true);

        consumible.setOnClickListener(View -> {
            if (consumible.isChecked()){
                herramienta.setChecked(false);
            }
        });

        herramienta.setOnClickListener(View -> {
            if (herramienta.isChecked()){
                consumible.setChecked(false);
            }
        });

        tomarfoto.setOnClickListener(View -> {
            abrirCamara();
        });

        btnguardarherramienta = root.findViewById(R.id.btnguardarherramienta);
        btnguardarherramienta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String code = "";
                String description = "";
                Date fecha =  new Date();
                int count = 0;
                code = nombre_herramienta.getText().toString();
                description = descherramienta.getText().toString();
                count = Integer.parseInt(cantidad_herramienta.getText().toString());

                Herramientas herramientaNueva = new Herramientas(code, description, fecha, count);

                if(herramienta.isChecked()){
                    nuevaHerramienta(herramientaNueva,"Herramientas");
                }else{
                    nuevaHerramienta(herramientaNueva,"Consumibles");
                }

            }
        });
        return root;
    }

    private void nuevaHerramienta( Herramientas herramientaNueva, String collection){
        loadingProgressBar.setVisibility(View.VISIBLE);
        firestore.collection(collection)
                .add(herramientaNueva)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        nombre_herramienta.setText("");
                        descherramienta.setText("");
                        cantidad_herramienta.setText("");

                        SubirImagen(documentReference.getId());
                        imagen.setImageBitmap(null);
                        loadingProgressBar.setVisibility(View.GONE);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getContext(), "Error en el registro",
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void SubirImagen(String id){
        StorageReference imagensr = storageRef.child(id+".jpg");
        StorageReference ImagesRefsr = storageRef.child("images/"+id+".jpg");

        imagen.setDrawingCacheEnabled(true);
        imagen.buildDrawingCache();
        Bitmap bitmap = ((BitmapDrawable) imagen.getDrawable()).getBitmap();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        UploadTask uploadTask = imagensr.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
                // ...
            }
        });


    }

    private void abrirCamara() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
            File imagenArchivo = null;
            try {
                imagenArchivo = crearImagen();
            } catch (IOException ex) {
                Log.e("Error ", ex.toString());
            }

            if (imagenArchivo != null) {
                Uri fotoUri = FileProvider.getUriForFile(getContext(), "com.hardbug.productos.fileprovider", imagenArchivo);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, fotoUri);
                startActivityForResult(intent, 1);
            }
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            //Bundle extras = data.getExtras();
            imgBitMap = BitmapFactory.decodeFile(rutaImagen);
            imagen.setImageBitmap(imgBitMap);
            imagen.setRotation(90);

        }
    }

    private File crearImagen() throws IOException {
        String nombreImagen = "producto_";
        File directorio = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File imagen = File.createTempFile(nombreImagen, ".jpg", directorio);
        rutaImagen = imagen.getAbsolutePath();
        img = rutaImagen;
        return imagen;
    }

    private void iniciarFireBase(){
        FirebaseApp.initializeApp(getContext());
        firestore = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference();
    }
}