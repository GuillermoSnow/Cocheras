package com.robpercival.maplocationdemo.Activity.Container.Fragment;


import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.robpercival.maplocationdemo.Model.Cochera;
import com.robpercival.maplocationdemo.R;
import com.robpercival.maplocationdemo.Util.Constantes;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

/**
 * A simple {@link Fragment} subclass.
 */
public class CocheraFragment extends Fragment {

    private Cochera cochera;

    public Cochera getCochera() {
        return cochera;
    }

    public void setCochera(Cochera cochera) {
        this.cochera = cochera;
    }

    public CocheraFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cochera, container, false);
        TextView textViewNombre= (TextView)view.findViewById(R.id.nameCochera);
        TextView textViewTelefono= (TextView)view.findViewById(R.id.telefono_cochera);
        TextView textViewCapacidad= (TextView)view.findViewById(R.id.capacidad_cochera);
        TextView textViewCupos= (TextView)view.findViewById(R.id.cuposDisponible_cochera);
        TextView textViewDireccion=(TextView)view.findViewById(R.id.direccion_cochera);
        TextView textViewDescripcion=(TextView)view.findViewById(R.id.info_cochera);

        if(getCochera().getUrl_image().equals(Constantes.img_cochera_defecto)){
        }else {
            final ImageView imageView = (ImageView) view.findViewById(R.id.img_cochera);
            Picasso.with(getActivity()).load(getCochera().getUrl_image()).into(new Target() {
                @Override
                public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                    imageView.setBackground(new BitmapDrawable(bitmap));
                }

                @Override
                public void onBitmapFailed(Drawable errorDrawable) {
                    Log.d("TAG", "FAILED");
                }

                @Override
                public void onPrepareLoad(Drawable placeHolderDrawable) {
                    Log.d("TAG", "Prepare Load");
                }
            });
        }

        textViewNombre.setText(getCochera().getNombre());
        textViewTelefono.setText(getCochera().getTelefono());
        textViewCapacidad.setText(getCochera().getCapacidad());
        textViewDireccion.setText(getCochera().getDireccion());
        textViewCupos.setText(getCochera().getCuposTomados());
        textViewDescripcion.setText(getCochera().getDescripcion());
        showToolbar("", true, view);
        configureImageButton(view); //Se implementa la llamada
        return view;
    }

    private void configureImageButton(View v){
        ImageButton btn = (ImageButton) v.findViewById(R.id.imgBtn_Phone);

        btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
//                Toast.makeText(getActivity(), "You Clicked the button!", Toast.LENGTH_LONG).show();
                Intent  intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:"+getCochera().getTelefono()));
                 if(ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED){
//                    Toast.makeText(getActivity(),"no hay permiso",Toast.LENGTH_LONG).show();
                    requestPermision();
                }else {
                    startActivity(intent);
                }
            }

        });

    }
    public void requestPermision(){
        ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.CALL_PHONE},1);
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
            /*    Intent regresoMap = new Intent(v.getContext(), MapsActivity.class);
                startActivity(regresoMap);*/
                onBackPressed();
            }
        });

    }

    public void onBackPressed() {
        getActivity().onBackPressed();
    }


}
