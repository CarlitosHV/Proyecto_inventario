package com.hardbug.productos;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;

import com.google.android.material.color.DynamicColors;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class Presentacion extends AppCompatActivity{
    private static final int TIME_OUT = 3000;
    private final ArrayList<Drawable> imagenes = new ArrayList<Drawable>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DynamicColors.applyToActivitiesIfAvailable(getApplication());
        setContentView(R.layout.activity_presentacion);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        imagenes.add(getResources().getDrawable(R.drawable.introuno));
        imagenes.add(getResources().getDrawable(R.drawable.introdos));
        imagenes.add(getResources().getDrawable(R.drawable.introtres));
        imagenes.add(getResources().getDrawable(R.drawable.introcuatro));
        imagenes.add(getResources().getDrawable(R.drawable.introcinco));
        imagenes.add(getResources().getDrawable(R.drawable.introseis));
        imagenes.add(getResources().getDrawable(R.drawable.instrosietee));
        imagenes.add(getResources().getDrawable(R.drawable.introocho));
        imagenes.add(getResources().getDrawable(R.drawable.intronueve));
        imagenes.add(getResources().getDrawable(R.drawable.introdiez));

        int randomNum = randomThis(0,9);

        ImageView imagen = findViewById(R.id.imgPresentacion);
        imagen.setImageDrawable(imagenes.get(randomNum));
        imagen.setScaleType(ImageView.ScaleType.FIT_XY);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i = new Intent(Presentacion.this, MainActivity.class);
                startActivity(i);
                finish();
            }
        }, TIME_OUT);

    }

    private static int randomThis(int min, int max) {

        if (min >= max) {
            throw new IllegalArgumentException("max must be greater than min");
        }

        Random r = new Random();
        return r.nextInt((max - min) + 1) + min;
    }
}