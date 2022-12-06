package com.hardbug.productos;

import static android.app.Activity.RESULT_OK;

import android.annotation.SuppressLint;
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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.hardbug.productos.model.Herramientas;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link fragment_modificar#newInstance} factory method to
 * create an instance of this fragment.
 */
public class fragment_modificar extends Fragment {

    EditText ID, nombre, categoria, descripcion, cantidad;
    ImageView foto;
    Button actualizar, buscar, tomarfoto;
    String img = "";
    String rutaImagen;
    String id_herramienta;
    ProgressBar loadingbar;
    MaterialToolbar materialToolbar;
    private FirebaseFirestore firestore;
    private FirebaseStorage storage;
    private StorageReference storageRef;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public fragment_modificar() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment fragment_modificar.
     */
    // TODO: Rename and change types and number of parameters
    public static fragment_modificar newInstance(String param1, String param2) {
        fragment_modificar fragment = new fragment_modificar();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_modificar, container, false);
        Botones(root);

        if (getArguments() != null){
            id_herramienta = getArguments().getString("id_herramienta");
            ID.setText(id_herramienta);
            ID.setEnabled(false);
        }

        materialToolbar = root.findViewById(R.id.toolbarmodificar);
        materialToolbar.setNavigationIcon(R.drawable.ic_back);
        materialToolbar.setNavigationOnClickListener(view -> {
            fragment_categorias cat = new fragment_categorias();
            FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.content, cat, "");
            fragmentTransaction.setReorderingAllowed(true);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        });

        actualizar.setOnClickListener(view -> {
            Map <String, Object> map = new HashMap<>();
            String code = nombre.getText().toString().trim();
            map.put("code", code);
            String description = descripcion.getText().toString().trim();
            map.put("descripcion", description);
            int count = Integer.parseInt(cantidad.getText().toString().trim());
            map.put("count", count);
            Date fecha =  new Date();
            map.put("fecha", fecha);

            Herramientas herramientaNueva = new Herramientas(code, description, fecha, count);
            updateHerramienta(herramientaNueva,"Herramientas", map);
        });



        tomarfoto.setOnClickListener(View -> {
            abrirCamara();
        });
        return root;
    }

    private void updateHerramienta(Herramientas herramientaNueva, String collection, Map map){
        iniciarFireBase();
        loadingbar.setVisibility(View.VISIBLE);
        firestore.collection(collection)
                .document(id_herramienta).update(map)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        limpiar();
                        //SubirImagen(id_herramienta);
                        loadingbar.setVisibility(View.GONE);
                        Toast.makeText(getContext(), "Herramienta editada con éxito",
                                Toast.LENGTH_SHORT).show();
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

        foto.setDrawingCacheEnabled(true);
        foto.buildDrawingCache();
        Bitmap bitmap = ((BitmapDrawable) foto.getDrawable()).getBitmap();
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


    public void Botones(View root) {
        ID = root.findViewById(R.id.idprodmodificar);
        nombre = root.findViewById(R.id.nombreprodmodificar);
        descripcion = root.findViewById(R.id.descrprodmodificar);
        actualizar = root.findViewById(R.id.btnguardarmodificar);
        foto = root.findViewById(R.id.ivFotoprodmodificar);
        tomarfoto = root.findViewById(R.id.btntomarfotomodificar);
        cantidad = root.findViewById(R.id.cantidadprodmodificar);
        loadingbar = root.findViewById(R.id.loadingMH);
    }



    public void limpiar(){
        ID.setText("");
        nombre.setText("");
        descripcion.setText("");
        cantidad.setText("");
        img = "";
        Bitmap imgBitMap = BitmapFactory.decodeFile(img);
        foto.setImageBitmap(imgBitMap);
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
            Bitmap imgBitMap = BitmapFactory.decodeFile(rutaImagen);
            foto.setImageBitmap(imgBitMap);

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



    public void abrirfragments(Fragment fragment){
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.content, fragment, "");
        fragmentTransaction.setReorderingAllowed(true);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    private void iniciarFireBase(){
        FirebaseApp.initializeApp(getContext());
        firestore = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference();
    }

}

