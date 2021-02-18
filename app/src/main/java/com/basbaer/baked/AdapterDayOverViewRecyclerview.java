package com.basbaer.baked;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;


public class AdapterDayOverViewRecyclerview extends RecyclerView.Adapter<AdapterDayOverViewRecyclerview.MyViewHolder> {

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        public CardView cardView;
        public TextView categoryTextView;
        public TextView activityNameTextView;

        public MyViewHolder(View incomingView){
            super(incomingView);

            cardView = incomingView.findViewById(R.id.cardView_layout_RecyclerView);
            categoryTextView = incomingView.findViewById(R.id.textView_date_RecyclerView);
            activityNameTextView = incomingView.findViewById(R.id.textView_activityname_RecyclerView);


        }

    }


    private ArrayList<TrackedActivity> activitiesAL;
    private Context context;


    public AdapterDayOverViewRecyclerview(Context context, ArrayList<TrackedActivity> activitiesAL){

        this.activitiesAL = activitiesAL;
        this.context = context;


    }



    @NonNull
    @Override
    public AdapterDayOverViewRecyclerview.MyViewHolder onCreateViewHolder( ViewGroup parent, int viewType) {

        View displayedView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_dayoverview_layout, parent, false);

        return new MyViewHolder(displayedView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        holder.cardView.setCardBackgroundColor(Color.parseColor(activitiesAL.get(position).getActivityColor()));

        holder.categoryTextView.setText(TrackedActivity.getCategory(activitiesAL.get(position).getActivityName()));

        holder.activityNameTextView.setText(activitiesAL.get(position).getActivityName());

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String firstActivity = activitiesAL.get(position).getActivityName();

                Intent intentToActivityOverview = new Intent(context, ActivityOverview.class);


                intentToActivityOverview.putExtra("activity", firstActivity);


                context.startActivity(intentToActivityOverview);

            }
        });

        holder.cardView.setOnLongClickListener(new View.OnLongClickListener() {

            final TrackedActivity ta = activitiesAL.get(position);


            @Override
            public boolean onLongClick(View v) {

                new AlertDialog.Builder(context)
                        .setIcon(android.R.drawable.ic_delete)
                        .setTitle("Deleting")
                        .setMessage("Do you really want to delete this entry?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                TrackedActivity.deleteEntry(ta);
                                holder.cardView.setVisibility(View.GONE);

                                View v = TrackedActivity.currentMonthHashMap.get(activitiesAL.get(position).getId());

                                v.setVisibility(View.GONE);

                            }
                        })
                        .setNegativeButton("No", null)
                        .show();


                return true;
            }
        });

    }

    @Override
    public int getItemCount() {
        return activitiesAL.size();
    }




}
