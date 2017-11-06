package com.robpercival.maplocationdemo.Activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.robpercival.maplocationdemo.Model.Informacion;
import com.robpercival.maplocationdemo.R;

import java.util.ArrayList;
import java.util.List;

public class SobreNosotros extends AppCompatActivity {

    private List<Informacion> lista;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sobre_nosotros);

        lista= new ArrayList<>();

        lista.add(new Informacion(1,"Teléfono","1-85569","HOLA","Por ahi"));
        lista.add(new Informacion(2,"Celular","97451256", "hola",""));
        lista.add(new Informacion(3,"Correo","asdce@moq.com","",""));
        lista.add(new Informacion(4,"Dirección","Av. Venezuela", "0", ""));


    }
}
