package com.example.ashlynncardoso.campusview;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private Button getLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getLocation = (Button) findViewById(R.id.loc_button);
        getLocation.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                nextButton();
            }
        });
    }

    private void nextButton(){
        Intent intent = new Intent(this, MapsActivity.class);
        startActivity(intent);
    }
}
