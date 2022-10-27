package com.hardbug.productos;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    Button login;
    EditText usuario, contrasenia;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        login = findViewById(R.id.btnlog);
        usuario = findViewById(R.id.campousuario);
        contrasenia = findViewById(R.id.campocontra);
        login.setOnClickListener(view -> {
            if (usuario.getText().toString().equals("") || contrasenia.getText().toString().equals("")){
                Toast.makeText(this, "Ingresa la información", Toast.LENGTH_SHORT).show();
            }else{
                if (usuario.getText().toString().equals("a") && contrasenia.getText().toString().equals("a")){
                    Intent intent = new Intent(view.getContext(), fragment_principal.class);
                    startActivity(intent);
                }
            }
        });
    }
}