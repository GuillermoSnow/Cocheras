package com.robpercival.maplocationdemo;

import android.*;
import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.identity.intents.Address;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.vision.barcode.Barcode;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import javax.net.ssl.HttpsURLConnection;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    LocationManager locationManager;

    LocationListener locationListener;
    ArrayList<Cochera> cocheras;
    static String CAPACIDAD;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 1) {

            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    {

                        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);

                    }

                }

            }

        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

       /* CargarUbicacionCocheras cargarCocheras= new CargarUbicacionCocheras();
        cargarCocheras.execute("tuURL"); // La api de carlos va aqui */
    }

    public class CargarUbicacionCocheras extends AsyncTask<String,Void,String>{

        @Override
        protected String doInBackground(String... urls) {
            String result = "";
            URL url;
            HttpURLConnection urlConnection= null;
            try {
                url =new URL(urls[0]);
                urlConnection = (HttpURLConnection) url.openConnection();

                InputStream in = urlConnection.getInputStream();

                InputStreamReader reader = new InputStreamReader(in);
                int data = reader.read();
                while (data != -1) {
                    char current = (char) data;
                    result += current;
                    data = reader.read();
                }
                Log.i("JSON", result);


            } catch (MalformedURLException e) {
                e.printStackTrace();

            } catch (IOException ex) {
                ex.printStackTrace();

            }

            /*String result = null;
            try {

                InputStream is = getAssets().open("variasCocheras.json");

                int size = is.available();

                byte[] buffer = new byte[size];

                is.read(buffer);

                is.close();

                result = new String(buffer, "UTF-8");


            } catch (IOException ex) {
                ex.printStackTrace();
                return null;
            }*/
            return result;
        }

         @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
             String a=null;
             String ss=" ";
            try {
                JSONArray lugares= new JSONArray(result);
                ArrayList<Cochera> cocheras = new ArrayList<Cochera>();
                for(int i=0; i<lugares.length();i++){
                    Cochera coch=new Cochera();
                    JSONObject jsonObject= lugares.getJSONObject(i);
                    JSONObject coordenada= jsonObject.getJSONObject("coordenadas");
                    JSONArray servicios= new JSONArray(jsonObject.getString("servicioCocheras"));
                    double latitud= Double.parseDouble(coordenada.getString("lat"));
                    double longitud=Double.parseDouble(coordenada.getString("lng"));
                    coch.setLatitud(latitud);
                    coch.setLongitud(longitud);
                    coch.setCapacidad(jsonObject.getString("capacidad"));

                    ArrayList<String> listaservicios= new ArrayList<String>() ;
                    for (int j=0; j<servicios.length();j++){
                        JSONObject jsonObjects= servicios.getJSONObject(j);

                        if(jsonObjects.getBoolean("estado")==true){
                            JSONObject detalleservicio= jsonObjects.getJSONObject("tipoServicio");
                            listaservicios.add(detalleservicio.getString("nombre")+": Precio: S/. "+ jsonObjects.getString("precio_hora"));

                            //ss= ss+" Servicio: "+detalleservicio.getString("nombre")+ " Precio: "+ jsonObjects.getString("precio_hora");
                             Marker mark = mMap.addMarker(new MarkerOptions().title(jsonObject.getString("nombre")).position(new LatLng(latitud,
                                    longitud)).snippet("Capacidad : " + jsonObject.getString("capacidad")

                            ).icon(BitmapDescriptorFactory.fromResource(R.drawable.coche))

                            );
                            mark.setTag(listaservicios);


                        }

                    }
                     // coch.setLista(listaservicios);
                     // cocheras.add(coch);
                }

            } catch (JSONException e) {
                Toast.makeText(getApplication(),"Meq", Toast.LENGTH_SHORT).show();
            }
        }
    }



    void createMarkersFromJson(JSONArray json) throws  JSONException{

        for(int i=0; i<json.length();i++){
            JSONObject jsonObject= json.getJSONObject(i);
            JSONObject coordenada= jsonObject.getJSONObject("coordenada");
            double latitud= Double.parseDouble(jsonObject.getString("lat"));
            double longitud=Double.parseDouble(jsonObject.getString("lng"));
            mMap.addMarker(new MarkerOptions().title("Punto")

            .position(new LatLng(latitud,
                    longitud)));
        }
    }
    /* public void callAsynchronousTask() {
        final Handler handler = new Handler();
        Timer timer = new Timer();
        TimerTask doAsynchronousTask = new TimerTask() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    public void run() {
                        try {
                            CargarUbicacionCocheras cargarUbicacionCocheras = new CargarUbicacionCocheras();
                            // PerformBackgroundTask this class is the class that extends AsynchTask
                            cargarUbicacionCocheras.execute("54.175.211.134:3000/todaCocheraConServicios");
                        } catch (Exception e) {
                            Toast.makeText(getApplication(),"Error al cargar los datos", Toast.LENGTH_SHORT).show();

                        }
                    }
                });
            }
        };
        timer.schedule(doAsynchronousTask, 0, 100000); //execute in every 100000 ms
    }*/



    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;

        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {

                LatLng userLocation = new LatLng(location.getLatitude(), location.getLongitude());

                mMap.clear();
                MarkerOptions userMarker= new MarkerOptions().position(userLocation).title("User Location");
                userMarker.icon(BitmapDescriptorFactory.fromResource( R.drawable.usericon));
                userMarker.draggable(false);

                mMap.addMarker(userMarker);

                mMap.moveCamera(CameraUpdateFactory.newLatLng(userLocation));
                //callAsynchronousTask();
               // CargarUbicacionCocheras cargarUbicacionCocheras= new CargarUbicacionCocheras();
               // cargarUbicacionCocheras.execute("http://54.175.211.134:3000/todaCocheraConServicios");



            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {

            }
        };

        if (Build.VERSION.SDK_INT < 23) {

            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
            CargarUbicacionCocheras cargarUbicacionCocheras= new CargarUbicacionCocheras();
            cargarUbicacionCocheras.execute("Hola");

        } else {

            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);

                CargarUbicacionCocheras cargarUbicacionCocheras= new CargarUbicacionCocheras();
                cargarUbicacionCocheras.execute("http://54.175.211.134:3000/todaCocheraConServicios");


            } else {

                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);

                Location lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

                LatLng userLocation = new LatLng(lastKnownLocation.getLatitude(), lastKnownLocation.getLongitude());
                mMap.clear();
                MarkerOptions userMarker= new MarkerOptions().position(userLocation).title("User Location");
                userMarker.icon(BitmapDescriptorFactory.fromResource( R.drawable.usericon));

                mMap.addMarker(userMarker);
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation,10));
                //callAsynchronousTask();
                CargarUbicacionCocheras cargarUbicacionCocheras= new CargarUbicacionCocheras();
                cargarUbicacionCocheras.execute("http://54.175.211.134:3000/todaCocheraConServicios");


            }


        }
        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            public void onInfoWindowClick(Marker marker) {
                if(marker.getTitle()!="User Location") {
                    ArrayList<String> info = (ArrayList<String>) marker.getTag();
                    Intent intent = new Intent(MapsActivity.this, DetalleServicio.class);
                    intent.putExtra("LISTA", info);

                    startActivity(intent);
                }

            }
        });


    }

}
