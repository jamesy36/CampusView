package com.example.ashlynncardoso.campusview;

import android.app.Dialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.location.FusedLocationProviderClient;
import android.location.Location;
import com.google.android.gms.tasks.OnSuccessListener;

import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.places.PlaceDetectionClient;
import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;

import android.location.LocationManager;

public class MainActivity extends AppCompatActivity{

    private Button getLocation;
    private Button getMap;
    private static final String TAG = "MainActivity";
    private static final int ERROR_REQUEST = 9001;
    int PLACE_PICKER_REQUEST=1;
    private FusedLocationProviderClient mFusedLocationClient;
    private LocationRequest mLocationRequest;
    private static final int PERMISSIONS_REQUEST_LOCATION = 5;
    private LatLng start;
    private static LocationManager locationManager = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        if(isService()){
            init();
        }

    }

    private void init(){
        getLocation = (Button) findViewById(R.id.pop_button);
        getLocation.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                nextButton();
            }
        });
        getMap = (Button) findViewById((R.id.loc_button));
        getMap.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                getMap();
            }
        } );

    }

    private void getMap(){
        PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
        Intent intent;

        try{
            intent = builder.build(MainActivity.this);
            startActivityForResult(intent,PLACE_PICKER_REQUEST);
        }
        catch(GooglePlayServicesNotAvailableException e){
            e.printStackTrace();
        }
        catch(GooglePlayServicesRepairableException e){
            e.printStackTrace();
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if(requestCode==PLACE_PICKER_REQUEST){
            if(resultCode==RESULT_OK){
                Place place = PlacePicker.getPlace(this,data);
                String address= String.format("Place %s", place.getLatLng());

                LatLng end = place.getLatLng();

                try {
                    if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)
                            != PackageManager.PERMISSION_GRANTED) {
                        // Permission is not granted
                        if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,
                                Manifest.permission.ACCESS_FINE_LOCATION)) {
                            // Show an explanation to the user asynchronously -- don't block
                            // this thread waiting for the user's response! After the user
                            // sees the explanation, try again to request the permission.
                        } else {
                            //request the permission
                            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSIONS_REQUEST_LOCATION);
                        }
                    }
//                    locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
//                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 5, (android.location.LocationListener) this);
                    mFusedLocationClient.getLastLocation()
                            .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                                @Override
                                public void onSuccess(Location location) {
                                    // Got last known location. In some rare situations this can be null.
                                    if (location != null) {
                                        double latitude = location.getLatitude();
                                        double longitude = location.getLongitude();
                                        start = new LatLng(latitude, longitude);
                                    }

                                }
                            });

                }
                catch(SecurityException e) {
                    e.printStackTrace();
                }

                String address2= String.format("Place %s", start);
                Toast.makeText(this, address2, Toast.LENGTH_LONG).show();
            }
        }
    }


    private void nextButton(){
        Intent intent = new Intent(this, MapsActivity.class);
        startActivity(intent);
    }


//    private void getLocation() {
//        try {
//            if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)
//                    != PackageManager.PERMISSION_GRANTED) {
//                // Permission is not granted
//                if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,
//                        Manifest.permission.ACCESS_FINE_LOCATION)) {
//                    // Show an explanation to the user asynchronously -- don't block
//                    // this thread waiting for the user's response! After the user
//                    // sees the explanation, try again to request the permission.
//                } else {
//                    //request the permission
//                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSIONS_REQUEST_LOCATION);
//                }
//            }
//            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
//            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 5, (android.location.LocationListener) this);
//        }
//        catch(SecurityException e) {
//            e.printStackTrace();
//        }
//    }
//
//
//    @Override
//    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
//        switch (requestCode) {
//            case PERMISSIONS_REQUEST_LOCATION: {
//                // If request is cancelled, the result arrays are empty.
//                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                    // permission was granted, yay!
//                    getLocation();
//                }
//            }
//        }
//    }


    //It's probably a good idea to check if Google Services works or not...

    public boolean isService() {
        Log.d(TAG, "isService: Checking Google Services Version");
        int available = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(MainActivity.this);

        if (available == ConnectionResult.SUCCESS)

        {
            Log.d(TAG, "Google Play Services is Working!");
            return true;
        }
        else if(GoogleApiAvailability.getInstance().isUserResolvableError(available)){
            Log.d(TAG, "Error has occured but can be fixed!");
            Dialog dialog = GoogleApiAvailability.getInstance().getErrorDialog(MainActivity.this, available, ERROR_REQUEST);
            dialog.show();
        }
        else
        {
            Toast.makeText(this,"You can't make a map request", Toast.LENGTH_SHORT).show();

        }
        return false;
    }


}