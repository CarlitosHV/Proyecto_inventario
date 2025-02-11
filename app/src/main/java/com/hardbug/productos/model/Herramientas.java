package com.hardbug.productos.model;

import java.util.Date;

public class Herramientas {

    private String code;
    private String descripcion;
    private Date fecha;
    private int count;
    private String id;

    public Herramientas() {
    }

    public Herramientas(String code, String descripcion, Date fecha, int count) {
        this.setCode(code);
        this.setDescripcion(descripcion);
        this.setFecha(fecha);
        this.setCount(count);
    }

    public Herramientas(String code, String descripcion, Date fecha, int count, String id) {
        this.setCode(code);
        this.setDescripcion(descripcion);
        this.setFecha(fecha);
        this.setCount(count);
        this.setId(id);
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

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
