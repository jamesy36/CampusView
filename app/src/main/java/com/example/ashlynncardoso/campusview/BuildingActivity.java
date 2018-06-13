package com.example.ashlynncardoso.campusview;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class BuildingActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private static final int ROUTE_REQUEST = 3;
    private Button getRoute;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_building);

        TextView building = (TextView)findViewById(R.id.title);
        TextView description = (TextView)findViewById(R.id.textView);
        //pass the resource through intent??
        Bundle extras = getIntent().getExtras();
        String info = extras.getString("building");
        String[] split = info.split(":");
        building.setText(split[0]);
        description.setText(split[1]);

        getRoute = (Button) findViewById(R.id.button);
        getRoute.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                nextButton();
            }
        });

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    private void nextButton(){
        Intent intent = new Intent(this, RoutesActivity.class);

        Bundle bundle = getIntent().getParcelableExtra("bundle");
        LatLng dest = bundle.getParcelable("destination");
        LatLng curr = bundle.getParcelable("current");

        Bundle route = new Bundle();
        route.putParcelable("destination", dest);
        route.putParcelable("current", curr);
        intent.putExtra("bundle", bundle);

        startActivityForResult(intent, ROUTE_REQUEST);
    }


    @Override
    public void onMapReady(GoogleMap map) {
        mMap = map;

        Bundle bundle = getIntent().getParcelableExtra("bundle");
        LatLng current = bundle.getParcelable("destination");
        String buildingName = getIntent().getExtras().getString("building");

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(current, 18f));
        mMap.setMapType(mMap.MAP_TYPE_SATELLITE);
        mMap.addMarker(new MarkerOptions().position(current).title(buildingName));
    }

}