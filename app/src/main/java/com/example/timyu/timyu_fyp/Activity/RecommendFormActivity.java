package com.example.timyu.timyu_fyp.Activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
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

import com.example.timyu.timyu_fyp.Class.JSONParser;
import com.example.timyu.timyu_fyp.Class.RecommendationAdapter;
import com.example.timyu.timyu_fyp.Class.SelectedSpot;
import com.example.timyu.timyu_fyp.Class.SuggestQuestion;
import com.example.timyu.timyu_fyp.Class.UserManager;
import com.example.timyu.timyu_fyp.R;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import ernestoyaquello.com.verticalstepperform.VerticalStepperFormLayout;
import ernestoyaquello.com.verticalstepperform.fragments.BackConfirmationFragment;
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
    String TAG = "Log Test";

    //Get data from form
    Double pointLat, pointLng;
    int dayOfTravel, startTime, endTime;
    String searchedCountry, searchedStartPoint, travelTitle, travelType;

    //Get data from db
    String RECOMMEND = "http://timyu123.com/include/recommend.php";
    String USERPLAN = "http://timyu123.com/include/userPlan.php";
    JSONParser jsonParser = new JSONParser();

    //class


    //List
    ArrayList<SuggestQuestion> waitToSelect = new ArrayList<>();
    ArrayList<SuggestQuestion> listToSelect = new ArrayList<>();
    ArrayList <SelectedSpot>selectedPlace = new ArrayList<>();

    //filtering var
    int firstFilter = 150000;
    int secondFilter = 5000;
    double compareLat, compareLng;
    int day = 1;
    int oneDayTime;
    int leftTime;
    int selectedIndex;
    String planId;





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
            case 6:
                //view = createConfirmStep();
                //break;
        }
        return view;
    }

    private View createNameStep() {
        LayoutInflater inflater = LayoutInflater.from(getBaseContext());
        LinearLayout titleLayoutContent = (LinearLayout) inflater.inflate(R.layout.title_step_layout, null, false);
        title = (EditText) titleLayoutContent.findViewById(R.id.txtTitle) ;

        //travelTitle = title.getText().toString();
        //Log.w(TAG,travelTitle);
        return titleLayoutContent;
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
            case 6:
                Log.d(TAG,"in the confirm data");
        }
    }

    private void checkName() {
        title.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {}

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                if(title.getText().toString().length() >= 1 && title.getText().toString().length() <= 40) {
                    travelTitle = title.getText().toString();
                    //Continue button
                    verticalStepperForm.setActiveStepAsCompleted();
                } else {
                    // This error message is optional (use null if you don't want to display an error message)
                    String errorMessage = "The name must have between 1 and 40 characters";
                    Log.d(TAG,errorMessage);
                    verticalStepperForm.setActiveStepAsUncompleted(errorMessage);
                }
            }
        });

    }

    private void checkStartPoint() {
        searchedPlace.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {}

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
                String errorMessage = "Please search the start point of your travel";
                Log.d(TAG,errorMessage);
                verticalStepperForm.setActiveStepAsUncompleted(errorMessage);
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                if(searchedPlace.getText().toString().length() >= 1 && searchedPlace.getText().toString().length() <= 70) {
                    //travelTitle = title.getText().toString();
                    //Continue button
                    verticalStepperForm.setActiveStepAsCompleted();
                } else {
                    // This error message is optional (use null if you don't want to display an error message)
                    String errorMessage = "The name must have between 1 and 40 characters";
                    Log.d(TAG,errorMessage);
                    verticalStepperForm.setActiveStepAsUncompleted(errorMessage);
                }
            }
        });
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
        Log.e(TAG,"confirmData");

        //init data
        leftTime = endTime - startTime -4;
        oneDayTime = endTime - startTime -4;
        compareLat = pointLat;
        compareLng = pointLng;
        selectedPlace.clear();
        day = 1;
        planId =  ( UserManager.getInstance().getUser().getId() + ""+ (Math.random()* 999999999)  +"" + UserManager.getInstance().getUser().getId()) ;
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Confirm Data");
            builder.setMessage("Place Title: " + travelTitle +
                    "\nPlace Name: " + searchedStartPoint +
                    "\nPlace Country: " + searchedCountry +
                    "\nTravel Days: " + dayOfTravel +
                    "\nStart Time: " + startTime + " AM" +
                    "\nEnd Time: " + endTime + " PM" +
                    "\nTravel Type: " + travelType)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            //get data from db
                            RecommendFormActivity.AttemptRecommend attemptRecommend = new RecommendFormActivity.AttemptRecommend();
                            attemptRecommend.execute(searchedCountry);

                        }
                    })
                    .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            BackConfirmationFragment backConfirmation = new BackConfirmationFragment();
                            backConfirmation.show(getSupportFragmentManager(), null);
                        }
                    });
            // Create the AlertDialog object and return it
            builder.create().show();

    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        Log.d(TAG,"onSaveInstanceState");
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        Log.d(TAG,"onRestoreInstanceState");
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
                compareLat = place.getLatLng().latitude;
                compareLng = place.getLatLng().longitude;

            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(this, data);
                Log.e(TAG, "Error: Status = " + status.toString());
            } else if (resultCode == RESULT_CANCELED) {
                // Indicates that the activity closed before a selection was made. For example if
                // the user pressed the back button.
            }
        }
    }


    public float distanceBetween(double latA, double lngA, double latB, double lngB){
        Location locationA = new Location("point A");

        locationA.setLatitude(latA);
        locationA.setLongitude(lngA);

        Location locationB = new Location("point B");

        locationB.setLatitude(latB);
        locationB.setLongitude(lngB);

        float distance = locationA.distanceTo(locationB);
        return distance;
    }

    private class AttemptRecommend extends AsyncTask<String,Void,JSONObject> {

        @Override

        protected JSONObject doInBackground(String... args) {

            String country= args[0];

            ArrayList params = new ArrayList();
            params.add(new BasicNameValuePair("TCountry", country));

            JSONObject json = jsonParser.makeHttpRequest(RECOMMEND, "POST", params);

            return json;

        }

        protected void onPostExecute(JSONObject result) {

            try {
                if(result.optInt("success",10) == 30) {
                    android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(RecommendFormActivity.this);
                    builder.setMessage("Internet Error")
                            .setCancelable(false)
                            .setPositiveButton("OK", null);
                    android.app.AlertDialog alert = builder.create();
                    alert.show();
                }else {
                    Log.d(TAG,"Got some data from server");
                    listToSelect.clear();
                    waitToSelect.clear();
                    for(int i = 0; i<result.getJSONArray("result").length();i++){
                        float a = distanceBetween(compareLat,compareLng, (result.getJSONArray("result").getJSONObject(i).optDouble("Tlat",0)), result.getJSONArray("result").getJSONObject(i).optDouble("Tlng",0));

                        if(a > 0 && a < secondFilter){
                            SuggestQuestion suggestQuestion = new SuggestQuestion();
                            suggestQuestion.setPlaceName(result.getJSONArray("result").getJSONObject(i).optString("Tname","0"));
                            suggestQuestion.setPlaceAddress(result.getJSONArray("result").getJSONObject(i).optString("TAddress","0"));
                            suggestQuestion.setPlaceCountry(result.getJSONArray("result").getJSONObject(i).optString("TCountry","0"));
                            suggestQuestion.setPlaceId(result.getJSONArray("result").getJSONObject(i).optString("PlaceId","0"));
                            suggestQuestion.setPlaceLat(result.getJSONArray("result").getJSONObject(i).optDouble("Tlat",0));
                            suggestQuestion.setPlaceLng(result.getJSONArray("result").getJSONObject(i).optDouble("Tlng",0));
                            suggestQuestion.setPlaceTime(result.getJSONArray("result").getJSONObject(i).optInt("TSuggestTime",0));
                            suggestQuestion.setPlaceType(result.getJSONArray("result").getJSONObject(i).optString("TType","0"));
                            suggestQuestion.setStatus(false);
                            listToSelect.add(suggestQuestion);
                        }
                        if(a > 0 && a < firstFilter){
                            SuggestQuestion suggestQuestion1 = new SuggestQuestion();
                            suggestQuestion1.setPlaceName(result.getJSONArray("result").getJSONObject(i).optString("Tname","0"));
                            suggestQuestion1.setPlaceAddress(result.getJSONArray("result").getJSONObject(i).optString("TAddress","0"));
                            suggestQuestion1.setPlaceCountry(result.getJSONArray("result").getJSONObject(i).optString("TCountry","0"));
                            suggestQuestion1.setPlaceId(result.getJSONArray("result").getJSONObject(i).optString("PlaceId","0"));
                            suggestQuestion1.setPlaceLat(result.getJSONArray("result").getJSONObject(i).optDouble("Tlat",0));
                            suggestQuestion1.setPlaceLng(result.getJSONArray("result").getJSONObject(i).optDouble("Tlng",0));
                            suggestQuestion1.setPlaceTime(result.getJSONArray("result").getJSONObject(i).optInt("TSuggestTime",0));
                            suggestQuestion1.setPlaceType(result.getJSONArray("result").getJSONObject(i).optString("TType","0"));
                            suggestQuestion1.setStatus(false);
                            waitToSelect.add(suggestQuestion1);
                        }

                    }
                    Log.e("   list to select", String.valueOf(listToSelect.size()));
                    Log.e("   wait to select", String.valueOf(waitToSelect.size()));

                    //show the list in the log
                    for(int i=0;i<listToSelect.size();i++){
                        Log.d("Name",listToSelect.get(i).getPlaceName());
                    }
                    //alert list box
                    AlertDialog.Builder builder = new AlertDialog.Builder(RecommendFormActivity.this);
                    builder.setTitle("DAY : "+day);
                    LayoutInflater inflater = RecommendFormActivity.this.getLayoutInflater();
                    View view = inflater.inflate(R.layout.layout_recommendationview, null);

                    //The list
                    final RecyclerView list = (RecyclerView) view.findViewById(R.id.list);
                    final RecommendationAdapter recommendationAdapter = new RecommendationAdapter(listToSelect);
                    recommendationAdapter.setOnItemClickListener(new RecommendationAdapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(View view, int position) {
                            for(SuggestQuestion suggestQuestion : listToSelect){
                                suggestQuestion.setStatus(false);
                            }
                            listToSelect.get(position).setStatus(true);
                            selectedIndex = position;
                            recommendationAdapter.notifyDataSetChanged();

                        }
                    });
                    list.setAdapter(recommendationAdapter);
                    list.setLayoutManager(new LinearLayoutManager(RecommendFormActivity.this));

                    builder.setView(view);

                    //selected action
                    if(leftTime >= 4 && day <= dayOfTravel) {
                        builder.setPositiveButton("Next Spot", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                leftTime = leftTime - listToSelect.get(selectedIndex).getPlaceTime();
                                compareLng = listToSelect.get(selectedIndex).getPlaceLng();
                                compareLat = listToSelect.get(selectedIndex).getPlaceLat();
                                //(String planid, String plantitle,int planday, int plantime, int planstart, int planend, double planlat, double planlng, String planaddress, String planname, String plancountry, long userid)

                                selectedPlace.add(
                                        selectedSpot(planId, travelTitle, day, listToSelect.get(selectedIndex).getPlaceTime(),
                                        startTime, endTime, listToSelect.get(selectedIndex).getPlaceLat(),
                                        listToSelect.get(selectedIndex).getPlaceLng(),
                                        listToSelect.get(selectedIndex).getPlaceAddress(),
                                        listToSelect.get(selectedIndex).getPlaceName(),
                                        listToSelect.get(selectedIndex).getPlaceCountry(),
                                                (int) UserManager.getInstance().getUser().getId())
                                );

                                RecommendFormActivity.AttemptRecommend attemptRecommend = new RecommendFormActivity.AttemptRecommend();
                                attemptRecommend.execute(searchedCountry);

                            }
                        });
                        builder.show();
                    }else if(leftTime < 4 && day < dayOfTravel){
                        builder.setPositiveButton("Next Day", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                day += day;
                                leftTime = oneDayTime;
                                compareLng = pointLng;
                                compareLat = pointLat;
                                selectedPlace.add(
                                        selectedSpot(planId, travelTitle, day, listToSelect.get(selectedIndex).getPlaceTime(),
                                                startTime, endTime, listToSelect.get(selectedIndex).getPlaceLat(),
                                                listToSelect.get(selectedIndex).getPlaceLng(),
                                                listToSelect.get(selectedIndex).getPlaceAddress(),
                                                listToSelect.get(selectedIndex).getPlaceName(),
                                                listToSelect.get(selectedIndex).getPlaceCountry(),
                                                (int) UserManager.getInstance().getUser().getId())
                                );

                                RecommendFormActivity.AttemptRecommend attemptRecommend = new RecommendFormActivity.AttemptRecommend();
                                attemptRecommend.execute(searchedCountry);
                            }
                        });
                        builder.show();
                    }else if(leftTime < 4 && day == dayOfTravel){
                        builder.setPositiveButton("Finish", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                selectedPlace.add(
                                        selectedSpot(planId, travelTitle, day, listToSelect.get(selectedIndex).getPlaceTime(),
                                                startTime, endTime, listToSelect.get(selectedIndex).getPlaceLat(),
                                                listToSelect.get(selectedIndex).getPlaceLng(),
                                                listToSelect.get(selectedIndex).getPlaceAddress(),
                                                listToSelect.get(selectedIndex).getPlaceName(),
                                                listToSelect.get(selectedIndex).getPlaceCountry(),
                                                (int) UserManager.getInstance().getUser().getId())
                                        ////
                                );
                                Log.e("Total size", selectedPlace.size()+"");

                                for(int i = 0; i<selectedPlace.size();i++){
                                    RecommendFormActivity.AttemptAddPlan attemptAddPlan = new AttemptAddPlan();

                                    attemptAddPlan.execute(selectedPlace.get(i).getPlanId(),
                                            selectedPlace.get(i).getPlanTitle(),
                                            String.valueOf(selectedPlace.get(i).getPlanDay()),
                                            String.valueOf(selectedPlace.get(i).getPlanTime()),
                                            String.valueOf(selectedPlace.get(i).getPlanStart()),
                                            String.valueOf(selectedPlace.get(i).getPlanEnd()),
                                            String.valueOf(selectedPlace.get(i).getPlanLat()),
                                            String.valueOf(selectedPlace.get(i).getPlanLng()),
                                            selectedPlace.get(i).getPlanName(),
                                            selectedPlace.get(i).getPlanAddress(),
                                            selectedPlace.get(i).getPlanCountry(),
                                            String.valueOf(selectedPlace.get(i).getUserId()));
                                }
                                finish();

                            }
                        });
                        builder.show();

                    }
                }
            }catch (Exception e){
                Log.d(TAG, "onPostExecute: "+e);
            }

        }

    }

    public SelectedSpot selectedSpot(String planid, String plantitle, int planday, int plantime, int planstart, int planend, double planlat, double planlng, String planaddress, String planname, String plancountry, int userid){
        SelectedSpot selectedSpot = new SelectedSpot();
        selectedSpot.setPlanId(planid);
        selectedSpot.setPlanTitle(plantitle);
        selectedSpot.setPlanDay(planday);
        selectedSpot.setPlanTime(plantime);
        selectedSpot.setPlanStart(planstart);
        selectedSpot.setPlanEnd(planend);
        selectedSpot.setPlanLat(planlat);
        selectedSpot.setPlanLng(planlng);
        selectedSpot.setPlanAddress(planaddress);
        selectedSpot.setPlanName(planname);
        selectedSpot.setPlanCountry(plancountry);
        selectedSpot.setUserId(userid);
        return  selectedSpot;
    }

    private class AttemptAddPlan extends AsyncTask<String,Void,JSONObject> {

        @Override
        protected JSONObject doInBackground(String... args) {
            String planid = args[0];
            String plantitle= args[1];
            String planday = args[2];
            String plantime = args[3];
            String planstart= args[4];
            String planend = args[5];
            String planlat= args[6];
            String planlng = args[7];
            String planaddress= args[8];
            String planname= args[9];
            String plancountry= args[10];
            String userid= args[11];

            ArrayList params = new ArrayList();
            params.add(new BasicNameValuePair("PlanId", planid));
            params.add(new BasicNameValuePair("Plantitle", plantitle));
            params.add(new BasicNameValuePair("Planday", planday));
            params.add(new BasicNameValuePair("Plantime", plantime));
            params.add(new BasicNameValuePair("Planstarttime", planstart));
            params.add(new BasicNameValuePair("Planendtime", planend));
            params.add(new BasicNameValuePair("Planlat", planlat));
            params.add(new BasicNameValuePair("Planlng", planlng));
            params.add(new BasicNameValuePair("Planaddress", planaddress));
            params.add(new BasicNameValuePair("Planname", planname));
            params.add(new BasicNameValuePair("Plancountry", plancountry));
            params.add(new BasicNameValuePair("userId", userid));

            Log.e("Data","id:"+planid +"\ntitle: " +plantitle +"\nday: " +planday +"\ntime: " +plantime +"\nstart: " +planstart +"\nend: " +planend +"\nlat: " +planlat +"\nlng: " +planlng +"\naddress: " +planaddress +"\nname: " +planname +"\ncity: " +plancountry +"\nuid: " +userid);
            Log.e("Show Params:",params+"");

            JSONObject json = jsonParser.makeHttpRequest(USERPLAN, "POST", params);

            return json;
        }

        protected void onPostExecute(JSONObject result) {

            // dismiss the dialog once product deleted
            //Toast.makeText(getApplicationContext(),result,Toast.LENGTH_LONG).show();

            try {
                if (result.getInt("success") == 40) {
                    Log.e(TAG, "Insert success" );
                }else{
                    Log.e(TAG, "Insert not success" );
                }
            }catch (Exception e){

            }
        }
    }

}
