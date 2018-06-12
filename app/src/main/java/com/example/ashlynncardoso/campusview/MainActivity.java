package com.example.ashlynncardoso.campusview;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;

public class MainActivity extends AppCompatActivity {

    private Button getLocation;
    private Button getPlace;
    int PLACE_PICKER_REQUEST=1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        getLocation = (Button) findViewById(R.id.loc_button);
//        getLocation.setOnClickListener(new View.OnClickListener(){
//            @Override
//            public void onClick(View v){
//                nextButton();
//            }
//        });

        getPlace = (Button) findViewById(R.id.getPlace);
        getPlace.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
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
        });
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if(requestCode==PLACE_PICKER_REQUEST){
            if(resultCode==RESULT_OK){
                Place place = PlacePicker.getPlace(this,data);
                String address= String.format("Place %s", place.getName());
                getPlace.setText(address);
            }
        }
    }

    private void nextButton(){
        Intent intent = new Intent(this, MapsActivity.class);
        startActivity(intent);
    }


}
