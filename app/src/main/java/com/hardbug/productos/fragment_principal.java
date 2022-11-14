package com.hardbug.productos;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Context;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class fragment_principal extends AppCompatActivity {

    Context context = this;
    BottomNavigationView bottomNavigationView;
    Boolean prueba = true;

    protected void onStart() {
        Toast.makeText(this, MainActivity.UserSys.getName() + " bienvenido", Toast.LENGTH_SHORT).show();
        super.onStart();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_fragment);

        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setItemHorizontalTranslationEnabled(true);

        if (MainActivity.UserSys.isTipo()){
            bottomNavigationView.inflateMenu(R.menu.menu_admin);
            //Fragment que siempre va a cargar en pantalla principal
            fragment_categorias cate = new fragment_categorias();
            abrirfragments(cate);
        }else{
            bottomNavigationView.inflateMenu(R.menu.menu_usuario);
            fragment_prestamos presta= new fragment_prestamos();
            abrirfragments(presta);
        }




        bottomNavigationView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                //Casos para los fragmentos del admin
                case R.id.menuadmincategorias:
                    fragment_categorias cats = new fragment_categorias();
                    abrirfragments(cats);
                    return true;
                case R.id.menuadminconfiguracion:
                    fragment_configuracion config = new fragment_configuracion();
                    abrirfragments(config);
                    return true;
                case R.id.menuadminusuarios:
                    fragment_usuarios usuarios = new fragment_usuarios();
                    abrirfragments(usuarios);
                    return true;
                case R.id.menuadminregistros:
                    fragment_registros registros = new fragment_registros();
                    abrirfragments(registros);
                    return true;
                case R.id.barmostrarinactivos:
                    listarinactivos Listarinactivos = new listarinactivos();
                    abrirfragments(Listarinactivos);
                    return true;
                    //Casos para el usuario
                case R.id.menuuserprestamos:
                    fragment_prestamos prestamos = new fragment_prestamos();
                    abrirfragments(prestamos);
                    return true;
            }
            return false;
        });

    }

    public void abrirfragments(Fragment fragment){
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.content, fragment, "");
        fragmentTransaction.setReorderingAllowed(true);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.bartopfind:
                Toast.makeText(context, "Se ha presionado", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }


    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu2, menu);
        return true;
    }
}