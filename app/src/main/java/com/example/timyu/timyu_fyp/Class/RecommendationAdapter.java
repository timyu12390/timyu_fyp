package com.example.timyu.timyu_fyp.Class;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.timyu.timyu_fyp.Activity.RecommandActivity;
import com.example.timyu.timyu_fyp.R;

import java.util.ArrayList;

/**
 * Created by timyu on 29/3/2017.
 */

public class RecommendationAdapter extends RecyclerView.Adapter<RecommendationAdapter.MyViewHolder> {

    ArrayList<SuggestQuestion> data;
    OnItemClickListener listener;

    public RecommendationAdapter(ArrayList<SuggestQuestion> nameList){
        this.data = nameList;
    }

    @Override
    public RecommendationAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.suggestion_name_textview, parent, false));
    }

    @Override
    public void onBindViewHolder(RecommendationAdapter.MyViewHolder holder, int position) {
            if(data.get(position).getStatus()){
                holder.textView.setText("☑ " +data.get(position).getPlaceName());
            }else {
                holder.textView.setText(data.get(position).getPlaceName());
            }

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int pos);
    }

    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView textView;
        public MyViewHolder(View itemView) {
            super(itemView);

            textView = (TextView) itemView.findViewById(R.id.suggest_title);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (listener != null) listener.onItemClick(view, getAdapterPosition());
        }
    }


}
