package com.example.ashlynncardoso.campusview;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.Button;

import android.location.LocationListener;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, LocationListener, OnMarkerClickListener {

    private GoogleMap mMap;

    private static final float DEFAULT_ZOOM = 17f;
    private static final LatLng DEFAULT_LOCATION = new LatLng(34.414051, -119.849033);
    private static final int BUILDING_REQUEST = 1;
    private static final int PERMISSIONS_REQUEST_LOCATION = 5;
    private static LocationManager locationManager = null;
    private Marker currentMarker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        Button getLocationBtn = (Button) findViewById(R.id.getLocationBtn);
        TextView locationText = (TextView)findViewById(R.id.locationText);

        getLocationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getLocation();
            }
        });
    }


    @Override
    public void onMapReady(GoogleMap map) {
        mMap = map;
        getLocation();
        mMap.setOnMarkerClickListener(MapsActivity.this);

        if(currentMarker != null) {
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentMarker.getPosition(), DEFAULT_ZOOM));
        }
        else {
            currentMarker = mMap.addMarker(new MarkerOptions().position(DEFAULT_LOCATION).title("UCSB"));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(DEFAULT_LOCATION, DEFAULT_ZOOM));
        }
        setUpMarkers();
    }

    private void getLocation() {
        try {
            if (ContextCompat.checkSelfPermission(MapsActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {
                // Permission is not granted
                if (ActivityCompat.shouldShowRequestPermissionRationale(MapsActivity.this,
                        Manifest.permission.ACCESS_FINE_LOCATION)) {
                    // Show an explanation to the user *asynchronously* -- don't block
                    // this thread waiting for the user's response! After the user
                    // sees the explanation, try again to request the permission.
                } else {
                    //request the permission
                    ActivityCompat.requestPermissions(MapsActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSIONS_REQUEST_LOCATION);
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
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay!
                    getLocation();
                }
            }
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        LatLng current = new LatLng(location.getLatitude(), location.getLongitude());
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(current, DEFAULT_ZOOM));
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
        Toast.makeText(MapsActivity.this, "Please Enable GPS and Internet", Toast.LENGTH_SHORT).show();
    }

    private void setUpMarkers() {
        BitmapDescriptor icon = BitmapDescriptorFactory.fromResource(R.drawable.blue_marker);
        mMap.addMarker(new MarkerOptions().icon(icon).position(new LatLng(34.412698, -119.848375)).title("Storke Tower"));
        mMap.addMarker(new MarkerOptions().icon(icon).position(new LatLng(34.412774, -119.852583)).title("Student Resource Building"));
        mMap.addMarker(new MarkerOptions().icon(icon).position(new LatLng(34.413867, -119.851370)).title("Events Center Thunderdome"));
        mMap.addMarker(new MarkerOptions().icon(icon).position(new LatLng(34.412672, -119.850893)).title("Theater and Dance Building"));
        mMap.addMarker(new MarkerOptions().icon(icon).position(new LatLng(34.413548, -119.850340)).title("Humanities and Social Sciences Building"));
        mMap.addMarker(new MarkerOptions().icon(icon).position(new LatLng(34.412504, -119.849294)).title("Arts Building"));
        mMap.addMarker(new MarkerOptions().icon(icon).position(new LatLng(34.412132, -119.848838)).title("Art Design and Architecture Museum"));
        mMap.addMarker(new MarkerOptions().icon(icon).position(new LatLng(34.411791, -119.848462)).title("UCSB Bookstore"));
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        Resources res = getResources();
        String[] buildings = res.getStringArray(R.array.buildings);
        Intent intent = new Intent(this, BuildingActivity.class);
        //get name of building from marker
        String buildingName = marker.getTitle();
        Bundle bundle = new Bundle();
        for(String building: buildings){
            if(building.contains(buildingName)){
                intent.putExtra("building", building);
                bundle.putParcelable("latlng", marker.getPosition());
                intent.putExtra("bundle", bundle);
                startActivityForResult(intent, BUILDING_REQUEST);
                return true;
            }
        }
        return false;
    }
}
