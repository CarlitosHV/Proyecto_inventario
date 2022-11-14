package com.hardbug.productos.model;

public class UserType {

    private String email;
    private boolean tipo;
    private String ID;

    public UserType() {
    }

    public UserType(String email, boolean tipo, String ID) {
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

    public boolean isTipo() {
        return tipo;
    }

    public void setTipo(boolean tipo) {
        this.tipo = tipo;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }
}
