package com.hardbug.productos;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class Menu_Admin extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_admin);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
    }
}