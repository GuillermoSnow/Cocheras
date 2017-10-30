package com.robpercival.maplocationdemo.Adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.robpercival.maplocationdemo.Model.Informacion;
import com.robpercival.maplocationdemo.R;

import java.util.List;

/**
 * Created by Guillermo on 1/10/2017.
 */

    public class Adaptador extends BaseAdapter{

    private Context mContext;
    private List<Informacion> lista;

    public Adaptador(Context mContext, List<Informacion> lista) {
        this.mContext = mContext;
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
        View v= View.inflate(mContext, R.layout.aboutlist,null);
        TextView tvNombre= (TextView)v.findViewById(R.id.nombre);
        TextView tvdescripcion= (TextView)v.findViewById(R.id.descripcion);
        TextView tvextra= (TextView)v.findViewById(R.id.extra);
        TextView tvaddress= (TextView)v.findViewById(R.id.address);
        tvNombre.setText(lista.get(position).getNombre());
        tvdescripcion.setText(lista.get(position).getDescripcion());
        tvextra.setText(lista.get(position).getExtra());
        tvaddress.setText(lista.get(position).getAddress());
        v.setTag(lista.get(position).getId());
        return v;
    }
}
