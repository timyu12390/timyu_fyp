package com.example.timyu.timyu_fyp.Class;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.timyu.timyu_fyp.R;
import com.google.android.gms.vision.text.Text;

import java.util.ArrayList;

/**
 * Created by timyu on 5/5/2017.
 */

public class PlanDetailAdapter extends RecyclerView.Adapter<PlanDetailAdapter.ViewHolder> {
    ArrayList<UserPlanDetail> data = new ArrayList<>();
    private PlanDetailAdapterListener listener;
    @Override
    public PlanDetailAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.userplan_detail_view, parent, false);
        // set the view's size, margins, paddings and layout parameters
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    public PlanDetailAdapter(ArrayList<UserPlanDetail> dataList){
        data = dataList;
    }

    @Override
    public void onBindViewHolder(PlanDetailAdapter.ViewHolder holder, int position) {
        holder.textTime.setText(data.get(position).getPlanTime() +"");
        holder.textTitle.setText(data.get(position).getPlanName());
    }


    @Override
    public int getItemCount() {
        return data.size();
    }

    public interface PlanDetailAdapterListener{
        void onItemClicked(View view,UserPlanDetail userPlanDetail);

    }
    public void setListener(PlanDetailAdapterListener listener){
        this.listener = listener;
    }



    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        // each data item is just a string in this case
        public TextView textTitle;
        public TextView textTime;

        public ViewHolder(View v) {
            super(v);
            textTitle = (TextView)v.findViewById(R.id.txtName);
            textTime = (TextView)v.findViewById(R.id.txtTime);
            v.setOnClickListener(this);
        }
        public void onClick(View v){
            if(listener != null){
                listener.onItemClicked(v, data.get(getAdapterPosition()));
            }
        }
    }
}
