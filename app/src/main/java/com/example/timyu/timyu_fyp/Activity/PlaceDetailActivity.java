package com.example.timyu.timyu_fyp.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.example.timyu.timyu_fyp.R;
import com.google.android.gms.vision.text.Text;

public class PlaceDetailActivity extends AppCompatActivity {
    String planname, planaddress;
    Double planlat, planlng;

    private TextView placeName, placeAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_detail);

        planname = getIntent().getExtras().getString("Planname");
        planaddress = getIntent().getExtras().getString("Planaddress");
        planlat = getIntent().getExtras().getDouble("Planlat");
        planlng = getIntent().getExtras().getDouble("Planlng");

        placeName = (TextView)findViewById(R.id.txtName);
        placeAddress = (TextView)findViewById(R.id.txtAddress);

        placeName.setText(planname);
        placeAddress.setText(planaddress);


    }
}
