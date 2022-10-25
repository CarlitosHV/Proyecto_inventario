package com.hardbug.productos.bd;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.Editable;
import android.util.Log;

import java.util.ArrayList;

public class SQLITE {

    private final SQL Sql;
    private SQLiteDatabase db;

    public SQLITE(Context context){
        Sql = new SQL(context);
    }

    public void abrir(){
        Log.i("SQLite", "Se abre conexión con base de datos " + Sql.getDatabaseName());
        db = Sql.getWritableDatabase();
    }

    public void cerrar(){
        Log.i("SQLite", "Se cierra conexión con base de datos " + Sql.getDatabaseName());
        Sql.close();
    }

    public boolean addProducto(
            int id, String nombreprod, String  categoria, String descripcion, boolean activo, String imagepath){

        ContentValues cv = new ContentValues();
        cv.put("ID_PRODUCTO", id);
        cv.put("NOMBRE_PRODUCTO", nombreprod);
        cv.put("CATEGORIA", categoria);
        cv.put("DESCRIPCION", descripcion);
        cv.put("ACTIVO", activo);
        cv.put("IMAGEN", imagepath);

        return (db.insert("PRODUCTOS", null, cv) != 1)?true : false;
    }
    public Cursor getProducto(){
        return db.rawQuery("SELECT * FROM PRODUCTOS WHERE ACTIVO=true", null);
    }

    public Cursor getProductoInactivo(){
        return db.rawQuery("SELECT * FROM PRODUCTOS WHERE ACTIVO=false", null);
    }

    public ArrayList<String> getProductos(Cursor cursor){
        ArrayList<String> ListData = new ArrayList<>();
        String item = "";

        if (cursor.moveToFirst()){
            do{
                item += "ID: " + cursor.getString(0) + " \r\n";
                item += "Nombre: " + cursor.getString(1) + " \r\n";
                item += "Categoría: " + cursor.getString(2) + " \r\n";
                item += "Descripción: " + cursor.getString(3) + " \r\n";
                ListData.add(item);
                item = "";
            }while(cursor.moveToNext());
        } return ListData;
    }

    public ArrayList<String> getNombreProd(Cursor cursor){
        ArrayList<String> ListData = new ArrayList<>();
        String item = "";
        if (cursor.moveToFirst()){
            do{
                item += cursor.getString(1) + " \r\n";
                ListData.add(item);
                item = "";
            }while(cursor.moveToNext());
        } return ListData;
    }

    public ArrayList<String> getImagenes(Cursor cursor){
        ArrayList<String> ListData = new ArrayList<>();
        if (cursor.moveToFirst()){
            do {
                ListData.add(cursor.getString(5));
            }while(cursor.moveToNext());
        } return ListData;
    }

    public ArrayList<String> getID(Cursor cursor){
        ArrayList<String> ListData = new ArrayList<>();
        if (cursor.moveToFirst()){
            do {
                ListData.add(cursor.getString(0));
            }while(cursor.moveToNext());
        } return ListData;
    }

    public String updateRegistroProducto(
            int id, String nombreprod, String  categoria, String descripcion, boolean activo, String imagepath){

        ContentValues cv = new ContentValues();
        cv.put("ID_PRODUCTO", id);
        cv.put("NOMBRE_PRODUCTO", nombreprod);
        cv.put("CATEGORIA", categoria);
        cv.put("DESCRIPCION", descripcion);
        cv.put("ACTIVO", activo);
        cv.put("IMAGEN", imagepath);

        int valor = db.update("PRODUCTOS", cv, "ID_PRODUCTO=" + id, null);

        if (valor == 1){
            return "Producto actualizado con éxito";
        }else{
            return "Error de actualización";
        }
    }

    public Boolean updateEstatus(
            int id, String nombreprod, String  categoria, String descripcion, boolean activo, String imagepath){

        ContentValues cv = new ContentValues();
        cv.put("ID_PRODUCTO", id);
        cv.put("NOMBRE_PRODUCTO", nombreprod);
        cv.put("CATEGORIA", categoria);
        cv.put("DESCRIPCION", descripcion);
        cv.put("ACTIVO", activo);
        cv.put("IMAGEN", imagepath);

        int valor = db.update("PRODUCTOS", cv, "ID_PRODUCTO=" + id, null);

        if (valor == 1){
            return true;
        }else{
            return false;
        }
    }

    public Cursor getValor(int id){
        return db.rawQuery("SELECT * FROM PRODUCTOS WHERE ID_PRODUCTO =" + id, null);
    }



    public int Eliminar(Editable id){
        return db.delete("PRODUCTOS", "ID_PRODUCTO=" +id, null);
    }
}
