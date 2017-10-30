package com.robpercival.maplocationdemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class DetalleServicio extends AppCompatActivity {

    private ListView listavw;
    private AdaptadorServicio adaptador;
    private List<Servicio> lista;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_servicio);
        //String capacidad=  getIntent().getStringExtra("LISTA");

        ArrayList<Servicio> myList = (ArrayList<Servicio>) getIntent().getSerializableExtra("LISTA");
        ListView listavw= (ListView) findViewById(R.id.lista);
        lista= new ArrayList<Servicio>();

        for (int i=0; i<myList.size(); i++){
            Servicio s= new Servicio(i,myList.get(i).getNombre(),myList.get(i).getPrecio());

            lista.add(s);
        }
        adaptador=new AdaptadorServicio(getApplicationContext(),myList);
        listavw.setAdapter(adaptador);

        /*ArrayAdapter<Servicio> arrayAdapter = new ArrayAdapter<Servicio>(this, android.R.layout.simple_expandable_list_item_1
        ,myList);
        listavw.setAdapter(arrayAdapter); */


        //moq.setText(myList.indexOf(0));
    }

    private void cargar (View view){
        String capacidad=  getIntent().getStringExtra("Capacidad");

    }

}
