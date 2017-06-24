package com.robpercival.maplocationdemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;

public class DetalleServicio extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_servicio);
        //String capacidad=  getIntent().getStringExtra("LISTA");

        ArrayList<String> myList = (ArrayList<String>) getIntent().getSerializableExtra("LISTA");
        ListView servicios= (ListView) findViewById(R.id.lista);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1
        ,myList);
        servicios.setAdapter(arrayAdapter);
        //moq.setText(myList.indexOf(0));
    }

    private void cargar (View view){
        String capacidad=  getIntent().getStringExtra("Capacidad");



    }

}
