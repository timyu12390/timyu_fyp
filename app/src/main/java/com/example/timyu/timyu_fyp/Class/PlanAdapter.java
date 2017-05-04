package com.example.timyu.timyu_fyp.Class;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.timyu.timyu_fyp.R;

import org.w3c.dom.Text;

import java.util.ArrayList;

/**
 * Created by timyu on 4/5/2017.
 */

public class PlanAdapter extends RecyclerView.Adapter<PlanAdapter.ViewHolder> {
    ArrayList<UserPlan> data = new ArrayList<UserPlan>();
    private PlanAdapterListener listener;
    @Override
    public PlanAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.userplan_view, parent, false);
        // set the view's size, margins, paddings and layout parameters
        ViewHolder vh = new ViewHolder(v);

        return vh;
    }


    public PlanAdapter(ArrayList<UserPlan> dataList){
        data = dataList;
    }
    public interface PlanAdapterListener{
        void onItemClicked(View view,UserPlan userPlan);

    }

    public void setListener(PlanAdapterListener listener){
        this.listener = listener;
    }

    @Override
    public void onBindViewHolder(PlanAdapter.ViewHolder holder, int position) {
        holder.mTextView.setText(data.get(position).getPlanTitle());
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        // each data item is just a string in this case
        public TextView mTextView;

        public ViewHolder(View v) {
            super(v);
            mTextView = (TextView)v.findViewById(R.id.txtPlanTitle);
            v.setOnClickListener(this);
        }
        public void onClick(View v){
            if(listener != null){
                listener.onItemClicked(v,data.get(getAdapterPosition()));
            }
        }
    }

}
