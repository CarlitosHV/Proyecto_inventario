package com.hardbug.productos.bd;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SQL extends SQLiteOpenHelper {
    private static final String database = "productos";
    private static final int VERSION = 1;
    private final String  tProductos = "CREATE TABLE PRODUCTOS (" +
            "ID_PRODUCTO INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
            "NOMBRE_PRODUCTO TEXT NOT NULL," +
            "CATEGORIA TEXT NOT NULL," +
            "DESCRIPCION TEXT NOT NULL," +
            "ACTIVO BOOLEAN NOT NULL," +
            "IMAGEN TEXT NOT NULL);";

    public SQL(Context context){
        super(context, database, null, VERSION);
    }

    public void onCreate(SQLiteDatabase db){
        db.execSQL(tProductos);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        if (newVersion > oldVersion){
            db.execSQL("DROP TABLE IF EXISTS PRODUCTOS");
            db.execSQL(tProductos);
        }
    }

}
