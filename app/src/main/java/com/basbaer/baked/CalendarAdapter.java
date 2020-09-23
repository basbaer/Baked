package com.basbaer.baked;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;

class CalendarAdapter extends BaseAdapter {

    private HashMap<Integer, Calendar> datesHashMap;
    private final Context context;
    private TextView datesTextView;
    private TextView firstActivityTextView;
    public static int weeksOfMonth;



    public CalendarAdapter(Context context, HashMap<Integer, Calendar> datesAL){
        super();

        this.context = context;
        this.datesHashMap = datesAL;


    }


    @Override
    public int getCount() {
        return datesHashMap.size();
    }

    @Override
    public Object getItem(int position) {
        return datesHashMap.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {


        //inflates the View with the custom_calendar_day.xml
        convertView = LayoutInflater.from(context).inflate(R.layout.custom_calendar_day, parent, false);

        LinearLayout daysCl = convertView.findViewById(R.id.daysCL);


        datesTextView = convertView.findViewById(R.id.daysTV);
        firstActivityTextView = convertView.findViewById(R.id.firstActivityTV);


        Calendar displayedDate = datesHashMap.get(position);

        if(position < 7){

            datesTextView.setText(displayedDate.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.SHORT, Locale.getDefault()));

            //design
            datesTextView.setTextColor(Color.parseColor("#222222"));
            datesTextView.setTextAppearance(R.style.TextAppearance_AppCompat_Body1);
            datesTextView.setTextSize(18f);
            datesTextView.setPadding(5,5,5,0);


        }else{



            daysCl.setMinimumHeight(getLayoutHeigth(context));

            datesTextView.setText(String.valueOf(displayedDate.get(Calendar.DAY_OF_MONTH)));

            ArrayList<TrackedActivity> activitiesOfTheDay = TrackedActivity.getActivities(displayedDate);

            if(activitiesOfTheDay.size() == 1){

                datesTextView.setPadding(5,5,5,5);

                firstActivityTextView.setText(activitiesOfTheDay.get(0).getActivityName());

                firstActivityTextView.setPadding(5,5,5, 30);

                firstActivityTextView.setBackgroundColor(Color.parseColor(activitiesOfTheDay.get(0).getActivityColor()));

            }



        }




        datesTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context, AddActivity.class);

                long dateInSeconds = datesHashMap.get(position).getTime().getTime();

                intent.putExtra("date", dateInSeconds);



                context.startActivity(intent);


            }
        });

        return convertView;
    }


    private static int getLayoutHeigth(Context context){

        DisplayMetrics metrics = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(metrics);

        int height = metrics.heightPixels;

        height =- 50;

        //getting the weeks of the month
        if(weeksOfMonth == 0){
            weeksOfMonth = 6;

        }

        return height/(weeksOfMonth+1);
    }
}
