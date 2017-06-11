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
        JSONObject lugar1 = new JSONObject();
        try{
            lugar1.put("latitude","15.613658");
            lugar1.put("longitude","16.106653");

        }catch (JSONException e) {
            e.printStackTrace();
            Log.i("Error","Plocs");

        }
        JSONObject lugar2 = new JSONObject();
        try{
            lugar2.put("latitude","48.613658");
            lugar2.put("longitude","-80.106653");
        }catch (JSONException e) {
            e.printStackTrace();
            Log.i("Error","Plocs");

        }
        JSONArray jsonArray = new JSONArray();
        jsonArray.put(lugar1);
        jsonArray.put(lugar2);
        JSONObject lugares = new JSONObject();
        try {
            lugares.put("Lugares",jsonArray);
        } catch (JSONException e) {
            e.printStackTrace();
            Log.i("Error","Plocs");
        }
        /*try {
            lugares.put("Lugares",lugares);
        } catch (JSONException e) {
            e.printStackTrace();
        }*/
        String jsonStr= lugares.toString();
        try {
            createMarkersFromJson(jsonStr);
        } catch (JSONException e) {
            e.printStackTrace();
        }
       /* CargarUbicacionCocheras cargarCocheras= new CargarUbicacionCocheras();
        cargarCocheras.execute("tuURL"); // La api de carlos va aqui */
    }

    public class CargarUbicacionCocheras extends AsyncTask<String,Void, String>{

        @Override
        protected String doInBackground(String... urls) {
            String result= "";
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
            }

            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            try {
                createMarkersFromJson(result);
            } catch (JSONException e) {
                e.printStackTrace();
            }


        }
    }
    void createMarkersFromJson(String json) throws  JSONException{
        JSONArray jsonArray = new JSONArray(json);
        for(int i=0; i<jsonArray.length();i++){
            JSONObject jsonObject= jsonArray.getJSONObject(i);
            double latitud= Double.parseDouble(jsonObject.getString("latitude"));
            double longitud=Double.parseDouble(jsonObject.getString("longitude"));
            mMap.addMarker(new MarkerOptions().title("Punto")

            .position(new LatLng(latitud,
                    longitud)));
        }
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
                MarkerOptions userMarker= new MarkerOptions().position(userLocation).title("User Location");
                userMarker.icon(BitmapDescriptorFactory.fromResource( R.drawable.usericon));
                userMarker.draggable(true);
                mMap.addMarker(userMarker);
                mMap.moveCamera(CameraUpdateFactory.newLatLng(userLocation));
                Log.i("hI","F");



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

        } else {

            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
                Log.i("hI","F");


            } else {

                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);

                Location lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

                LatLng userLocation = new LatLng(lastKnownLocation.getLatitude(), lastKnownLocation.getLongitude());
                mMap.clear();
                MarkerOptions userMarker= new MarkerOptions().position(userLocation).title("User Location");
                userMarker.icon(BitmapDescriptorFactory.fromResource( R.drawable.usericon));
                userMarker.draggable(true);

                mMap.addMarker(userMarker);
                mMap.moveCamera(CameraUpdateFactory.newLatLng(userLocation));
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

                /*try {
                    lugares.put("Lugares",lugares);
                } catch (JSONException e) {
                    e.printStackTrace();
                }*/



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
                    mMap.addMarker(new MarkerOptions().title("Punto")

                            .position(new LatLng(latitud,
                                    longitud)));
                }

            }


        }


    }
}
