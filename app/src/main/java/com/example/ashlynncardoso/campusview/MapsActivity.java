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
        mMap.addMarker(new MarkerOptions().icon(icon).position(new LatLng(34.412132, -119.848838)).title("Art, Design and Architecture Museum"));
        mMap.addMarker(new MarkerOptions().icon(icon).position(new LatLng(34.411791, -119.848462)).title("UCSB Bookstore"));
        mMap.addMarker(new MarkerOptions().icon(icon).position(new LatLng(34.411539, -119.848027)).title("University Center"));
        mMap.addMarker(new MarkerOptions().icon(icon).position(new LatLng(34.416358, -119.845287)).title("Campbell Hall"));
        mMap.addMarker(new MarkerOptions().icon(icon).position(new LatLng(34.416402, -119.844633)).title("Phelps Hall"));
        mMap.addMarker(new MarkerOptions().icon(icon).position(new LatLng(34.415720, -119.845266)).title("Ellison Hall"));
        mMap.addMarker(new MarkerOptions().icon(icon).position(new LatLng(34.415467, -119.843579)).title("Physical Science Building North"));
        mMap.addMarker(new MarkerOptions().icon(icon).position(new LatLng(34.415430, -119.844577)).title("Buchanan Hall"));
        mMap.addMarker(new MarkerOptions().icon(icon).position(new LatLng(34.416038, -119.843536)).title("Department of Chemistry and Biochemistry"));
        mMap.addMarker(new MarkerOptions().icon(icon).position(new LatLng(34.410084, -119.852687)).title("Carrillo Dining Commons"));
        mMap.addMarker(new MarkerOptions().icon(icon).position(new LatLng(34.411053, -119.847011)).title("Ortega Dining Commons"));
        mMap.addMarker(new MarkerOptions().icon(icon).position(new LatLng(34.409712, -119.845026)).title("De La Guerra Dining Commons"));
        mMap.addMarker(new MarkerOptions().icon(icon).position(new LatLng(34.409522, -119.852240)).title("Manzanita Village Residence Hall"));
        mMap.addMarker(new MarkerOptions().icon(icon).position(new LatLng(34.411067, -119.852900)).title("San Rafael Residence Hall"));
        mMap.addMarker(new MarkerOptions().icon(icon).position(new LatLng(34.410296, -119.846783)).title("San Miguel Residence Hall"));
        mMap.addMarker(new MarkerOptions().icon(icon).position(new LatLng(34.409557, -119.846123)).title("San Nicolas Residence Hall"));
        mMap.addMarker(new MarkerOptions().icon(icon).position(new LatLng(34.409902, -119.843511)).title("Santa Cruz Residence Hall"));
        mMap.addMarker(new MarkerOptions().icon(icon).position(new LatLng(34.411079, -119.842958)).title("Anacapa Residence Hall"));
        mMap.addMarker(new MarkerOptions().icon(icon).position(new LatLng(34.411274, -119.845243)).title("Santa Rosa Residence Hall"));
        mMap.addMarker(new MarkerOptions().icon(icon).position(new LatLng(34.413872, -119.847576)).title("South Hall"));
        mMap.addMarker(new MarkerOptions().icon(icon).position(new LatLng(34.413624, -119.846718)).title("Girvetz Hall"));
        mMap.addMarker(new MarkerOptions().icon(icon).position(new LatLng(34.413770, -119.845532)).title("UCSB Library"));
        mMap.addMarker(new MarkerOptions().icon(icon).position(new LatLng(34.411170, -119.845844)).title("College of Creative Studies"));
        mMap.addMarker(new MarkerOptions().icon(icon).position(new LatLng(34.413978, -119.841336)).title("Harold Frank Hall"));
        mMap.addMarker(new MarkerOptions().icon(icon).position(new LatLng(34.414133, -119.843117)).title("Broida Hall"));
        mMap.addMarker(new MarkerOptions().icon(icon).position(new LatLng(34.414838, -119.841256)).title("Engineering II"));
        mMap.addMarker(new MarkerOptions().icon(icon).position(new LatLng(34.414838, -119.841256)).title("Mosher Alumni House"));


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
            if(building.contains(buildingName + ":")){
                intent.putExtra("building", building);
                bundle.putParcelable("destination", marker.getPosition());
                intent.putExtra("bundle", bundle);
                bundle.putParcelable("current", currentMarker.getPosition());
                startActivityForResult(intent, BUILDING_REQUEST);
                return true;
            }
        }
        return false;
    }
}
