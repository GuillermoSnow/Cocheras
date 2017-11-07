package com.robpercival.maplocationdemo.Activity;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.widget.TextView;

import com.robpercival.maplocationdemo.R;
import com.robpercival.maplocationdemo.Util.TextJustification;

public class TerminosCondiciones extends AppCompatActivity {
    StyleSpan boldSpan1 = new StyleSpan(Typeface.BOLD);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terminos_condiciones);
        TextView textView1 = (TextView) findViewById(R.id.txt_condiciones1);
        justificarTexto(textView1);

        TextView textView2 = (TextView) findViewById(R.id.txt_condiciones2);
        justificarTexto(textView2);

        TextView textView3 = (TextView) findViewById(R.id.txt_condiciones3);
        justificarTexto(textView3);

        TextView textView4 = (TextView) findViewById(R.id.txt_condiciones4);
        justificarTexto(textView4);

        TextView textView5 = (TextView) findViewById(R.id.txt_condiciones5);
        justificarTexto(textView5);

            TextView acuerdo1 = (TextView) findViewById(R.id.txt_acuerdos1);
            justificarTexto(acuerdo1);

            TextView acuerdo2 = (TextView) findViewById(R.id.txt_acuerdos2);
            justificarTexto(acuerdo2);

                TextView acuerdo3_1 = (TextView) findViewById(R.id.txt_condiciones3_1);
                justificarTexto(acuerdo3_1);

                TextView acuerdo3_2 = (TextView) findViewById(R.id.txt_condiciones3_2);
                justificarTexto(acuerdo3_2);

            TextView acuerdo4 = (TextView) findViewById(R.id.title_acuerdos4);
            justificarTexto(acuerdo4);

                TextView acuerdo4_1 = (TextView) findViewById(R.id.txt_condiciones4_1);
                justificarTexto(acuerdo4_1);

                TextView acuerdo4_2 = (TextView) findViewById(R.id.txt_condiciones4_2);
                justificarTexto(acuerdo4_2);

    }

    public void justificarTexto(TextView textView){
        textView.setText(new SpannableString(textView.getText()));
        TextJustification.justify(textView);
    }
}
