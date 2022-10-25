package com.hardbug.productos.model;

public class ListaProds {
    private final Integer iconoprod;
    private final String nomprod;


    public ListaProds(Integer iconoprod,String nomprod) {
        this.iconoprod = iconoprod;
        this.nomprod = nomprod;
    }

    public Integer getIconoprod() {
        return iconoprod;
    }

    public String getNomprod() {
        return nomprod;
    }
}
