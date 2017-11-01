package com.robpercival.maplocationdemo.Model;

/**
 * Created by Guillermo on 29/10/2017.
 */

public class Informacion {
    private int id;
    private String nombre;
    private String descripcion;
    private String extra;
    private String address;

    public Informacion(int id, String nombre, String descripcion, String extra, String address) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.extra = extra;
        this.address = address;
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

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getExtra() {
        return extra;
    }

    public void setExtra(String extra) {
        this.extra = extra;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
