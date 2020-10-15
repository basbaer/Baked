package com.basbaer.baked;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;

class CalendarAdapter extends BaseAdapter {

    private HashMap<Integer, Calendar> datesHashMap;
    private final Context context;
    private TextView datesTextView;
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

        //the date that is currently created
        Calendar displayedDate = datesHashMap.get(position);

        //test

        //setting the week days
        if(position < 7){

            datesTextView.setText(displayedDate.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.SHORT, Locale.getDefault()));

            //design
            datesTextView.setTextColor(Color.parseColor("#222222"));
            datesTextView.setTextAppearance(R.style.TextAppearance_AppCompat_Body1);
            datesTextView.setTextSize(18f);
            //datesTextView.setPadding(5,5,5,0);


        }else{
            //setting the date of the month
            daysCl.setMinimumHeight(getLayoutHeigth(context));

            datesTextView.setText(String.valueOf(displayedDate.get(Calendar.DAY_OF_MONTH)));

            ArrayList<TrackedActivity> activitiesOfTheDay = TrackedActivity.getActivities(displayedDate);



            //setting the entries of the day
            if(activitiesOfTheDay.size() == 1) {

                setUpFirstEntryOfTheDay(convertView, activitiesOfTheDay);

            }else if(activitiesOfTheDay.size() == 2){

                setUpFirstEntryOfTheDay(convertView, activitiesOfTheDay);

                setUpSecondEntryOfTheDay(convertView, activitiesOfTheDay);


            }else if(activitiesOfTheDay.size() == 3){

                setUpFirstEntryOfTheDay(convertView, activitiesOfTheDay);

                setUpSecondEntryOfTheDay(convertView, activitiesOfTheDay);

                setUpThirdEntryOfTheDay(convertView, activitiesOfTheDay);

            }else if(activitiesOfTheDay.size() == 4){

                setUpFirstEntryOfTheDay(convertView, activitiesOfTheDay);

                setUpSecondEntryOfTheDay(convertView, activitiesOfTheDay);

                setUpThirdEntryOfTheDay(convertView, activitiesOfTheDay);

                setUpForthEntryOfTheDay(convertView, activitiesOfTheDay);

            }else if(activitiesOfTheDay.size() > 4){

                setUpFirstEntryOfTheDay(convertView, activitiesOfTheDay);

                setUpSecondEntryOfTheDay(convertView, activitiesOfTheDay);

                setUpThirdEntryOfTheDay(convertView, activitiesOfTheDay);

                setUpMoreThanFourEntryOfTheDay(convertView);

            }


                /*
                final String firstActivityOfTheDay = activitiesOfTheDay.get(0).getActivityName();

                //onClick Listener for the activities
                firstActivityTextView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent intentToActivityOverview = new Intent(context, ActivityOverview.class);

                        intentToActivityOverview.putExtra("activity", firstActivityOfTheDay);

                        context.startActivity(intentToActivityOverview);


                    }
                });

                 */





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

        height -= 50;

        //getting the weeks of the month
        if(weeksOfMonth == 0){
            weeksOfMonth = 6;

        }

        return height/(weeksOfMonth+1);
    }

    private static void setUpFirstEntryOfTheDay(View convertView, ArrayList<TrackedActivity> activitiesOfTheDay){


        CardView cardView_1 = convertView.findViewById(R.id.cardView_Adapter_1);

        cardView_1.setCardBackgroundColor(Color.parseColor(activitiesOfTheDay.get(0).getActivityColor()));

        cardView_1.setVisibility(View.VISIBLE);

        TextView textView_1 = convertView.findViewById(R.id.ActivityTV_1);

        textView_1.setText(activitiesOfTheDay.get(0).getActivityName());

    }

    private static void setUpSecondEntryOfTheDay(View convertView, ArrayList<TrackedActivity> activitiesOfTheDay){


        CardView cardView_2 = convertView.findViewById(R.id.cardView_Adapter_2);

        cardView_2.setCardBackgroundColor(Color.parseColor(activitiesOfTheDay.get(1).getActivityColor()));

        cardView_2.setVisibility(View.VISIBLE);

        TextView textView_2 = convertView.findViewById(R.id.ActivityTV_2);

        textView_2.setText(activitiesOfTheDay.get(1).getActivityName());

    }

    private static void setUpThirdEntryOfTheDay(View convertView, ArrayList<TrackedActivity> activitiesOfTheDay){

        CardView cardView_3 = convertView.findViewById(R.id.cardView_Adapter_3);

        cardView_3.setCardBackgroundColor(Color.parseColor(activitiesOfTheDay.get(2).getActivityColor()));

        cardView_3.setVisibility(View.VISIBLE);

        TextView textView_3 = convertView.findViewById(R.id.ActivityTV_3);

        textView_3.setText(activitiesOfTheDay.get(2).getActivityName());

    }

    private static void setUpForthEntryOfTheDay(View convertView, ArrayList<TrackedActivity> activitiesOfTheDay){

        CardView cardView_4 = convertView.findViewById(R.id.cardView_Adapter_4);

        cardView_4.setCardBackgroundColor(Color.parseColor(activitiesOfTheDay.get(3).getActivityColor()));

        cardView_4.setVisibility(View.VISIBLE);

        TextView textView_4 = convertView.findViewById(R.id.ActivityTV_4);

        textView_4.setText(activitiesOfTheDay.get(3).getActivityName());

    }

    private static void setUpMoreThanFourEntryOfTheDay(View convertView){

        CardView cardView_4 = convertView.findViewById(R.id.cardView_Adapter_4);

        cardView_4.setCardBackgroundColor(Color.parseColor("#FFFFFF"));


        cardView_4.setVisibility(View.VISIBLE);

        TextView textView_4 = convertView.findViewById(R.id.ActivityTV_4);

        textView_4.setText("  .  .  .  ");

        textView_4.setPadding(0,0,0,0);



    }
}
