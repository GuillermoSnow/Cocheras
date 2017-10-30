package com.robpercival.maplocationdemo.Model;

import java.io.Serializable;

/**
 * Created by Guillermo on 30/10/2017.
 */

public class Servicio implements Serializable{
    private String nombre;
    private int id;
    private String precio;

    public Servicio(int id,String nombre,  String precio) {
        this.nombre = nombre;
        this.id = id;
        this.precio = precio;
    }
    public Servicio(){

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getPrecio() {
        return precio;
    }

    public void setPrecio(String precio) {
        this.precio = "S/." +precio;
    }
}
