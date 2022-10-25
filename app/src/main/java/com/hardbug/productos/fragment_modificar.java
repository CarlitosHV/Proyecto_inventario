package com.hardbug.productos;

import static android.app.Activity.RESULT_OK;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.appbar.MaterialToolbar;
import com.hardbug.productos.bd.SQLITE;

import java.io.File;
import java.io.IOException;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link fragment_modificar#newInstance} factory method to
 * create an instance of this fragment.
 */
public class fragment_modificar extends Fragment {

    EditText ID, nombre, categoria, descripcion;
    ImageView foto;
    Button actualizar, buscar, tomarfoto;
    String img = "";
    String rutaImagen;
    SQLITE sqlite;
    MaterialToolbar materialToolbar;
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
        sqlite = new SQLITE(getContext());

        materialToolbar = root.findViewById(R.id.toolbarmodificar);
        materialToolbar.setNavigationIcon(R.drawable.ic_back);
        materialToolbar.setNavigationOnClickListener(view -> {
            Intent intent = new Intent(getContext(), MainActivity.class);
            startActivity(intent);
        });

        materialToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.barmostrarcreditos:
                        new AlertDialog.Builder(getContext()).setTitle("Acerca de").setMessage("" + "Carlos Alberto Hernández Velázquez Catador de chichis \n"+
                                "Gerardo Pastrana Gómez \n" + "Profesora: Rocío Elizabeth Pulido Alba\n" + "Programación Android :D \n" + "Aplicación Material Design + SQLite\n" + "Versión 2.1").setPositiveButton("Aceptar", null).show();
                        return true;

                    case R.id.bartopfind:
                        buscar Buscar = new buscar();
                        abrirfragments(Buscar);
                        return true;
                    case R.id.bartopeliminar:
                        fragment_eliminar eliminar = new fragment_eliminar();
                        abrirfragments(eliminar);
                        return true;

                    default:
                        return false;
                }
            }
        });

        buscar.setOnClickListener(View -> {
            if (ID.getText().toString().isEmpty()) {
                Toast.makeText(getContext(), "Ingrese el ID del producto", Toast.LENGTH_SHORT).show();
            } else {
                sqlite.abrir();
                int idp = Integer.parseInt(ID.getText().toString());
                if (sqlite.getValor(idp).getCount() == 1) {
                    Cursor cursor = sqlite.getValor(idp);
                    if (cursor.moveToFirst()) {
                        do {
                            nombre.setText(cursor.getString(1));
                            categoria.setText(cursor.getString(2));
                            descripcion.setText(cursor.getString(3));
                            img = cursor.getString(5);
                            Bitmap imgBitMap = BitmapFactory.decodeFile(img);
                            foto.setImageBitmap(imgBitMap);
                            habilitarbotones();
                        } while (cursor.moveToNext());
                    }
                } else {
                    Toast.makeText(getContext(), "Error al cargar la información", Toast.LENGTH_SHORT).show();
                }
                sqlite.cerrar();
            }
        });


        actualizar.setOnClickListener(view -> {
            if (nombre.getText().equals("") || ID.getText().equals("") || categoria.getText().equals("")
                    || descripcion.getText().equals("") || img.equals("")) {
                Toast.makeText(getContext(), "Campos vacíos o imagen del producto no cargada", Toast.LENGTH_SHORT).show();
            } else {
                int id = Integer.parseInt(ID.getText().toString());
                String Nombre = nombre.getText().toString();
                String Categoria = categoria.getText().toString();
                String Descripcion = descripcion.getText().toString();
                boolean estatus = true;
                sqlite.abrir();
                String actualizacion = sqlite.updateRegistroProducto(id, Nombre, Categoria, Descripcion, estatus, img);
                if (actualizacion.contentEquals("Producto actualizado con éxito")) {
                    limpiar();
                    Toast.makeText(getContext(), "Información guardada con éxito", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), "Error al guardar la información", Toast.LENGTH_SHORT).show();
                }
                sqlite.cerrar();
            }
        });


        tomarfoto.setOnClickListener(View -> {
            abrirCamara();
        });
        return root;
    }

    public void Botones(View root) {
        ID = root.findViewById(R.id.idprodmodificar);
        nombre = root.findViewById(R.id.nombreprodmodificar);
        categoria = root.findViewById(R.id.categoriaprodmodificar);
        descripcion = root.findViewById(R.id.descriptionprodmodificar);
        actualizar = root.findViewById(R.id.btnguardarmodificar);
        foto = root.findViewById(R.id.ivFotoprodmodificar);
        buscar = root.findViewById(R.id.btnmodificarbuscar);
        tomarfoto = root.findViewById(R.id.btntomarfotomodificar);
        nombre.setEnabled(false);
        tomarfoto.setEnabled(false);
        categoria.setEnabled(false);
        descripcion.setEnabled(false);
    }

    public void habilitarbotones(){
        nombre.setEnabled(true);
        tomarfoto.setEnabled(true);
        categoria.setEnabled(true);
        descripcion.setEnabled(true);
    }

    public void limpiar(){
        ID.setText("");
        nombre.setText("");
        categoria.setText("");
        descripcion.setText("");
        img = "";
        Bitmap imgBitMap = BitmapFactory.decodeFile(img);
        foto.setImageBitmap(imgBitMap);
        nombre.setEnabled(false);
        tomarfoto.setEnabled(false);
        categoria.setEnabled(false);
        descripcion.setEnabled(false);
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

}

