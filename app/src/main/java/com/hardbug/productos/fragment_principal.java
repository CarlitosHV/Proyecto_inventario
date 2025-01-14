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

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setItemHorizontalTranslationEnabled(true);

        if (MainActivity.UserSys.isTipo()){
            bottomNavigationView.inflateMenu(R.menu.menu_admin);
            //Fragment que siempre va a cargar en pantalla principal
            fragment_categorias cate = new fragment_categorias();
            abrirfragments(cate);
        }else{
            bottomNavigationView.inflateMenu(R.menu.menu_usuario);
            fragment_herramientas_user herra = new fragment_herramientas_user();
            abrirfragments(herra);
        }




        bottomNavigationView.setOnItemSelectedListener(item -> {
            fragment_configuracion config = new fragment_configuracion();
            fragment_solicitudes sol = new fragment_solicitudes();
            switch (item.getItemId()) {
                //Casos para los fragmentos del admin
                case R.id.menuadmincategorias:
                    fragment_categorias cats = new fragment_categorias();
                    abrirfragments(cats);
                    return true;
                case R.id.menuadminconfiguracion:
                    abrirfragments(config);
                    return true;
                case R.id.menuadminusuarios:
                    fragment_usuarios usuarios = new fragment_usuarios();
                    abrirfragments(usuarios);
                    return true;
                    //Casos para el usuario
                case R.id.menuuserprestamos:
                    fragment_herramientas_user user = new fragment_herramientas_user();
                    abrirfragments(user);
                    return true;
                case R.id.menuusuarioconfiguracion:
                    abrirfragments(config);
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