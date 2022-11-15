package com.hardbug.productos.model;

import java.util.Date;

public class Consumibles {

    private String code;
    private String descripcion;
    private Date fecha;
    private float count;


    public Consumibles() {
    }

    public Consumibles(String code, String descripcion, Date fecha, float count) {
        this.setCode(code);
        this.setDescripcion(descripcion);
        this.setFecha(fecha);
        this.setCount(count);
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public float getCount() {
        return count;
    }

    public void setCount(float count) {
        this.count = count;
    }
}
