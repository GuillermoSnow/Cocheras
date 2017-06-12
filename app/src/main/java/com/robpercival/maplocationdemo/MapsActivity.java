package com.robpercival.maplocationdemo;

import android.*;
import android.Manifest;
import android.content.Context;
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
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.vision.barcode.Barcode;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import javax.net.ssl.HttpsURLConnection;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    LocationManager locationManager;

    LocationListener locationListener;

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
            /*String result= "";
            URL url;
            HttpsURLConnection urlConnection= null;
            try {
                url =new URL(urls[0]);
                urlConnection= (HttpsURLConnection) url.openConnection();
                InputStream in= urlConnection.getInputStream();
                InputStreamReader reader= new InputStreamReader(in);
                int data = reader.read();
                while (data!= -1){
                    char current = (char) data;
                    result+=current;
                    data=reader.read();

                }
                return result;

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }*/
            String ll= String.valueOf(urls);


            JSONArray req = new JSONArray();
            JSONObject lugar1 = new JSONObject();

            try {
                lugar1.put("id_empresa","1");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                lugar1.put("nombre","Cochera fake");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            JSONObject coordenadas = new JSONObject();
            try {
                coordenadas.put("lat","-12.0321091");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                coordenadas.put("lng","-77.0805006");
            } catch (JSONException e) {
                e.printStackTrace();
            }

            try {
                lugar1.put("coordenada",coordenadas);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            req.put(lugar1);
            String result=req.toString();
            return result;
        }

         @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
             String a=null;
            try {
                JSONArray lugares= new JSONArray(result);

                for(int i=0; i<lugares.length();i++){
                    JSONObject jsonObject= lugares.getJSONObject(i);
                    JSONObject coordenada= jsonObject.getJSONObject("coordenada");
                    double latitud= Double.parseDouble(coordenada.getString("lat"));
                    double longitud=Double.parseDouble(coordenada.getString("lng"));
                    mMap.addMarker(new MarkerOptions().title("Punto").position(new LatLng(latitud,
                            longitud)));
                    a=Double.toString(latitud);
                }
            } catch (JSONException e) {
                Toast.makeText(getApplication(),"No se pudo", Toast.LENGTH_SHORT).show();
            }
             Toast.makeText(getApplication(),a, Toast.LENGTH_LONG).show();


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
    public void callAsynchronousTask() {
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
                            cargarUbicacionCocheras.execute("Hola");
                        } catch (Exception e) {
                            // TODO Auto-generated catch block
                        }
                    }
                });
            }
        };
        timer.schedule(doAsynchronousTask, 0, 10000); //execute in every 50000 ms
    }



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
                MarkerOptions userMarker= new MarkerOptions().position(userLocation).title("Dolar mak");
                userMarker.icon(BitmapDescriptorFactory.fromResource( R.drawable.usericon));
                userMarker.draggable(true);
                mMap.addMarker(userMarker);
                mMap.moveCamera(CameraUpdateFactory.newLatLng(userLocation));
                Log.i("hI","F");
                CargarUbicacionCocheras cargarUbicacionCocheras= new CargarUbicacionCocheras();
                cargarUbicacionCocheras.execute("Hola");



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
                Log.i("hI","F");
                CargarUbicacionCocheras cargarUbicacionCocheras= new CargarUbicacionCocheras();
                cargarUbicacionCocheras.execute("Hola");


            } else {

                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);

                Location lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

                LatLng userLocation = new LatLng(lastKnownLocation.getLatitude(), lastKnownLocation.getLongitude());
                mMap.clear();
                MarkerOptions userMarker= new MarkerOptions().position(userLocation).title("User Location");
                userMarker.icon(BitmapDescriptorFactory.fromResource( R.drawable.usericon));

                mMap.addMarker(userMarker);
                mMap.moveCamera(CameraUpdateFactory.newLatLng(userLocation));
                callAsynchronousTask();
                /*try {
                    CargarUbicacionCocheras cargarUbicacionCocheras= new CargarUbicacionCocheras();
                    cargarUbicacionCocheras.execute("Hola");
                }catch (Exception e){
                    Log.i("Cagado","maq");
                }

                 JSONObject lugar1 = new JSONObject();
                Log.i("hI","F");
                try{
                    lugar1.put("latitude","-12.0534268");
                    lugar1.put("longitude","-77.0813782");

                }catch (JSONException e) {
                    e.printStackTrace();
                    Log.i("Error","Plocs");

                }
                JSONObject lugar2 = new JSONObject();
                try{
                    lugar2.put("latitude","-12.0662727");
                    lugar2.put("longitude","-77.0693972");
                }catch (JSONException e) {
                    e.printStackTrace();
                    Log.i("Error","Plocs");

                }
                JSONArray jsonArray = new JSONArray();
                jsonArray.put(lugar1);
                jsonArray.put(lugar2);

                try {
                    lugares.put("Lugares",lugares);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    createMarkersFromJson(jsonArray);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                /*
                for(int i=0; i<jsonArray.length();i++){

                     JSONObject jsonObject= null;

                     try {
                         jsonObject = jsonArray.getJSONObject(i);
                     } catch (JSONException e) {
                         e.printStackTrace();
                     }

                     double latitud=0;
                    try {
                        latitud = Double.parseDouble(jsonObject.getString("latitude"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    double longitud= 0;
                    try {
                        longitud = Double.parseDouble(jsonObject.getString("longitude"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    mMap.addMarker(new MarkerOptions().title("Punto").icon(BitmapDescriptorFactory.fromResource( R.drawable.cochera))

                            .position(new LatLng(latitud,
                                    longitud)));
                }*/

            }


        }


    }
}
