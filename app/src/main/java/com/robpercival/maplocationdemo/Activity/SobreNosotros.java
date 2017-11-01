package com.robpercival.maplocationdemo.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import com.robpercival.maplocationdemo.Adapter.Adaptador;
import com.robpercival.maplocationdemo.Model.Informacion;
import com.robpercival.maplocationdemo.R;

import java.util.ArrayList;
import java.util.List;

public class SobreNosotros extends AppCompatActivity {

    private ListView listavw;
    private Adaptador adaptador;
    private List<Informacion> lista;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sobre_nosotros);
        listavw = (ListView)findViewById(R.id.listaInfo);
        lista= new ArrayList<>();

        lista.add(new Informacion(1,"Teléfono","1-85569","HOLA","Por ahi"));
        lista.add(new Informacion(2,"Celular","97451256", "hola",""));
        lista.add(new Informacion(3,"Correo","asdce@moq.com","",""));
        lista.add(new Informacion(4,"Dirección","Av. Venezuela", "0", ""));

        adaptador= new Adaptador(getApplicationContext(),lista);
        listavw.setAdapter(adaptador);

    }
}
