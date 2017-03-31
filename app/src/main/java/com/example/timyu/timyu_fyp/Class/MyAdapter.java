package com.example.timyu.timyu_fyp.Class;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.timyu.timyu_fyp.Activity.PlanDetailActivity;
import com.example.timyu.timyu_fyp.R;
import com.google.android.gms.location.places.Place;

import java.util.List;

/**
 * Created by timyu on 26/2/2017.
 */

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
    private List<PlaceItem> data;
    private Context context;
    private MyAdapterListener listener;

    public MyAdapter(List<PlaceItem> list) {
        data = list;
    }
    public interface MyAdapterListener{
        void onItemClicked(View view,PlaceItem placeItem);

    }
    public void setListener(MyAdapterListener listener){
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        context = viewGroup.getContext();
        View v = LayoutInflater.from(context).inflate(R.layout.my_plan_view, viewGroup, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        PlaceItem placeItem = data.get(i);
        String title = "";
        switch (placeItem.getPlaceNum()){
            case 1:
                title = " First Place";
                break;
            case 2:
                title = " Second Place";
                break;
            case 3:
                title = " Third Place";
                break;
            case 4:
                title = " Forth Place";
                break;
            case 5:
                title = " Fifth Place";
                break;
            case 6:
                title = " Sixth Place";
                break;
        }
        viewHolder.textView.setText("Day " + placeItem.getDay() + title);
        //System.out.print(placeItem.getPlaceNum());
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void refresh(List<PlaceItem> data) {
        this.data.clear();
        this.data.addAll(data);
        notifyDataSetChanged();
    }

    public void add(List<PlaceItem> data) {
        this.data.addAll(data);
        notifyDataSetChanged();
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView textView;

        public ViewHolder(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.txtPlace);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if(listener != null){
                listener.onItemClicked(v,data.get(getAdapterPosition()));
            }
        }
    }

}