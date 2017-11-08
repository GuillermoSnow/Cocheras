package com.robpercival.maplocationdemo.Activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.robpercival.maplocationdemo.R;
import com.robpercival.maplocationdemo.Util.Constantes;

public class TerminosCondiciones extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terminos_condiciones);
        getSupportActionBar().setTitle(Constantes.terminosCondiciones);

    }
}
