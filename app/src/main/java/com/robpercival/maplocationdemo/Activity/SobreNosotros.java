package com.robpercival.maplocationdemo.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.robpercival.maplocationdemo.R;
import com.robpercival.maplocationdemo.Util.Constantes;

public class SobreNosotros extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sobre_nosotros);
        showToolbar(Constantes.SobreNosotros, true);

    }
    public void showToolbar(String tittle, boolean upButton){
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarCocheraAboutUS);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(tittle);
        getSupportActionBar().setDisplayHomeAsUpEnabled(upButton);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent regresoMap = new Intent(v.getContext(), MapsActivity.class);
                startActivity(regresoMap);
            }
        });
    }

}
