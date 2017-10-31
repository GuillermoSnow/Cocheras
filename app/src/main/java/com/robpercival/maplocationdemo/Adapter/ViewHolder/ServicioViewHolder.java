package com.robpercival.maplocationdemo.Adapter.ViewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.robpercival.maplocationdemo.R;

/**
 * Created by ANTHONY on 31/10/2017.
 */

public class ServicioViewHolder extends RecyclerView.ViewHolder {

     public TextView nombreServicio;
     public TextView precioServicio;
     public TextView estadoServicio;

    public ServicioViewHolder(View itemView) {
        super(itemView);
        nombreServicio = (TextView)itemView.findViewById(R.id.nameServicio_Cochera);
        precioServicio = (TextView)itemView.findViewById(R.id.precioServicio_Cochera);
        estadoServicio = (TextView)itemView.findViewById(R.id.estadoServicio_Cochera);
    }
}
