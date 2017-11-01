package com.robpercival.maplocationdemo.Model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Guillermo on 18/06/2017.
 */

public class Cochera implements Serializable{
    private Double latitud;
    private Double Longitud;
    private String Telefono;
    private String capacidad;
    private String cuposDisponibles;
    private String cuposTomados;

    public String getCuposTomados() {
        return cuposTomados;
    }

    public void setCuposTomados(String cuposTomados) {
        this.cuposTomados = cuposTomados;
    }

    private String nombre;
    private String direccion;


    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    private ArrayList<Servicio> listaServicio;

    public ArrayList<Servicio> getListaServicio() {
        return listaServicio;
    }

    public void setListaServicio(ArrayList<Servicio> listaServicio) {
        this.listaServicio = listaServicio;
    }

    public Double getLatitud() {
        return latitud;
    }

    public void setLatitud(Double latitud) {
        this.latitud = latitud;
    }

    public Double getLongitud() {
        return Longitud;
    }

    public void setLongitud(Double longitud) {
        Longitud = longitud;
    }

    public String getTelefono() {
        return Telefono;
    }

    public void setTelefono(String telefono) {
        Telefono = telefono;
    }

    public String getCapacidad() {
        return capacidad;
    }

    public void setCapacidad(String capacidad) {
        this.capacidad = capacidad;
    }

    public ArrayList<String> getLista() {
        return lista;
    }

    public void setLista(ArrayList<String> lista) {
        this.lista = lista;
    }

    private ArrayList<String> lista;

    public String getCuposDisponibles() {
        return cuposDisponibles;
    }

    public void setCuposDisponibles(String cuposDisponibles) {
        this.cuposDisponibles = cuposDisponibles;
    }
}
