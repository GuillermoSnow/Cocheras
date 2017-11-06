package com.robpercival.maplocationdemo.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.view.View;
import android.widget.TextView;

import com.robpercival.maplocationdemo.R;
import com.robpercival.maplocationdemo.Util.Constantes;
import com.robpercival.maplocationdemo.Util.TextJustification;

public class SobreNosotros extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sobre_nosotros);
        showToolbar(Constantes.SobreNosotros, true);
        TextView textViewquienesSomos = (TextView) findViewById(R.id.txt_quienesSomos);
        textViewquienesSomos.setText(new SpannableString(textViewquienesSomos.getText()));
        TextJustification.justify(textViewquienesSomos);

        TextView textViewproblemaPark = (TextView) findViewById(R.id.txt_problemaParking);
        textViewproblemaPark.setText(new SpannableString(textViewproblemaPark.getText()));
        TextJustification.justify(textViewproblemaPark);

        TextView textViewproblemaPark2 = (TextView) findViewById(R.id.txt_problemaParking2);
        textViewproblemaPark2.setText(new SpannableString(textViewproblemaPark2.getText()));
        TextJustification.justify(textViewproblemaPark2);


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
    public void justificarTexto(TextView textView){

    }
}
