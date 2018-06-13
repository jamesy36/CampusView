package com.example.ashlynncardoso.campusview;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private Button getPop;
    private Button getNearby;

    public static final int MAP_REQUEST = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getPop = (Button) findViewById(R.id.pop_button);
        getPop.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                popButton();
            }
        });
        getNearby = (Button) findViewById(R.id.loc_button);
        getNearby.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                nearbyButton();
            }
        });
    }

    private void popButton(){
        Intent intent = new Intent(this, MapsActivity.class);
        startActivityForResult(intent, MAP_REQUEST);
    }

    private void nearbyButton(){
//        Intent intent = new Intent(this, MapsActivity.class);
//        startActivityForResult(intent, MAP_REQUEST);
    }
}
