package com.robpercival.maplocationdemo;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
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
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;


import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

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




   public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    LocationManager locationManager;
    LocationListener locationListener;
    private String[] mOpcionesTitles;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private CharSequence mTitle;
    static final String url = "http://54.164.83.170:3200/todaCocheraConServicios";


        public void actualizar(View view){
        mMap.clear();
        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            /*listaCocheras =new ArrayList<Cochera>(); */
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
            Location lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            LatLng userLocation = new LatLng(lastKnownLocation.getLatitude(), lastKnownLocation.getLongitude());
            MarkerOptions userMarker= new MarkerOptions().position(userLocation).title("User Location");
            userMarker.icon(BitmapDescriptorFactory.fromResource( R.drawable.usericon));
            mMap.addMarker(userMarker);
            CargarUbicacionCocheras cargarUbicacionCocheras= new CargarUbicacionCocheras();
            cargarUbicacionCocheras.execute("");
        }
    }

    public void menu(View view){
        mDrawerLayout.openDrawer(mDrawerList);
    }

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
        mOpcionesTitles = getResources().getStringArray(R.array.opciones_array);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);

        mDrawerList.setAdapter(new ArrayAdapter<String>(this,
                R.layout.drawer_list_item, mOpcionesTitles));
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());
    }

    private class DrawerItemClickListener implements ListView.OnItemClickListener {


        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            selectItem(position);

        }
    }
    private void selectItem(int position) {
        // Create a new fragment
         /* android.app.Fragment fragment = new android.app.Fragment();
        Bundle args= new Bundle();
        args.putInt(InfoFragment.ARG_INFOR_NUMBER, position);
        fragment.setArguments(args);
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();*/
        mDrawerList.setItemChecked(position,true);
        if(position==0) {
            Intent meq = new Intent(MapsActivity.this, SobreNosotros.class);
            startActivity(meq);
        }
        mDrawerLayout.closeDrawer(mDrawerList);

    }

    public static class InfoFragment extends android.app.Fragment{
        public static final String ARG_INFOR_NUMBER = "info_number";

        public InfoFragment(){
        }
        public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
            View rootView= inflater.inflate(R.layout.servicio_detalle,container,false);
            int i= getArguments().getInt(ARG_INFOR_NUMBER);

            return rootView;
        }

    }


    public class CargarUbicacionCocheras extends AsyncTask<String,Void,String>{

        @Override
        protected String doInBackground(String... urls) {
            String result = "";
            URL url;
            HttpURLConnection urlConnection= null;
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
                }
                Log.i("JSON", result);

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
             String a=null;
             String ss=" ";
            try {
                /* InputStream is = getAssets().open("variasCocheras.json");
                int size = is.available();
                byte[] buffer = new byte[size];
                is.read(buffer);
                is.close();
                a = new String(buffer, "UTF-8"); */
                JSONArray lugares= new JSONArray(result);
                ArrayList<Cochera> listaCocheras = new ArrayList<Cochera>();

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
                    Integer x =Integer.valueOf(jsonObject.getString("cupos_disp"));
                    listaCocheras.add(coch);
                    ArrayList<Servicio> listaservicios= new ArrayList<Servicio>() ;
                    for (int j=0; j<servicios.length();j++){
                        JSONObject servicio= servicios.getJSONObject(j);

                        if(servicio.getBoolean("estado")==true){
                                JSONObject detalleservicio = servicio.getJSONObject("tipoServicio");
                                Servicio service= new Servicio();
                                service.setNombre(detalleservicio.getString("nombre"));
                                service.setPrecio(servicio.getString("precio_hora"));
                                listaservicios.add(service);
                                if(x <15) {
                                    Marker mark = mMap.addMarker(new MarkerOptions().title(jsonObject.getString("nombre")).position(new LatLng(latitud,
                                                    longitud)).snippet("Cupos : " + jsonObject.getString("cupos_disp")
                                            ).icon(BitmapDescriptorFactory.fromResource(R.drawable.coche))
                                    );
                                    mark.setTag(listaservicios);
                                }

                                else {Marker mark = mMap.addMarker(new MarkerOptions().title(jsonObject.getString("nombre")).position(new LatLng(latitud,
                                                    longitud)).snippet("Cupos : " + jsonObject.getString("cupos_disp")
                                            ).icon(BitmapDescriptorFactory.fromResource(R.drawable.cochera))
                                    );
                                    mark.setTag(listaservicios);
                            }
                        }

                    }
                }

            } catch (JSONException e) {
                Toast.makeText(getApplication(),"No se pudo cargar", Toast.LENGTH_SHORT).show();
            }
         }
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
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation,10));

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


                Location lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                LatLng userLocation = new LatLng(lastKnownLocation.getLatitude(), lastKnownLocation.getLongitude());
                MarkerOptions userMarker = new MarkerOptions().position(userLocation).title("User Location");
                userMarker.icon(BitmapDescriptorFactory.fromResource(R.drawable.usericon));
                mMap.addMarker(userMarker);
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation,15));
                CargarUbicacionCocheras cargarUbicacionCocheras = new CargarUbicacionCocheras();
                cargarUbicacionCocheras.execute(url);

        } else {

            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
                Location lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                LatLng userLocation = new LatLng(lastKnownLocation.getLatitude(), lastKnownLocation.getLongitude());
                MarkerOptions userMarker = new MarkerOptions().position(userLocation).title("User Location");
                userMarker.icon(BitmapDescriptorFactory.fromResource(R.drawable.usericon));
                mMap.addMarker(userMarker);
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation,15));
                CargarUbicacionCocheras cargarUbicacionCocheras= new CargarUbicacionCocheras();
                cargarUbicacionCocheras.execute(url);


            } else {

                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
                Location lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                LatLng userLocation = new LatLng(lastKnownLocation.getLatitude(), lastKnownLocation.getLongitude());
                MarkerOptions userMarker= new MarkerOptions().position(userLocation).title("User Location");
                userMarker.icon(BitmapDescriptorFactory.fromResource( R.drawable.usericon));
                mMap.addMarker(userMarker);
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation,10));
                CargarUbicacionCocheras cargarUbicacionCocheras= new CargarUbicacionCocheras();
                cargarUbicacionCocheras.execute(url);
        }


        }
        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            public void onInfoWindowClick(Marker marker) {
                if(!marker.getTitle().equals("User Location")) {
                    ArrayList<Servicio> info = (ArrayList<Servicio>) marker.getTag();
                    Intent intent = new Intent(MapsActivity.this, DetalleServicio.class);
                    intent.putExtra("LISTA", info);
                    startActivity(intent);
                }

            }
        });



    }

}
