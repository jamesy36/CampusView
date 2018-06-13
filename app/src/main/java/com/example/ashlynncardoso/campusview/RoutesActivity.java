package com.example.ashlynncardoso.campusview;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.icu.util.Output;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.google.android.gms.maps.model.Marker;
import com.google.maps.android.PolyUtil;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.DirectionsApi;
import com.google.maps.GeoApiContext;
import com.google.maps.errors.ApiException;
import com.google.maps.model.DirectionsResult;
import com.google.maps.model.TravelMode;

import org.joda.time.DateTime;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import com.google.android.gms.maps.model.PolylineOptions;

public class RoutesActivity extends AppCompatActivity implements OnMapReadyCallback, LocationListener {

    private GoogleMap mMap;
    private static LocationManager locationManager = null;
    private Marker currentMarker;
    private Marker destMarker;
    private static final int PERMISSIONS_REQUEST_LOCATION = 5;
    private static final String GOOGLE_ROUTES_KEY = "AIzaSyDEhmBLLL2wEi_m6BgGq7iFNgmruNDpMBY";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_routes);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        Bundle bundle = getIntent().getParcelableExtra("bundle");
        LatLng curr = bundle.getParcelable("current");
        LatLng dest = bundle.getParcelable("destination");

        if(destMarker != null) {
            destMarker.remove();
        }
        destMarker = mMap.addMarker(new MarkerOptions().position(dest).title("Destination"));

        String current = "origin=" + curr.toString();
        String destination = "destination=" + dest.toString();
        String key = "key=" + GOOGLE_ROUTES_KEY;
        String mode = "mode=walking";
        final String fullURL = "https://maps.googleapis.com/maps/api/directions/json?" + current + "&" + destination + "&" + key + "&" + mode;


        Thread thread = new Thread(new Runnable(){
            public void run() {
                try {
                    URL url = null;
                    OutputStream out = null;
                    URLConnection urlConnection = null;
                    url = new URL(fullURL);
                    HttpURLConnection request = (HttpURLConnection) (url.openConnection());
                    request.setRequestMethod("POST");
                    request.connect();
                    OutputStreamWriter writer = new OutputStreamWriter(request.getOutputStream());
                    String post = null;
                    writer.write(post);
                    writer.flush();


                    TextView description = (TextView)findViewById(R.id.textView);
                    description.setText(post);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

    }



    private void getLocation() {
        try {
            if (ContextCompat.checkSelfPermission(RoutesActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {
                // Permission is not granted
                if (ActivityCompat.shouldShowRequestPermissionRationale(RoutesActivity.this,
                        Manifest.permission.ACCESS_FINE_LOCATION)) {
                    // Show an explanation to the user *asynchronously* -- don't block
                    // this thread waiting for the user's response! After the user
                    // sees the explanation, try again to request the permission.
                } else {
                    //request the permission
                    ActivityCompat.requestPermissions(RoutesActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSIONS_REQUEST_LOCATION);
                }
            }
            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 5, this);
        }
        catch(SecurityException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        LatLng current = new LatLng(location.getLatitude(), location.getLongitude());
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(current, 18f));
        currentMarker.remove();
        currentMarker = mMap.addMarker(new MarkerOptions().position(current).title("You are here"));
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}
