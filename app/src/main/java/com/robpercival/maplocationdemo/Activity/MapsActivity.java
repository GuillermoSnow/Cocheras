package com.robpercival.maplocationdemo.Activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.multidex.MultiDex;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.robpercival.maplocationdemo.Activity.Container.ContainerCocheraActivity;
import com.robpercival.maplocationdemo.Model.Cochera;
import com.robpercival.maplocationdemo.Model.Servicio;
import com.robpercival.maplocationdemo.R;
import com.robpercival.maplocationdemo.Util.DirectionsJSONParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {


    private GoogleMap mMap;
    LocationManager locationManager;
    LocationListener locationListener;
    View mapView;
    private String[] mOpcionesTitles;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private CharSequence mTitle;
    private String lat;
    private String lon;
    private String url;
    private LatLng ultimaPosicion;
    private Polyline polyline=null;

    public LatLng getUltimaPosicion() {
        return ultimaPosicion;
    }

    public void setUltimaPosicion(LatLng ultimaPosicion) {
        this.ultimaPosicion = ultimaPosicion;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLon() {
        return lon;
    }

    public void setLon(String lon) {
        this.lon = lon;
    }

    public String getUrl() {
        return "http://107.22.10.188:3000/parking/nearby?lat=" + getLat() + "&lng=" + getLon();
    }

    public void setUrl(String url) {
        this.url = url;
    }


    public void actualizar(View view) {

        if (Build.VERSION.SDK_INT < 23) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ) {
                mMap.clear();
                mMap.setMyLocationEnabled(true);

                Location lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                LatLng userLocation = new LatLng(lastKnownLocation.getLatitude(), lastKnownLocation.getLongitude());
                ultimaPosicion = userLocation;
            /*MarkerOptions userMarker = new MarkerOptions().position(userLocation).title("User Location");
            userMarker.icon(BitmapDescriptorFactory.fromResource(R.drawable.usericon));
            mMap.addMarker(userMarker);*/
                //mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 15));
                setLat(Double.toString(userLocation.latitude));
                setLon(Double.toString(userLocation.longitude));
                CargarUbicacionCocheras cargarUbicacionCocheras = new CargarUbicacionCocheras();
                cargarUbicacionCocheras.execute(getUrl());

            } else {
                 Toast.makeText(getApplication(), "No se tiene permiso de ubicación ", Toast.LENGTH_SHORT).show();

            }
        } else if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            boolean isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

            if (isGPSEnabled) {
                mMap.setMyLocationEnabled(true);

                locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);

                Location lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                if (lastKnownLocation != null) {
                    mMap.clear();
                    LatLng userLocation = new LatLng(lastKnownLocation.getLatitude(), lastKnownLocation.getLongitude());
                    ultimaPosicion = userLocation;
                    //AIzaSyCHSq88VNCOxt-VuTHY7BeMgY_1P4eYak4
                    /*MarkerOptions userMarker = new MarkerOptions().position(ultimaPosicion).title("User Location");
                    userMarker.icon(BitmapDescriptorFactory.fromResource(R.drawable.usericon));
                    mMap.addMarker(userMarker);*/
                    setLat(Double.toString(ultimaPosicion.latitude));
                    setLon(Double.toString(ultimaPosicion.longitude));
                    CargarUbicacionCocheras cargarUbicacionCocheras = new CargarUbicacionCocheras();
                    cargarUbicacionCocheras.execute(getUrl());
                } else
                    Toast.makeText(getApplication(), "Hubo un problema al obtener la ubicacion actual", Toast.LENGTH_SHORT).show();

            } else
                Toast.makeText(getApplication(), "Por favor Encienda el Servicio de GPS ", Toast.LENGTH_SHORT).show();

        } else
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);

    }

    private String getDirectionsUrl(LatLng origin, LatLng dest) {

        // Origin of route
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;

        // Destination of route
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;

        // Sensor enabled
        String sensor = "sensor=false";
        String mode = "mode=driving";

        // Building the parameters to the web service
        String parameters = str_origin + "&" + str_dest + "&" + sensor + "&" + mode;

        // Output format
        String output = "json";

        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters;


        return url;
    }

    public void menu(View view) {
        mDrawerLayout.openDrawer(mDrawerList);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

       /* if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    {
                        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
                    }
                }
            }
        }*/
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {


                locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
                mMap.setMyLocationEnabled(true);

                /*Location lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                CargarUbicacionCocheras cargarUbicacionCocheras = new CargarUbicacionCocheras();
                cargarUbicacionCocheras.execute(getUrl());*/
                boolean isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
                boolean isREDEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
                if (isREDEnabled){
                    Location lastREDKnownLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                    if (lastREDKnownLocation!=null){
                        LatLng userLocation = new LatLng(lastREDKnownLocation.getLatitude(), lastREDKnownLocation.getLongitude());
                        ultimaPosicion = userLocation;
                        lat = String.valueOf(lastREDKnownLocation.getLatitude());
                        lon = String.valueOf(lastREDKnownLocation.getLongitude());
                        /*MarkerOptions userMarker = new MarkerOptions().position(userLocation).title("User Location");
                        userMarker.icon(BitmapDescriptorFactory.fromResource(R.drawable.usericon));
                        mMap.addMarker(userMarker);*/
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 15));
                        CargarUbicacionCocheras cargarUbicacionCocheras = new CargarUbicacionCocheras();
                        cargarUbicacionCocheras.execute(getUrl());
                    }

                    else {
                        Toast.makeText(getApplication(), "Hubo un problema al obtener la ubicacion actual por Red", Toast.LENGTH_SHORT).show();
                    }
                }
                 if (isGPSEnabled) {
                    // ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
                    Location lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                    mMap.setMyLocationEnabled(true);

                    if (lastKnownLocation!=null) {
                        LatLng userLocation = new LatLng(lastKnownLocation.getLatitude(), lastKnownLocation.getLongitude());
                        ultimaPosicion = userLocation;
                        lat = String.valueOf(lastKnownLocation.getLatitude());
                        lon = String.valueOf(lastKnownLocation.getLongitude());
                        /*MarkerOptions userMarker = new MarkerOptions().position(userLocation).title("User Location");
                        userMarker.icon(BitmapDescriptorFactory.fromResource(R.drawable.usericon));
                        mMap.addMarker(userMarker);*/
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 15));
                        CargarUbicacionCocheras cargarUbicacionCocheras = new CargarUbicacionCocheras();
                        cargarUbicacionCocheras.execute(getUrl());
                    }
                    else
                        Toast.makeText(getApplication(),"Hubo un problema al obtener la ubicacion actual", Toast.LENGTH_SHORT).show();

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
        mapView = mapFragment.getView(); //Esto es parte para reposicionar el location button
        mapFragment.getMapAsync(this);

        mOpcionesTitles = getResources().getStringArray(R.array.opciones_array);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);

        mDrawerList.setAdapter(new ArrayAdapter<String>(this,
                R.layout.drawer_list_item, mOpcionesTitles));
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());
        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

    }

    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            selectItem(position);
        }
    }

    private void selectItem(int position) {
        mDrawerList.setItemChecked(position, true);
        if (position == 0) {
            Intent sobreNosotros = new Intent(MapsActivity.this, SobreNosotros.class);
            sobreNosotros.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            sobreNosotros.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK); // clears all previous activities task
            startActivity(sobreNosotros);
        }
        if (position == 1) {
            Intent terminosCondiciones = new Intent(MapsActivity.this, TerminosCondiciones.class);
            terminosCondiciones.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            terminosCondiciones.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK); // clears all previous activities task
            startActivity(terminosCondiciones);
        }
        if (position == 2) {
            Intent tutorial = new Intent(MapsActivity.this, Tutorial.class);
            tutorial.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            tutorial.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK); // clears all previous activities task
            startActivity(tutorial);
        }
        mDrawerLayout.closeDrawer(mDrawerList);
    }

    /*public static class InfoFragment extends android.app.Fragment {
        public static final String ARG_INFOR_NUMBER = "info_number";

        public InfoFragment() {
        }

        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.servicio_detalle, container, false);
            int i = getArguments().getInt(ARG_INFOR_NUMBER);

            return rootView;
        }

    }*/


    public class CargarUbicacionCocheras extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... urls) {
            String result = "";
            String a = null;
            URL url;
            HttpURLConnection urlConnection = null;
            try {
                url = new URL(urls[0]);
                urlConnection = (HttpURLConnection) url.openConnection();
                InputStream in = urlConnection.getInputStream();

                InputStreamReader reader = new InputStreamReader(in);
                int data = reader.read();
                while (data != -1) {
                    char current = (char) data;
                    result += current;
                    data = reader.read();
                }/*
                InputStream is = getAssets().open("muchasCocheras.json");
                int size = is.available();
                byte[] buffer = new byte[size];
                is.read(buffer);
                is.close();
                a = new String(buffer, "UTF-8");
                Log.i("JSON", result);*/

            } catch (MalformedURLException e) {
                e.printStackTrace();

            } catch (IOException ex) {
                ex.printStackTrace();
            }
            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            String ss = " ";
            try {

                JSONArray lugares = new JSONArray(result);
                ArrayList<Cochera> listaCocheras = new ArrayList<Cochera>();

                for (int i = 0; i < lugares.length(); i++) {
                    Cochera coch = new Cochera();
                    JSONObject jsonObject = lugares.getJSONObject(i);
                    if (jsonObject.getBoolean("status")) {
                        JSONObject location = jsonObject.getJSONObject("location");
                        JSONArray servicios = new JSONArray(jsonObject.getString("services"));
                        JSONArray coordenada = new JSONArray(location.getString("coordinates"));

                        double latitud = Double.parseDouble(coordenada.get(0).toString());
                        double longitud = Double.parseDouble(coordenada.get(1).toString());

                        coch.setLatitud(latitud);
                        coch.setLongitud(longitud);
                        coch.setCapacidad(jsonObject.getString("capacity"));
                        coch.setNombre(jsonObject.getString("name"));
                        coch.setTelefono(jsonObject.getString("phone_number"));
                        coch.setDireccion(jsonObject.getString("address"));
                        coch.setUrl_image(jsonObject.getString("url_image"));
                        coch.setDescripcion(jsonObject.getString("description"));
                        Integer x = Integer.valueOf(jsonObject.getString("current_used"));
                        Integer capacidadActual = Integer.valueOf(jsonObject.getString("capacity")) - x;
                        coch.setCuposTomados(jsonObject.getString("current_used"));
                        listaCocheras.add(coch);
                        ArrayList<Servicio> listaservicios = new ArrayList<Servicio>();
                        for (int j = 0; j < servicios.length(); j++) {
                            JSONObject servicio = servicios.getJSONObject(j);

                            if (servicio.getBoolean("status") == true) {
                                Servicio service = new Servicio();
                                service.setNombre(servicio.getString("name"));
                                service.setPrecio(servicio.getString("cost_hour"));
                                listaservicios.add(service);

                            }

                        }
                        coch.setListaServicio(listaservicios);
                        if (capacidadActual > 30) {
                            Marker mark = mMap.addMarker(new MarkerOptions().title(jsonObject.getString("name")).position(new LatLng(latitud,
                                            longitud)).snippet("Cupos:" + capacidadActual.toString()
                                    ).icon(BitmapDescriptorFactory.fromResource(R.drawable.coche))
                            );
                            mark.setTag(coch);
                        }  else if(capacidadActual<=30 && capacidadActual>11)  {
                            Marker mark = mMap.addMarker(new MarkerOptions().title(jsonObject.getString("name")).position(new LatLng(latitud,
                                            longitud)).snippet("Cupos : " + capacidadActual.toString()
                                    ).icon(BitmapDescriptorFactory.fromResource(R.drawable.parking))
                            );
                            mark.setTag(coch);
                        }   else if(capacidadActual<11){
                            Marker mark = mMap.addMarker(new MarkerOptions().title(jsonObject.getString("name")).position(new LatLng(latitud,
                                            longitud)).snippet("Cupos : " + capacidadActual.toString()
                                    ).icon(BitmapDescriptorFactory.fromResource(R.drawable.aparcamiento))
                            );
                            mark.setTag(coch);
                        }

                    }
                }

            } catch (JSONException e) {
                Toast.makeText(getApplication(), "No se pudo cargar las cocheras ", Toast.LENGTH_SHORT).show();
            }
        }
    }
    public class CargarRuta extends AsyncTask<String, Void, String>{

        @Override
        protected String doInBackground(String... strings) {
            String data = "";

            try {
                data = downloadUrl(strings[0]);
            } catch (Exception e) {
                Log.d("Background Task", e.toString());
            }
            return data;
        }
        @Override
        protected void onPostExecute(String data){
            super.onPostExecute(data);
            ParserTask parserTask = new ParserTask();
            parserTask.execute(data);
        }
    }

    private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {

        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {
            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;
            try {
                jObject = new JSONObject(jsonData[0]);
                DirectionsJSONParser parser = new DirectionsJSONParser();

                routes = parser.parse(jObject);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return routes;
        }

        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> result) {
            try {


                ArrayList points = null;
                PolylineOptions lineOptions = null;
                MarkerOptions markerOptions = new MarkerOptions();

                for (int i = 0; i < result.size(); i++) {
                    points = new ArrayList();
                    lineOptions = new PolylineOptions();

                    List<HashMap<String, String>> path = result.get(i);

                    for (int j = 0; j < path.size(); j++) {
                        HashMap<String, String> point = path.get(j);

                        double lat = Double.parseDouble(point.get("lat"));
                        double lng = Double.parseDouble(point.get("lng"));
                        LatLng position = new LatLng(lat, lng);

                        points.add(position);
                    }

                    lineOptions.addAll(points);
                    lineOptions.width(5);
                    lineOptions.color(Color.BLACK);
                    lineOptions.geodesic(true);

                }
                polyline = mMap.addPolyline(lineOptions);
            } catch (Exception e) {
                Toast.makeText(getApplication(), "Hubo un problema al cargar la ruta", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(strUrl);

            urlConnection = (HttpURLConnection) url.openConnection();

            urlConnection.connect();

            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

            StringBuffer sb = new StringBuffer();

            String line = "";
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

            data = sb.toString();

            br.close();

        } catch (Exception e) {
            Log.d("Exception", e.toString());
        } finally {
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }




/*    void createMarkersFromJson(JSONArray json) throws  JSONException{

        for(int i=0; i<json.length();i++){
            JSONObject jsonObject= json.getJSONObject(i);
            JSONObject coordenada= jsonObject.getJSONObject("coordenada");
            double latitud= Double.parseDouble(jsonObject.getString("lat"));
            double longitud=Double.parseDouble(jsonObject.getString("lng"));
            mMap.addMarker(new MarkerOptions().title("Punto").position(new LatLng(latitud,longitud)));
        }
    }*/

    @Override
    public void onMapReady(GoogleMap googleMap) {
        MultiDex.install(this);
        mMap = googleMap;
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
        } else
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        // Location lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

        //locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {

                /*LatLng userLocation = new LatLng(location.getLatitude(), location.getLongitude());
                MarkerOptions userMarker = new MarkerOptions().position(userLocation).title("User Location");
                userMarker.icon(BitmapDescriptorFactory.fromResource(R.drawable.usericon));
                mMap.addMarker(userMarker);
                ultimaPosicion = userLocation;
                /*mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation,10));*/

            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {
                if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {


                }

                /*if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    CargarUbicacionCocheras cargarUbicacionCocheras= new CargarUbicacionCocheras();
                    cargarUbicacionCocheras.execute(getUrl());

                }*/
            }

            @Override
            public void onProviderDisabled(String s) {

            }
        };

        if (Build.VERSION.SDK_INT < 23) {

            /*if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }*/
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
            mMap.setMyLocationEnabled(true);
            Location lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            if(lastKnownLocation!=null) {
                LatLng userLocation = new LatLng(lastKnownLocation.getLatitude(), lastKnownLocation.getLongitude());
                ultimaPosicion = userLocation;
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 15));
                setLat(Double.toString(userLocation.latitude));
                setLon(Double.toString(userLocation.longitude));
                CargarUbicacionCocheras cargarUbicacionCocheras = new CargarUbicacionCocheras();
                cargarUbicacionCocheras.execute(getUrl());
            }
            else {
                Toast.makeText(getApplication(), "Hubo un problema al cargar las cocheras", Toast.LENGTH_SHORT).show();

            }

        } else {

            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {


                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);

                //locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);

                boolean isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
                boolean isREDEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);


                if (isREDEnabled){
                    Location lastREDKnownLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                    if (lastREDKnownLocation!=null){
                        LatLng userLocation = new LatLng(lastREDKnownLocation.getLatitude(), lastREDKnownLocation.getLongitude());
                        mMap.setMyLocationEnabled(true);

                        ultimaPosicion = userLocation;
                        lat = String.valueOf(lastREDKnownLocation.getLatitude());
                        lon = String.valueOf(lastREDKnownLocation.getLongitude());
                        /*MarkerOptions userMarker = new MarkerOptions().position(userLocation).title("User Location");
                        userMarker.icon(BitmapDescriptorFactory.fromResource(R.drawable.usericon));
                        mMap.addMarker(userMarker);
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 15));*/
                        CargarUbicacionCocheras cargarUbicacionCocheras = new CargarUbicacionCocheras();
                        cargarUbicacionCocheras.execute(getUrl());
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 15));

                    }

                    else {
                    Toast.makeText(getApplication(), "Hubo un problema al obtener la ubicacion actual por Red", Toast.LENGTH_SHORT).show();
                    }
                }
                else if (isGPSEnabled) {
                    // ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
                    Location lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                    if (lastKnownLocation!=null) {
                        LatLng userLocation = new LatLng(lastKnownLocation.getLatitude(), lastKnownLocation.getLongitude());
                        ultimaPosicion = userLocation;
                        lat = String.valueOf(lastKnownLocation.getLatitude());
                        lon = String.valueOf(lastKnownLocation.getLongitude());
                        mMap.setMyLocationEnabled(true);

                        /*MarkerOptions userMarker = new MarkerOptions().position(userLocation).title("User Location");
                        userMarker.icon(BitmapDescriptorFactory.fromResource(R.drawable.usericon));
                        mMap.addMarker(userMarker);*/
                        CargarUbicacionCocheras cargarUbicacionCocheras = new CargarUbicacionCocheras();
                        cargarUbicacionCocheras.execute(getUrl());
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 15));


                    }
                    else
                        Toast.makeText(getApplication(),"Hubo un problema al obtener la ubicacion actual", Toast.LENGTH_SHORT).show();

                }
                else
                    Toast.makeText(getApplication(),"Por favor Encienda un Servicio de Ubicacion ", Toast.LENGTH_SHORT).show();
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
               // mMap.setMyLocationEnabled(true);


            }


        }
