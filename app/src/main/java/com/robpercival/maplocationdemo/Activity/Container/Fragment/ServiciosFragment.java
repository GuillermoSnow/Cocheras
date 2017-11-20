package com.robpercival.maplocationdemo.Activity.Container.Fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.robpercival.maplocationdemo.Adapter.AdapterServicioCocheraRecyclerView;
import com.robpercival.maplocationdemo.Model.Cochera;
import com.robpercival.maplocationdemo.Model.Servicio;
import com.robpercival.maplocationdemo.R;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class ServiciosFragment extends Fragment {

    private Cochera cochera;

    public Cochera getCochera() {
        return cochera;
    }

    public void setCochera(Cochera cochera) {
        this.cochera = cochera;
    }

    public ServiciosFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_servicios, container, false);
        showToolbar("", true, view);

        RecyclerView servicioCocheras = (RecyclerView) view.findViewById(R.id.serviciosCocherasRecycler);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        servicioCocheras.setLayoutManager(linearLayoutManager);

        AdapterServicioCocheraRecyclerView adapterServicio =
                new AdapterServicioCocheraRecyclerView(buidServicio(), R.layout.cardview_servicios_cocheras, getActivity());
        servicioCocheras.setAdapter(adapterServicio);
        return view;

    }

    public ArrayList<Servicio> buidServicio(){

        ArrayList<Servicio> servicios =cochera.getListaServicio();
       /* servicios.add(new Servicio(1, "Lavado de Auto", "5.50"));
        servicios.add(new Servicio(2, "Lavado de Auto", "5.50"));
        servicios.add(new Servicio(3, "Lavado de Auto", "5.50"));
//        servicios.add(new Servicio(4, "Lavado de Auto", "5.50"));*/
        return servicios;
    }

    public void showToolbar(String tittle, boolean upButton, View view){
        Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(tittle);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(upButton);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Intent regresoMap = new Intent(v.getContext(), MapsActivity.class);
                startActivity(regresoMap);*/
                onBackPressed();
            }
        });
    }

    public void onBackPressed() {
        getActivity().onBackPressed();
    }
}
