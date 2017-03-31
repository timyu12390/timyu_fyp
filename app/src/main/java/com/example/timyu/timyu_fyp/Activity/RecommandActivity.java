package com.example.timyu.timyu_fyp.Activity;

import android.app.Dialog;
import android.content.DialogInterface;
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

import com.example.timyu.timyu_fyp.Class.RecommendationAdapter;
import com.example.timyu.timyu_fyp.Class.SuggestQuestion;
import com.example.timyu.timyu_fyp.R;

import java.util.ArrayList;

public class RecommandActivity extends AppCompatActivity {

    String[] abc = {"1", "2", "3"};
    ArrayList<String> travelSpotName = new ArrayList<>();
    int numberOfTime = 0;
    String btnFinshText = "Finsh";
    String btnNextText = "Next";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recommand);
    }


    public void btnQuestion(View v) {
        Log.d("", "clicked");
        LayoutInflater inflater = RecommandActivity.this.getLayoutInflater();

        AlertDialog.Builder builder = new AlertDialog.Builder(RecommandActivity.this);
        View view = inflater.inflate(R.layout.layout_recommendationview,null);


        final SuggestQuestion suggestQuestion = new SuggestQuestion();
        final ArrayList<SuggestQuestion> nameTest = new ArrayList<>();




        for(int i = 0; i<5;i++){
            int abc = 10;
            abc+= abc*i;
            suggestQuestion.setPlaceId(String.valueOf(abc));
            suggestQuestion.setPlaceName(String.valueOf(abc));
            suggestQuestion.setStatus(true);

            nameTest.add(suggestQuestion);
        }

        RecyclerView list = (RecyclerView) view.findViewById(R.id.list);

        final RecommendationAdapter recommendationAdapter = new RecommendationAdapter(nameTest);
        recommendationAdapter.setOnItemClickListener(new RecommendationAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Log.d("Test Class",suggestQuestion.getPlaceName());
                //nameTest.set(position, nameTest.get(position).getPlaceName() + "123");
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
}
