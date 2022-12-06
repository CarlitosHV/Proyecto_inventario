package com.hardbug.productos;

import android.app.Application;

import com.google.android.material.color.DynamicColors;


public class ColorDinamico extends Application {
    public void OnCreate(){
        super.onCreate();
        DynamicColors.applyToActivitiesIfAvailable(this);
    }
}
