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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_fragment);

        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setItemHorizontalTranslationEnabled(true);


        fragment_home home = new fragment_home();
        FragmentTransaction fragmentTransaction1 = getSupportFragmentManager().beginTransaction();
        fragmentTransaction1.replace(R.id.content, home, "");
        fragmentTransaction1.addToBackStack(null);
        fragmentTransaction1.commit();

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()) {

                    case R.id.barAnadir:
                        Agregar agregar = new Agregar();
                        abrirfragments(agregar);
                        return true;
                    case R.id.barhome:
                        fragment_home home = new fragment_home();
                        abrirfragments(home);
                        return true;
                    case R.id.barModify:
                        fragment_modificar modificar = new fragment_modificar();
                        abrirfragments(modificar);
                        return true;
                    case R.id.barmostraractivos:
                        listaractivos Listaractivos = new listaractivos();
                        abrirfragments(Listaractivos);
                        return true;
                    case R.id.barmostrarinactivos:
                        listarinactivos Listarinactivos = new listarinactivos();
                        abrirfragments(Listarinactivos);
                }
                return false;
            }
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
                //metodoAdd()

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