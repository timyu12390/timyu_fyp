package com.example.timyu.timyu_fyp.Activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.timyu.timyu_fyp.R;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.vision.text.Text;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import ernestoyaquello.com.verticalstepperform.VerticalStepperFormLayout;
import ernestoyaquello.com.verticalstepperform.*;
import ernestoyaquello.com.verticalstepperform.interfaces.VerticalStepperForm;

public class RecommendFormActivity extends AppCompatActivity implements VerticalStepperForm {
    private VerticalStepperFormLayout verticalStepperForm;
    private static final int REQUEST_CODE_AUTOCOMPLETE = 1;
    //On form view
    EditText title;
    Button searchPlace;
    TextView searchedPlace;
    Spinner daySpinner, startSpinner, endSpinner, typeSpinner;
    final String[] dayArray = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10"};
    //Log message
    String TAG = "";

    //Get data from form
    Double pointLat, pointLng;
    int dayOfTravel, startTime, endTime;
    String searchedCountry, searchedStartPoint, travelTitle, travelType;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recommend_form);

        String[] mySteps = {"Title", "Start Point", "Day", "Start Time", "End Time", "Travel Type"};
        String[] subTitle = {"",
                "The start point of the travel" ,
                "The total days of this travel" ,
                "The start time of each day ",
                "The end time of each day (Option)",
                "The type of this travel"};
        int colorPrimary = ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary);
        int colorPrimaryDark = ContextCompat.getColor(getApplicationContext(), R.color.colorPrimaryDark);
        // Finding the view
        verticalStepperForm = (VerticalStepperFormLayout) findViewById(R.id.vertical_stepper_form);
        // Setting up and initializing the form
        VerticalStepperFormLayout.Builder.newInstance(verticalStepperForm, mySteps, this, this)
                .primaryColor(colorPrimary)
                .primaryDarkColor(colorPrimaryDark)
                .displayBottomNavigation(true)
                .stepsSubtitles(subTitle)
                .init();
    }

    @Override
    public View createStepContentView(int stepNumber) {
        View view = null;
        switch (stepNumber) {
            case 0:
                view = createNameStep();
                break;
            case 1:
                view = createStartStep();
                break;
            case 2:
                view = createDayStep();
                break;
            case 3:
                view = createStartTimeStep();
                break;
            case 4:
                view = createEndTimeStep();
                break;
            case 5:
                view = createTypeStep();
                break;
        }
        return view;
    }

    private View createNameStep() {
        // Here we generate programmatically the view that will be added by the system to the step content layout
        title = new EditText(this);
        title.setSingleLine(true);
        title.setHint("Travel title");

        return title;
    }

    private View createStartStep() {
        // In this case we generate the view by inflating a XML file
        LayoutInflater inflater = LayoutInflater.from(getBaseContext());
        LinearLayout searchLayoutContent = (LinearLayout) inflater.inflate(R.layout.email_step_layout, null, false);

        searchPlace = (Button)searchLayoutContent.findViewById(R.id.btnSearchPlace);
        searchedPlace = (TextView)searchLayoutContent.findViewById(R.id.txtSearchedPlace);

        searchPlace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openAutocompleteActivity();
            }
        });

        return searchLayoutContent;
    }

    private View createDayStep() {
        LayoutInflater inflater = LayoutInflater.from(getBaseContext());
        LinearLayout daysLayoutContent = (LinearLayout) inflater.inflate(R.layout.day_step_layout, null, false);

        //daysSelected = (TextView) daysLayoutContent.findViewById(R.id.txtDaysSelected);
        daySpinner = (Spinner) daysLayoutContent.findViewById(R.id.daySpinner);
        final ArrayAdapter<String> daysList = new ArrayAdapter<>(RecommendFormActivity.this,
                android.R.layout.simple_spinner_dropdown_item,
                dayArray);
        daySpinner.setAdapter(daysList);

        daySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                dayOfTravel = Integer.parseInt(dayArray[i]);
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
        return daysLayoutContent;
    }

    private View createStartTimeStep() {
        LayoutInflater inflater = LayoutInflater.from(getBaseContext());
        LinearLayout startLayoutContent = (LinearLayout) inflater.inflate(R.layout.time_step_layout, null, false);

        final String[] start = {"06","07","08","09","10","11","12"};
        TextView am = (TextView)startLayoutContent.findViewById(R.id.txtMsgOnly2);
        am.setText("AM each day");
        startSpinner = (Spinner) startLayoutContent.findViewById(R.id.timeSpinner);
        final ArrayAdapter<String> timeList = new ArrayAdapter<>(RecommendFormActivity.this,
                android.R.layout.simple_spinner_dropdown_item,
                start);
        startSpinner.setAdapter(timeList);

        startSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                startTime = Integer.parseInt(start[i]);
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        return startLayoutContent;
    }
    private View createEndTimeStep() {
        LayoutInflater inflater = LayoutInflater.from(getBaseContext());
        LinearLayout endLayoutContent = (LinearLayout) inflater.inflate(R.layout.time_step_layout, null, false);

        final String[] end = {"17","18","19","20","21","22","23"};
        TextView am = (TextView)endLayoutContent.findViewById(R.id.txtMsgOnly2);
        am.setText("PM each day");
        endSpinner = (Spinner) endLayoutContent.findViewById(R.id.timeSpinner);
        final ArrayAdapter<String> timeList = new ArrayAdapter<>(RecommendFormActivity.this,
                android.R.layout.simple_spinner_dropdown_item,
                end);
        endSpinner.setAdapter(timeList);

        endSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                endTime = Integer.parseInt(end[i]);
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        return endLayoutContent;
    }
    private View createTypeStep() {
        LayoutInflater inflater = LayoutInflater.from(getBaseContext());
        LinearLayout typeLayoutContent = (LinearLayout) inflater.inflate(R.layout.type_step_layout, null, false);

        final String[] type = {"Shopping", "Leisure", "Natural Scene"};

        typeSpinner = (Spinner) typeLayoutContent.findViewById(R.id.typeSpinner);
        final ArrayAdapter<String> timeList = new ArrayAdapter<>(RecommendFormActivity.this,
                android.R.layout.simple_spinner_dropdown_item,
                type);
        typeSpinner.setAdapter(timeList);

        typeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                travelType = type[i];
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        return typeLayoutContent;
    }


    @Override
    public void onStepOpening(int stepNumber) {
        switch (stepNumber) {
            case 0:
                checkName();
                break;
            case 1:
                checkStartPoint();
                break;
            case 2:
                checkDay();
                break;
            case 3:
                checkStart();
                break;
            case 4:
                checkEnd();
                break;
            case 5:
                checkType();
                break;
        }
    }

    private void checkName() {
        Log.d(TAG,"in log");
        if(title.length() >= 0 && title.length() <= 40) {
            travelTitle = title.getText().toString();
            Log.d(TAG,">=0");
            //Continue button
            verticalStepperForm.setActiveStepAsCompleted();
        } else {
            // This error message is optional (use null if you don't want to display an error message)
            String errorMessage = "The name must have between 1 and 40 characters";
            Log.d(TAG,errorMessage);
            verticalStepperForm.setActiveStepAsUncompleted(errorMessage);
        }
    }

    private void checkStartPoint() {
        if(searchedPlace.length() >= 0) {
            //Continue button
            verticalStepperForm.setActiveStepAsCompleted();
        } else {
            // This error message is optional (use null if you don't want to display an error message)
            String errorMessage = "Please enter the start point of this travel";
            verticalStepperForm.setActiveStepAsUncompleted(errorMessage);
        }
        //Continue button
        //verticalStepperForm.setActiveStepAsCompleted();
    }

    private void checkDay() {
        verticalStepperForm.setActiveStepAsCompleted();
    }
    private void checkStart() {
        verticalStepperForm.setActiveStepAsCompleted();
    }
    private void checkEnd() {
        verticalStepperForm.setActiveStepAsCompleted();
    }
    private void checkType() {
        verticalStepperForm.setActiveStepAsCompleted();
    }

    @Override
    public void sendData() {

    }
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {

        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
	    super.onRestoreInstanceState(savedInstanceState);
    }

    private void openAutocompleteActivity() {
        try {
            // The autocomplete activity requires Google Play Services to be available. The intent
            // builder checks this and throws an exception if it is not the case.
            Intent intent = new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN)
                    .build(this);
            startActivityForResult(intent, REQUEST_CODE_AUTOCOMPLETE);
        } catch (GooglePlayServicesRepairableException e) {
            // Indicates that Google Play Services is either not installed or not up to date. Prompt
            // the user to correct the issue.
            GoogleApiAvailability.getInstance().getErrorDialog(this, e.getConnectionStatusCode(),
                    0 /* requestCode */).show();
        } catch (GooglePlayServicesNotAvailableException e) {
            // Indicates that Google Play Services is not available and the problem is not easily
            // resolvable.
            String message = "Google Play Services is not available: " +
                    GoogleApiAvailability.getInstance().getErrorString(e.errorCode);

            Log.e(TAG, message);
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Check that the result was from the autocomplete widget.
        if (requestCode == REQUEST_CODE_AUTOCOMPLETE) {
            if (resultCode == RESULT_OK) {
                // Get the user's selected place from the Intent.
                Place place = PlaceAutocomplete.getPlace(this, data);
                Log.i(TAG, "Place Selected: " + place.getName());

                Geocoder geocoder = new Geocoder(this, Locale.getDefault());
                List<android.location.Address> addresses = null;
                try {
                    addresses = geocoder.getFromLocation(place.getLatLng().latitude, place.getLatLng().longitude, 1);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                searchedCountry = addresses.get(0).getCountryName();
                searchedStartPoint = place.getName().toString();
                // Format the place's details and display them in the TextView.
                searchedPlace.setText("Place Name : " + place.getName()  +"\nPlace Country :" + searchedCountry );

                pointLat = place.getLatLng().latitude;
                pointLng = place.getLatLng().longitude;

            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(this, data);
                Log.e(TAG, "Error: Status = " + status.toString());
            } else if (resultCode == RESULT_CANCELED) {
                // Indicates that the activity closed before a selection was made. For example if
                // the user pressed the back button.
            }
        }
    }


    public void distanceBetween(double lagA, double lngA, double latB, double lngB){

    }

}
