package com.robpercival.maplocationdemo.Adapter;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.widget.RecyclerView;
import android.transition.Explode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.robpercival.maplocationdemo.Adapter.ViewHolder.ServicioViewHolder;
import com.robpercival.maplocationdemo.Model.Servicio;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by ANTHONY on 31/10/2017.
 */

public class AdapterServicioCocheraRecyclerView extends RecyclerView.Adapter<ServicioViewHolder>{
    private List<Servicio> lista_servicios;
    private int resource;
    private Activity activity;

    public AdapterServicioCocheraRecyclerView(List<Servicio> lista_servicios, int resource, Activity activity) {
        this.lista_servicios = lista_servicios;
        this.resource = resource;
        this.activity = activity;
    }

    @Override
    public ServicioViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(resource, parent, false);
        return new ServicioViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ServicioViewHolder holder, int position) {
        Servicio servicio = lista_servicios.get(position);
        holder.nombreServicio.setText(servicio.getNombre());
        holder.precioServicio.setText(servicio.getPrecio());
//        holder.estadoServicio.setText(servicio.getLike_number());
//        Falta implementar todavia esos metodo
//        Picasso.with(activity).load(picture.getPicture()).into(holder.pictureCard);

    }

    @Override
    public int getItemCount() {
        return lista_servicios.size();
    }
}