//        Esto es para reposicionar el location button para que vaya en la parte de abajo a la derecha
        View locationButton = ((View) mapView.findViewById(Integer.parseInt("1")).getParent()).findViewById(Integer.parseInt("2"));
        RelativeLayout.LayoutParams rlp = (RelativeLayout.LayoutParams) locationButton.getLayoutParams();
        // position on right bottom
        rlp.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0);
        rlp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);rlp.setMargins(0,0,30,30);
//      hasta Aqui

//      Esto es para reposicionar el locationCompass  button para que vaya en la parte de abajo a la izquierda
        if (mapView != null &&
                mapView.findViewById(Integer.parseInt("1")) != null) {
            // Get the view
            View locationCompass = ((View) mapView.findViewById(Integer.parseInt("1")).getParent()).findViewById(Integer.parseInt("5"));
            // and next place it, on bottom right (as Google Maps app)
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams)
                    locationCompass.getLayoutParams();
            // position on bottom - left
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, 0);
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0);
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_END, 0);
            layoutParams.addRule(RelativeLayout.ALIGN_END, 0);
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
            layoutParams.setMargins(30, 0, 0, 40);
        }

        mMap.getUiSettings().setMapToolbarEnabled(false);
        //Recuerda que puedes eliminarlos gestos de google Maps como eran estos 2 que me direccionaban a la aplicacion de google maps

        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            public void onInfoWindowClick(Marker marker) {
                if(!marker.getTitle().equals("User Location")) {
                    Cochera info = (Cochera) marker.getTag();
                    /*Intent intent = new Intent(MapsActivity.this, DetalleServicio.class);
                    intent.putExtra("Cochera", info);*/
                        Intent intent = new Intent(MapsActivity.this, ContainerCocheraActivity.class);
                    intent.putExtra("Cochera", info);
                    startActivity(intent);

                }

            }
        });

        /*mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {

            String  url= getDirectionsUrl(latLng,ultimaPosicion);
                CargarRuta cargarRuta=new CargarRuta();
                cargarRuta.execute(url);
            }
        });*/

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                try{
                    if(polyline!=null){
                        polyline.remove();
                    }
                }catch (Exception e){
                    Toast.makeText(getApplication(),"Ocurrió un problema", Toast.LENGTH_SHORT).show();
                }

                try {
                    String  url= getDirectionsUrl(ultimaPosicion,marker.getPosition());
                    CargarRuta cargarRuta=new CargarRuta();
                    cargarRuta.execute(url);
                } catch (Exception e){
                    Toast.makeText(getApplication(),"Error al cargar la ruta ", Toast.LENGTH_SHORT).show();
                }

                return false;
            }
        });




    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
    }

}
