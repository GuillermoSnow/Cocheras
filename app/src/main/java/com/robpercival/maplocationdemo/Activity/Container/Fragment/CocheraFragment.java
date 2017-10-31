package com.robpercival.maplocationdemo.Activity.Container.Fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.robpercival.maplocationdemo.Activity.MapsActivity;
import com.robpercival.maplocationdemo.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class CocheraFragment extends Fragment {


    public CocheraFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cochera, container, false);
        showToolbar("", true, view);

        return view;
    }

    public void showToolbar(String tittle, boolean upButton, View view){
        Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbarCochera);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(tittle);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(upButton);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(upButton);
       /* toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent regresoMap = new Intent(v.getContext(), MapsActivity.class);
                startActivity(regresoMap);
            }
        });*/
       /*voy a explicar como le damos true al se setDisplayHomeAsUpEnabled esto hace que aparezca la flecha de atras
       * y con el metodo de navigation  podemos darle atras intente con el metodo normal al toolbar pero no es optimo
       * asi que recuerda si quieres manejar eventos de toolbar no necesariamente necesitas declarar en el manifest*/
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent regresoMap = new Intent(v.getContext(), MapsActivity.class);
                startActivity(regresoMap);
            }
        });
    }

}
