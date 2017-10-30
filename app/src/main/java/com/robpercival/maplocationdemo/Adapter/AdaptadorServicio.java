package com.robpercival.maplocationdemo.Adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.robpercival.maplocationdemo.R;
import com.robpercival.maplocationdemo.Model.Servicio;

import java.util.List;

/**
 * Created by Guillermo on 30/10/2017.
 */

public class AdaptadorServicio extends BaseAdapter {
    private Context mContext;
    private List<Servicio> lista;

    public AdaptadorServicio(Context mContext, List<Servicio> lista) {
        this.mContext = mContext;
        this.lista = lista;
    }

    public Context getmContext() {
        return mContext;
    }

    public void setmContext(Context mContext) {
        this.mContext = mContext;
    }

    public List<Servicio> getLista() {
        return lista;
    }

    public void setLista(List<Servicio> lista) {
        this.lista = lista;
    }

    @Override
    public int getCount() {
        return lista.size();
    }

    @Override
    public Object getItem(int position) {

        return lista.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v= View.inflate(mContext, R.layout.servicio_detalle,null);
        TextView tvNombre= (TextView)v.findViewById(R.id.nombreServicio);
        TextView tvdescripcion= (TextView)v.findViewById(R.id.precio);
        tvNombre.setText(lista.get(position).getNombre());
        tvdescripcion.setText(lista.get(position).getPrecio());
        v.setTag(lista.get(position).getId());
        return v;    }
}
