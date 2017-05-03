package com.example.timyu.timyu_fyp.Activity;

        import android.app.Dialog;
        import android.content.DialogInterface;
        import android.os.AsyncTask;
        import android.support.v7.app.AlertDialog;
        import android.support.v7.app.AppCompatActivity;
        import android.os.Bundle;
        import android.support.v7.widget.LinearLayoutManager;
        import android.support.v7.widget.RecyclerView;
        import android.util.Log;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.widget.TextView;
        import android.widget.Toast;

        import com.example.timyu.timyu_fyp.Class.JSONParser;
        import com.example.timyu.timyu_fyp.Class.RecommendationAdapter;
        import com.example.timyu.timyu_fyp.Class.SuggestQuestion;
        import com.example.timyu.timyu_fyp.Class.User;
        import com.example.timyu.timyu_fyp.Class.UserManager;
        import com.example.timyu.timyu_fyp.R;

        import org.apache.http.message.BasicNameValuePair;
        import org.json.JSONObject;

        import java.util.ArrayList;

public class RecommandActivity extends AppCompatActivity {


    ArrayList<String> travelSpotName = new ArrayList<>();
    int numberOfTime = 0;
    String btnFinshText = "Finsh";
    String btnNextText = "Next";
    String RECOMMEND = "http://timyu123.com/include/recommend.php";
    String TAG = "test";
    JSONParser jsonParser = new JSONParser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recommand);
    }


    public void btnQuestion(View v) {
        Log.d("", "clicked");
        AttemptRecommend attemptRecommend= new AttemptRecommend();
        attemptRecommend.execute("台灣");

        AlertDialog.Builder builder = new AlertDialog.Builder(RecommandActivity.this);
        LayoutInflater inflater = RecommandActivity.this.getLayoutInflater();
        View view = inflater.inflate(R.layout.layout_recommendationview,null);

        final ArrayList<SuggestQuestion> nameTest = new ArrayList<>();

        for(int i = 0; i<5;i++){
            int abc = 10;
            abc+= abc*i;
            SuggestQuestion suggestQuestion = new SuggestQuestion();
            suggestQuestion.setPlaceId(String.valueOf(abc));
            suggestQuestion.setPlaceName(String.valueOf(abc));
            suggestQuestion.setStatus(false);

            nameTest.add(suggestQuestion);
        }

        RecyclerView list = (RecyclerView) view.findViewById(R.id.list);

        //Row on click
        final RecommendationAdapter recommendationAdapter = new RecommendationAdapter(nameTest);
        recommendationAdapter.setOnItemClickListener(new RecommendationAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                SuggestQuestion suggestQuestion2 = new SuggestQuestion();
                System.out.print(suggestQuestion2.getPlaceName());
                for(SuggestQuestion suggestQuestion : nameTest){
                    suggestQuestion.setStatus(false);
                }
                nameTest.get(position).setStatus(true);
                recommendationAdapter.notifyDataSetChanged();

            }
        });
        list.setAdapter(recommendationAdapter);
        list.setLayoutManager(new LinearLayoutManager(this));


        builder.setView(view);
        // Add action buttons
        if(numberOfTime<3) {
            builder.setPositiveButton(btnNextText, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int id) {
                    numberOfTime++;
                    System.out.println(numberOfTime);
                    btnQuestion(null);
                }
            });
            builder.show();
        }else if(numberOfTime>=3){
            builder.setPositiveButton(btnFinshText, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int id) {
                    System.out.println(numberOfTime);
                    numberOfTime=0;

                }
            });
            builder.show();
        }


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

            // dismiss the dialog once product deleted
            //Toast.makeText(getApplicationContext(),result,Toast.LENGTH_LONG).show();

            try {
                if(result.optInt("success",10) == 30) {
                    android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(RecommandActivity.this);
                    builder.setMessage("Internet Error")
                            .setCancelable(false)
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    //do things
                                }
                            });
                    android.app.AlertDialog alert = builder.create();
                    alert.show();
                }else {
                    Log.d(TAG,"get some data from server");
                    Log.d(TAG,result+"");
                }
            }catch (Exception e){
                Log.d(TAG, "onPostExecute: "+e);
            }

        }

    }
}
