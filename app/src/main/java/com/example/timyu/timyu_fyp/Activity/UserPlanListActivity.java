package com.example.timyu.timyu_fyp.Activity;

import android.app.AlertDialog;
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
import com.example.timyu.timyu_fyp.Class.UserManager;
import com.example.timyu.timyu_fyp.Class.UserPlan;
import com.example.timyu.timyu_fyp.R;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;

public class UserPlanListActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private PlanAdapter planListAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ArrayList<UserPlan> dataList = new ArrayList<UserPlan>();
    String USERPLANLIST = "http://timyu123.com/include/userPlan.php";

    JSONParser jsonParser = new JSONParser();

    String TAG = "test";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_plan_list);
        mRecyclerView = (RecyclerView) findViewById(R.id.Planlist);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        UserPlanListActivity.AttempUserPlan attempUserPlan = new UserPlanListActivity.AttempUserPlan();
        String uid = String.valueOf(UserManager.getInstance().getUser().getId());
        attempUserPlan.execute(uid);

    }

    private class AttempUserPlan extends AsyncTask<String,Void,JSONObject> {

        @Override
        protected JSONObject doInBackground(String... args) {
            String uid = args[0];
            ArrayList params = new ArrayList();
            params.add(new BasicNameValuePair("userId", uid));
            JSONObject json = jsonParser.makeHttpRequest(USERPLANLIST, "POST", params);

            return json;
        }

        protected void onPostExecute(JSONObject result) {

            // dismiss the dialog once product deleted
            //Toast.makeText(getApplicationContext(),result,Toast.LENGTH_LONG).show();

            try {
                if(result.optInt("success",10) == 30) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(UserPlanListActivity.this);
                    builder.setMessage("Internet Error")
                            .setCancelable(false)
                            .setPositiveButton("OK", null);
                    AlertDialog alert = builder.create();
                    alert.show();
                }else {
                    Log.d(TAG,"Got some data from server");
                    dataList.clear();
                    for(int i = 0; i<result.getJSONArray("result").length();i++){
                        UserPlan userPlan = new UserPlan();
                        userPlan.setPlanId(result.getJSONArray("result").getJSONObject(i).optString("PlanId","0"));
                        userPlan.setPlanTitle(result.getJSONArray("result").getJSONObject(i).optString("Plantitle","0"));
                        dataList.add(userPlan);
                        Log.e("",dataList.get(i).getPlanTitle());

                    }
                    // specify an adapter (see also next example)
                    planListAdapter = new PlanAdapter(dataList);
                    mRecyclerView.setAdapter(planListAdapter);

                    planListAdapter.setListener(new PlanAdapter.PlanAdapterListener() {
                        @Override
                        public void onItemClicked(View view, UserPlan userPlan) {
                            Log.e("","clicked");
                            Intent detail = new Intent(UserPlanListActivity.this, PlanDetailActivity.class);
                            detail.putExtra("PlanId",userPlan.getPlanId());
                            startActivity(detail);
                        }
                    });

                }
            }catch (Exception e){
                Log.d(TAG, "onPostExecute: "+e);
            }

            /*catch (JSONException e) {
                e.printStackTrace();
            }
            */


        }
    }
}
