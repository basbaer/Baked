package com.basbaer.baked;

import android.content.Context;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class AdapterDayOverViewRecyclerview extends RecyclerView.Adapter<AdapterDayOverViewRecyclerview.MyViewHolder> {

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        public CardView cardView;
        public TextView dateTextView;
        public TextView activityNameTextView;

        public MyViewHolder(View incomingView){
            super(incomingView);

            cardView = incomingView.findViewById(R.id.cardView_layout_RecyclerView);
            dateTextView = incomingView.findViewById(R.id.textView_date_RecyclerView);
            activityNameTextView = incomingView.findViewById(R.id.textView_activityname_RecyclerView);


        }

    }


    private ArrayList<TrackedActivity> activitiesAL;
    private Context context;

    public AdapterDayOverViewRecyclerview(Context context, ArrayList<TrackedActivity> activitiesAL){

        this.activitiesAL = activitiesAL;
        this.context = context;

    }



    @Override
    public AdapterDayOverViewRecyclerview.MyViewHolder onCreateViewHolder( ViewGroup parent, int viewType) {

        View displayedView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_dayoverview_layout, parent, false);

        MyViewHolder viewHolder = new MyViewHolder(displayedView);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {

        holder.cardView.setCardBackgroundColor(Color.parseColor(activitiesAL.get(position).getActivityColor()));

        SimpleDateFormat s = new SimpleDateFormat("h:mm a");



        Date c = activitiesAL.get(position).getCalendarDate().getTime();

        String printTime = s.format(c);

        holder.dateTextView.setText(printTime);

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

    }

    @Override
    public int getItemCount() {
        return activitiesAL.size();
    }




}
