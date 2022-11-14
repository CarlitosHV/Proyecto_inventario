package com.hardbug.productos.model;

public class UserType {

    private String email;
    private String tipo;
    private String ID;

    public UserType() {
    }

    public UserType(String email, String tipo, String ID) {
        this.setEmail(email);
        this.setTipo(tipo);
        this.setID(ID);
    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }
}
