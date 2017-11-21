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
    private String nombre;
    private String direccion;
    private String url_image;
    private String descripcion;

    public String getCuposTomados() {
        return cuposTomados;
    }

    public void setCuposTomados(String cuposTomados) {
        this.cuposTomados = cuposTomados;
    }

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

    public String getCuposDisponibles() {
        return cuposDisponibles;
    }

    public void setCuposDisponibles(String cuposDisponibles) {
        this.cuposDisponibles = cuposDisponibles;
    }

    public String getUrl_image() {
        return url_image;
    }

    public void setUrl_image(String url_image) {
        this.url_image = url_image;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}
