package com.example.timyu.timyu_fyp.Activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.example.timyu.timyu_fyp.Class.JSONParser;
import com.example.timyu.timyu_fyp.Class.PlanAdapter;
import com.example.timyu.timyu_fyp.Class.PlanDetailAdapter;
import com.example.timyu.timyu_fyp.Class.User;
import com.example.timyu.timyu_fyp.Class.UserManager;
import com.example.timyu.timyu_fyp.Class.UserPlan;
import com.example.timyu.timyu_fyp.Class.UserPlanDetail;
import com.example.timyu.timyu_fyp.R;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;

public class UserPlanDetailActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private PlanDetailAdapter planDetailAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ArrayList<UserPlanDetail> dataList = new ArrayList<>();
    private String placeId;
    String USERPLANLIST = "http://timyu123.com/include/userPlan.php";

    JSONParser jsonParser = new JSONParser();

    String TAG = "test";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_plan_datail);
        placeId = getIntent().getExtras().getString("PlanId");

        mRecyclerView = (RecyclerView) findViewById(R.id.PlanDetail);

        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        AttemptPlanDetail attemptPlanDetail = new AttemptPlanDetail();
        attemptPlanDetail.execute(String.valueOf(UserManager.getInstance().getUser().getId()), placeId);
    }

    private class AttemptPlanDetail extends AsyncTask<String,Void,JSONObject> {

        @Override
        protected JSONObject doInBackground(String... args) {
            String userId = args[0];
            String planId = args[1];

            ArrayList params = new ArrayList();
            params.add(new BasicNameValuePair("userId", userId));
            params.add(new BasicNameValuePair("PlanId", planId));

            JSONObject json = jsonParser.makeHttpRequest(USERPLANLIST, "POST", params);

            return json;
        }

        protected void onPostExecute(JSONObject result) {

            // dismiss the dialog once product deleted
            //Toast.makeText(getApplicationContext(),result,Toast.LENGTH_LONG).show();
            try {
                if(result.optInt("success",10) == 30) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(UserPlanDetailActivity.this);
                    builder.setMessage("No existing travel plan")
                            .setCancelable(false)
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    //do things
                                }
                            });
                    AlertDialog alert = builder.create();
                    alert.show();
                }else if(result!= null){
                //(String planId, String planTitle, String planAddress, String planName, String planCountry, int planDay, int planTime, int planStart, int planEnd, int userId, double planLat, double planLng)
                    for(int i = 0; i<result.getJSONArray("result").length();i++){
                        UserPlanDetail userPlanDetail = new UserPlanDetail(
                                result.getJSONArray("result").getJSONObject(i).optString("PlanId","0"),
                                result.getJSONArray("result").getJSONObject(i).optString("Plantitle","0"),
                                result.getJSONArray("result").getJSONObject(i).optString("Planname","0"),
                                result.getJSONArray("result").getJSONObject(i).optString("Planaddress","0"),
                                result.getJSONArray("result").getJSONObject(i).optString("Plancountry","0"),
                                result.getJSONArray("result").getJSONObject(i).optInt("Planday",0),
                                result.getJSONArray("result").getJSONObject(i).optInt("Plantime",0),
                                result.getJSONArray("result").getJSONObject(i).optInt("Planstarttime",0),
                                result.getJSONArray("result").getJSONObject(i).optInt("Planendtime",0),
                                result.getJSONArray("result").getJSONObject(i).optInt("userId",0),
                                result.getJSONArray("result").getJSONObject(i).optDouble("Planlat",0),
                                result.getJSONArray("result").getJSONObject(i).optDouble("Planlng",0)
                        );
                        dataList.add(userPlanDetail);


                        planDetailAdapter = new PlanDetailAdapter(dataList);
                        mRecyclerView.setAdapter(planDetailAdapter);
                    }

                    planDetailAdapter.setListener(new PlanDetailAdapter.PlanDetailAdapterListener() {
                        @Override
                        public void onItemClicked(View view, UserPlanDetail userPlanDetail) {
                            Log.e("","clicked");
                            Intent detail = new Intent(UserPlanDetailActivity.this, PlaceDetailActivity.class);
                            detail.putExtra("Planname",userPlanDetail.getPlanName());
                            detail.putExtra("Planaddress",userPlanDetail.getPlanAddress());
                            detail.putExtra("Planlat",userPlanDetail.getPlanLat());
                            detail.putExtra("Planlng",userPlanDetail.getPlanLng());
                            startActivity(detail);
                        }
                    });


                }
            }catch (Exception e){
                Log.d(TAG, "onPostExecute: "+e);
            }
        }
    }

}
