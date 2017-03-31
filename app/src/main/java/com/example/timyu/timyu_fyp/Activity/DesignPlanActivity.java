package com.example.timyu.timyu_fyp.Activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.location.Geocoder;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
//import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.Spanned;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.timyu.timyu_fyp.Class.MyAdapter;
import com.example.timyu.timyu_fyp.Class.PlaceItem;
import com.example.timyu.timyu_fyp.R;
import com.github.clans.fab.FloatingActionMenu;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.identity.intents.Address;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.lang.Object;

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.google.gson.Gson;


public class DesignPlanActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private GoogleApiClient mGoogleApiClient;
    private static final int REQUEST_CODE_AUTOCOMPLETE = 1;
    private TextView mPlaceDetailsText;
    private TextView mPlaceAttribution;
    private RecyclerView mRecyclerView;
    private MyAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    List<PlaceItem> placeList = new ArrayList<>();
    int day = 1;
    int placeNum = 0;

    String log = "Test";

    FloatingActionMenu materialDesignFAM;
    com.github.clans.fab.FloatingActionButton floatingActionButton1, floatingActionButton2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_design_plan);
        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        // specify an adapter (see also next example)
        mAdapter = new MyAdapter(placeList);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setListener(new MyAdapter.MyAdapterListener() {
            @Override
            public void onItemClicked(View view, PlaceItem placeItem) {
                Intent i = new Intent(DesignPlanActivity.this, PlanDetailActivity.class);

                i.putExtra("placeItem", placeItem.getPlaceNum());
                startActivityForResult(i,999);
            }
        });

        //google place api
        mGoogleApiClient = new GoogleApiClient
                .Builder(this)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();

        materialDesignFAM = (FloatingActionMenu) findViewById(R.id.material_design_android_floating_action_menu);
        //add place
        floatingActionButton1 = (com.github.clans.fab.FloatingActionButton) findViewById(R.id.material_design_floating_action_menu_item1);
        //add day
        floatingActionButton2 = (com.github.clans.fab.FloatingActionButton) findViewById(R.id.material_design_floating_action_menu_item2);



        floatingActionButton1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //TODO something when floating action menu first item clicked

                PlaceItem placeItem = new PlaceItem();
                placeNum = placeNum+1;
                if(placeNum <= 6) {
                    Log.d(log, "add place: ");
                    placeItem.setPlaceNum(placeNum);
                    placeItem.setDay(day);
                    placeList.add(placeItem);
                    mAdapter.notifyDataSetChanged();

                }else{
                    Toast.makeText(DesignPlanActivity.this, "Maximum can add 6 travel spot in one day", Toast.LENGTH_LONG).show();
                    Log.d(log, "max ");
                }
                materialDesignFAM.close(true);
            }
        });
        floatingActionButton2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //TODO something when floating action menu second item clicked
                final PlaceItem placeItem = new PlaceItem();
                if(placeNum < 3){
                    AlertDialog.Builder builder = new AlertDialog.Builder(DesignPlanActivity.this);
                    builder.setMessage("Are you sure day " + day + "only have " + placeNum + " place?")
                            .setCancelable(false)
                            .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    day++;
                                    placeNum = 1;
                                    Log.d(log, "add day");
                                    placeItem.setPlaceNum(placeNum);
                                    placeItem.setDay(day);
                                    placeList.add(placeItem);
                                    mAdapter.notifyDataSetChanged();
                                }
                            });
                    builder.setNegativeButton("No", new DialogInterface.OnClickListener(){
                        public void onClick(DialogInterface dialog, int id) {
                            day++;
                            placeNum = 0;
                        }
                    });
                    AlertDialog alert = builder.create();
                    alert.show();
                }else {
                    day++;
                    placeNum = 1;
                    Log.d(log, "add day");
                    placeItem.setPlaceNum(placeNum);
                    placeItem.setDay(day);
                    placeList.add(placeItem);
                    mAdapter.notifyDataSetChanged();
                }
                materialDesignFAM.close(true);
            }
        });


        //mPlaceDetailsText = (TextView) findViewById(R.id.place_details);
        mPlaceAttribution = (TextView) findViewById(R.id.place_attribution);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case 999:
                if(resultCode == RESULT_OK ){
                    Gson gson = new Gson();
                    PlaceItem item = gson.fromJson(data.getStringExtra("placeData"),PlaceItem.class);
                    for(int i = 0; i<placeList.size(); i++){
                        if(placeList.get(i).getPlaceNum() == item.getPlaceNum()){
                            placeList.set(i,item);
                            mAdapter.notifyDataSetChanged();
                        }
                    }
                }
                break;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    protected void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}

