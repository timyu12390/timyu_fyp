package com.example.timyu.timyu_fyp.Activity;

import com.example.timyu.timyu_fyp.Class.JSONParser;
import com.example.timyu.timyu_fyp.Class.PlaceItem;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.identity.intents.Address;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;

//import com.example.android.common.activities.SampleActivityBase;
//import com.example.android.common.logger.Log;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.res.Resources;

import android.location.Geocoder;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import com.example.timyu.timyu_fyp.R;
import com.google.android.gms.plus.model.people.Person;
import com.google.gson.Gson;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;


public class PlanDetailActivity extends AppCompatActivity implements PlaceSelectionListener, View.OnClickListener {


    String TRAVELSPOT = "http://timyu123.com/include/travelSpot.php";
    JSONParser jsonParser = new JSONParser();

    public static final String TAG = "SampleActivityBase";
    private TextView txtStartTime, txtEndTime, txtTime, txtType;
    private EditText txtPlaceName;
    private Button btnStartTime, btnEndTime, btnSubmit;
    private Spinner menuType, menuTime;
    // data from user
    private int mHour, mMinute;
    private String placeType = "購物";
    private int placeTime = 2;
    private PlaceItem placeItem = new PlaceItem();

    private String placeId, placeName, placeInfo, placeCountry, placeCity, placeAddress;
    private Double placeLat, placeLng;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plan_detail);

        // Retrieve the PlaceAutocompleteFragment.
        PlaceAutocompleteFragment autocompleteFragment = (PlaceAutocompleteFragment)
                getFragmentManager().findFragmentById(R.id.autocomplete_fragment);

        // Register a listener to receive callbacks when a place has been selected or an error has
        // occurred.
        autocompleteFragment.setOnPlaceSelectedListener(this);
        int num = getIntent().getExtras().getInt("placeItem");
        placeItem.setPlaceNum(num);

        //button
        btnSubmit = (Button)findViewById(R.id.btnSubmit);
        btnStartTime=(Button)findViewById(R.id.btnStartTime);
        btnEndTime=(Button)findViewById(R.id.btnEndTime);
        //txt
        txtStartTime=(TextView) findViewById(R.id.txtstartTime);
        txtEndTime=(TextView) findViewById(R.id.txtEndTime);
        txtTime = (TextView)findViewById(R.id.txtTime);
        txtType = (TextView)findViewById(R.id.txtType);
        //on click
        btnStartTime.setOnClickListener(this);
        btnEndTime.setOnClickListener(this);
        btnSubmit.setOnClickListener(this);
        //edit Txt
        txtPlaceName = (EditText)findViewById(R.id.txtPlaceName);
        //menu
        menuTime = (Spinner)findViewById(R.id.menuTime);
        menuType = (Spinner)findViewById(R.id.menuType);

        txtTime.setText(placeTime+"");

        //menu Array
        final String[] Type = {"購物", "休閒", "自然景觀"};
        final String[] Time = {"1", "2", "3", "4", "5", "6", "7"};

        //Time
        ArrayAdapter<String> suggestTime = new ArrayAdapter<>(PlanDetailActivity.this,
                android.R.layout.simple_spinner_dropdown_item,
                Time);
        //Type
        ArrayAdapter<String> suggestType = new ArrayAdapter<>(PlanDetailActivity.this,
                android.R.layout.simple_spinner_dropdown_item,
                Type);

        menuTime.setAdapter(suggestTime);
        menuType.setAdapter(suggestType);

        menuTime.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                placeTime = Integer.parseInt(Time[i]);
                txtTime.setText(placeTime+"");
                placeItem.setSuggestTime(placeTime);
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                //time = 2;
                //txtTime.setText(time+"");
            }
        });
        menuType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                placeType = Type[i];
                txtType.setText(placeType+"");
                placeItem.setPlaceType(placeType);
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
    }
    @Override
    public void onClick(View v) {
        if (v == btnStartTime) {
            // Get Current Time
            final Calendar c = Calendar.getInstance();
            mHour = c.get(Calendar.HOUR_OF_DAY);
            mMinute = c.get(Calendar.MINUTE);

            // Launch Time Picker Dialog
            TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                    new TimePickerDialog.OnTimeSetListener() {
                        @Override
                        public void onTimeSet(TimePicker view, int hourOfDay,
                                              int minute) {
                            txtStartTime.setText(hourOfDay + ":" + minute);
                            c.set(Calendar.HOUR_OF_DAY,hourOfDay);
                            c.set(Calendar.MINUTE,minute);
                            placeItem.setStartTime(c.getTime());
                        }
                    }, mHour, mMinute, false);
            timePickerDialog.show();
        }else if(v == btnEndTime){
            // Get Current Time
            final Calendar c = Calendar.getInstance();
            mHour = c.get(Calendar.HOUR_OF_DAY);
            mMinute = c.get(Calendar.MINUTE);

            // Launch Time Picker Dialog
            TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                    new TimePickerDialog.OnTimeSetListener() {

                        @Override
                        public void onTimeSet(TimePicker view, int hourOfDay,
                                              int minute) {
                            txtEndTime.setText(hourOfDay + ":" + minute);
                            c.set(Calendar.HOUR_OF_DAY,hourOfDay);
                            c.set(Calendar.MINUTE,minute);
                            placeItem.setEndTime(c.getTime());
                        }
                    }, mHour, mMinute, false);
            timePickerDialog.show();
        }else if(v == btnSubmit){
            String placeGet;
            placeGet = String.valueOf(txtPlaceName.getText());
            if(placeGet != null){
                Gson gson = new Gson();
                Intent i = new Intent();
                i.putExtra("placeData",gson.toJson(placeItem));
                /*
                i.putExtra("placeItem",placeItem.getPlaceNum());
                i.putExtra("placeSTime",placeItem.getStartTime());
                i.putExtra("placeETime",placeItem.getEndTime());
                i.putExtra("placeType",placeItem.getPlaceType());
                i.putExtra("placeSuggestTime",placeItem.getSuggestTime());
                i.putExtra("placeId",placeItem.getPlaceId());
                i.putExtra("placeName",placeItem.getPlaceName());
                i.putExtra("placeInfo",placeItem.getPlaceInfo());
                i.putExtra("placeAddress",placeItem.getPlaceAddress());
                i.putExtra("placeLat",placeItem.getPlaceLat());
                i.putExtra("placeLng",placeItem.getPlaceLng());
                i.putExtra("placeCountry",placeItem.getPlaceCountry());
                */
                PlanDetailActivity.AttemptSetSpot attemptSetSpot= new AttemptSetSpot();
                attemptSetSpot.execute(placeId, placeName, placeInfo, placeAddress, placeLat.toString(), placeLng.toString(), placeCountry, placeType, String.valueOf(placeTime));
                setResult(RESULT_OK,i);
                finish();
            }else {
                Toast.makeText(PlanDetailActivity.this, "Please input the place", Toast.LENGTH_LONG).show();
            }
        }
    }

    /**
     * Callback invoked when a place has been selected from the PlaceAutocompleteFragment.
     */
    @Override
    public void onPlaceSelected(Place place) {
        //placeItem.setPlaceName(place);

        Log.i(TAG, "Place Selected: " + place.getName());

        // Format the returned place's details and display them in the TextView.
        txtPlaceName.setText(place.getName());

        placeId = place.getId();
        placeName = (String) place.getName();
        placeInfo = String.valueOf(place.getWebsiteUri());
        placeAddress = (String) place.getAddress();
        placeLat = place.getLatLng().latitude;
        placeLng = place.getLatLng().longitude;

        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        List<android.location.Address> addresses = null;
        try {
            addresses = geocoder.getFromLocation(placeLat, placeLng, 1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        placeCountry = addresses.get(0).getCountryName();

        placeItem.setPlaceId(placeId);
        placeItem.setPlaceName(placeName);
        placeItem.setPlaceInfo(placeInfo);
        placeItem.setPlaceAddress(placeAddress);
        placeItem.setPlaceLat(placeLat);
        placeItem.setPlaceLng(placeLng);
        placeItem.setPlaceCountry(placeCountry);

    }

    /**
     * Callback invoked when PlaceAutocompleteFragment encounters an error.
     */
    @Override
    public void onError(Status status) {
        Log.e(TAG, "onError: Status = " + status.toString());

        Toast.makeText(this, "Place selection failed: " + status.getStatusMessage(),
                Toast.LENGTH_SHORT).show();
    }

    private class AttemptSetSpot extends AsyncTask<String,Void,JSONObject> {

        @Override

        protected JSONObject doInBackground(String... args) {
            String placeid = args[0];
            String placename= args[1];
            String placeinfo = args[2];
            String placeaddress = args[3];
            String placelat= args[4];
            String placelng = args[5];
            String placecountry= args[6];
            String placetype = args[7];
            String placesuggesttime= args[8];


            ArrayList params = new ArrayList();
            params.add(new BasicNameValuePair("PlaceId", placeid));
            params.add(new BasicNameValuePair("Tname", placename));
            params.add(new BasicNameValuePair("Tinfo", placeinfo));
            params.add(new BasicNameValuePair("TAddress", placeaddress));
            params.add(new BasicNameValuePair("Tlat", placelat));
            params.add(new BasicNameValuePair("Tlng", placelng));
            params.add(new BasicNameValuePair("TCountry", placecountry));
            params.add(new BasicNameValuePair("TType", placetype));
            params.add(new BasicNameValuePair("TSuggestTime", placesuggesttime));


            JSONObject json = jsonParser.makeHttpRequest(TRAVELSPOT, "POST", params);


            return json;

        }

        protected void onPostExecute(JSONObject result) {

            // dismiss the dialog once product deleted
            //Toast.makeText(getApplicationContext(),result,Toast.LENGTH_LONG).show();

            try {
                if (result.getInt("place") == 30) {
                    Log.e(TAG, "Insert success" );

                }else{
                    Log.e(TAG, "Insert not success" );
                }
            }catch (Exception e){

            }
        }
    }
}
