package com.robpercival.maplocationdemo.Activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.View;
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
import com.robpercival.maplocationdemo.Activity.Container.ContainerCocheraActivity;
import com.robpercival.maplocationdemo.Model.Cochera;
import com.robpercival.maplocationdemo.Model.Servicio;
import com.robpercival.maplocationdemo.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
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
    private String lat;
    private String lon;
    private String url;
    private LatLng ultimaPosicion;

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
        return "http://54.89.248.15:3000/parking/nearby?lat=" + getLat() + "&lng=" + getLon();
    }

    public void setUrl(String url) {
        this.url = url;
    }


    public void actualizar(View view) {

        if (Build.VERSION.SDK_INT < 23) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
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
        } else if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            boolean isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            if (isGPSEnabled) {

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

                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
                locationManager.requestSingleUpdate(LocationManager.GPS_PROVIDER, locationListener, null);

                Location lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                CargarUbicacionCocheras cargarUbicacionCocheras = new CargarUbicacionCocheras();
                cargarUbicacionCocheras.execute(getUrl());

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
            Intent meq = new Intent(MapsActivity.this, SobreNosotros.class);
            startActivity(meq);
        }
        if (position == 1) {
            Intent meq = new Intent(MapsActivity.this, SobreNosotros.class);
            startActivity(meq);
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
                /*url = new URL(urls[0]);
                urlConnection = (HttpURLConnection) url.openConnection();
                InputStream in = urlConnection.getInputStream();

                InputStreamReader reader = new InputStreamReader(in);
                int data = reader.read();
                while (data != -1) {
                    char current = (char) data;
                    result += current;
                    data = reader.read();
                }*/
                InputStream is = getAssets().open("muchasCocheras.json");
                int size = is.available();
                byte[] buffer = new byte[size];
                is.read(buffer);
                is.close();
                a = new String(buffer, "UTF-8");
                Log.i("JSON", result);

            } catch (MalformedURLException e) {
                e.printStackTrace();

            } catch (IOException ex) {
                ex.printStackTrace();
            }
            return a;
        }

        @Override
        protected void onPostExecute(String a) {
            super.onPostExecute(a);
            String ss = " ";
            try {

                JSONArray lugares = new JSONArray(a);
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
                        if (x < 15) {
                            Marker mark = mMap.addMarker(new MarkerOptions().title(jsonObject.getString("name")).position(new LatLng(latitud,
                                            longitud)).snippet("Cupos:" + capacidadActual.toString()
                                    ).icon(BitmapDescriptorFactory.fromResource(R.drawable.coche))
                            );
                            mark.setTag(coch);
                        } else {
                            Marker mark = mMap.addMarker(new MarkerOptions().title(jsonObject.getString("name")).position(new LatLng(latitud,
                                            longitud)).snippet("Cupos : " + jsonObject.getString("current_used")
                                    ).icon(BitmapDescriptorFactory.fromResource(R.drawable.parking))
                            );
                            mark.setTag(coch);
                        }
                    }
                }

            } catch (JSONException e) {
                Toast.makeText(getApplication(), "No se pudo cargar las cocheras ", Toast.LENGTH_SHORT).show();
            }
            Toast.makeText(getApplication(), getUrl(), Toast.LENGTH_SHORT).show();
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
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            mMap.setMyLocationEnabled(true);
        } else
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
             mMap.setMyLocationEnabled(true);
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

                    locationManager.requestSingleUpdate(LocationManager.GPS_PROVIDER, locationListener, null);

                }
            }

            @Override
            public void onProviderDisabled(String s) {

            }
        };

        if (Build.VERSION.SDK_INT < 23) {

            /*if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }*/
            Location lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            LatLng userLocation = new LatLng(lastKnownLocation.getLatitude(), lastKnownLocation.getLongitude());
            ultimaPosicion = userLocation;
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 15));
            setLat(Double.toString(userLocation.latitude));
            setLon(Double.toString(userLocation.longitude));
            CargarUbicacionCocheras cargarUbicacionCocheras = new CargarUbicacionCocheras();
            cargarUbicacionCocheras.execute(getUrl());

        } else {

            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {


                locationManager.requestSingleUpdate(LocationManager.GPS_PROVIDER, locationListener, null);

                //locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);

                boolean isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
                if (isGPSEnabled) {
                    // ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
                    Location lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                    if (lastKnownLocation!=null) {
                        Location lastKnowRED = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
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
                else
                    Toast.makeText(getApplication(),"Por favor Encienda el Servicio de GPS ", Toast.LENGTH_SHORT).show();
            } else {
                 ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
                /*locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
                Location lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                LatLng userLocation = new LatLng(lastKnownLocation.getLatitude(), lastKnownLocation.getLongitude());
                lat = String.valueOf(lastKnownLocation.getLatitude());
                lon = String.valueOf(lastKnownLocation.getLongitude());
                MarkerOptions userMarker= new MarkerOptions().position(userLocation).title("User Location");
                userMarker.icon(BitmapDescriptorFactory.fromResource( R.drawable.usericon));
                mMap.addMarker(userMarker);
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation,10));
                CargarUbicacionCocheras cargarUbicacionCocheras= new CargarUbicacionCocheras();
                cargarUbicacionCocheras.execute(getUrl());*/
            }


        }


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



    }

}
