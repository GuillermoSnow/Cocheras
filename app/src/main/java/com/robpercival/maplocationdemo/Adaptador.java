package com.robpercival.maplocationdemo;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Guillermo on 1/10/2017.
 */

    public class Adaptador extends RecyclerView.Adapter<Adaptador.ViewHolder>{
    private  ArrayList<String> mdata;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView mTextView;
        public ViewHolder(TextView v) {
            super(v);
            mTextView = v;
        }
    }

    public Adaptador( ArrayList<String> myDataset) {
        mdata = myDataset;
    }


    @Override
    public Adaptador.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        TextView v = (TextView) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.activity_detalle_servicio, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(Adaptador.ViewHolder holder, int position) {
        holder.mTextView.setText(mdata.get(position));
    }

    @Override
    public int getItemCount() {
        return mdata.size();
    }
}
