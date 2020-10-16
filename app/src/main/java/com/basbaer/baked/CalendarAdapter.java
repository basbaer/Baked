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
    private static ArrayList<TrackedActivity> activitiesOfTheDay;
    private static View currentConvertView;


    public CalendarAdapter(Context context, HashMap<Integer, Calendar> datesAL) {
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
        if(convertView == null) {
            currentConvertView = LayoutInflater.from(context).inflate(R.layout.custom_calendar_day, parent, false);

            convertView = currentConvertView;
        }else{
            currentConvertView = convertView;
        }

        LinearLayout daysCl = currentConvertView.findViewById(R.id.daysCL);
        datesTextView = currentConvertView.findViewById(R.id.daysTV);

        //the date that is currently created
        final Calendar displayedDate = datesHashMap.get(position);


        //setting the week days
        if (position < 7) {

            datesTextView.setText(displayedDate.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.SHORT, Locale.getDefault()));

            //design
            datesTextView.setTextColor(Color.parseColor("#222222"));
            datesTextView.setTextAppearance(R.style.TextAppearance_AppCompat_Body1);
            datesTextView.setTextSize(18f);



        } else {
            //setting the date of the month
            daysCl.setMinimumHeight(getLayoutHeigth(context));

            datesTextView.setText(String.valueOf(displayedDate.get(Calendar.DAY_OF_MONTH)));

            activitiesOfTheDay = TrackedActivity.getActivitiesOfTheDay(displayedDate);

            if(!activitiesOfTheDay.isEmpty()){
                //there is definetly min 1 entry
                setUpFirstEntryOfTheDay();

                if(activitiesOfTheDay.size() == 1) {

                    //intent to the AddActivity (if there is more than one entry, first a dayOverview opens)
                    onClickListenerForFirstActivity(context);

                }else if(activitiesOfTheDay.size() > 1){


                    setUpSecondEntryOfTheDay();

                    if(activitiesOfTheDay.size() > 2){

                        setUpThirdEntryOfTheDay();

                        if(activitiesOfTheDay.size() == 4){

                            setUpForthEntryOfTheDay();

                        }else if(activitiesOfTheDay.size() > 4){

                            setUpMoreThanFourEntryOfTheDay();

                        }

                    }

                }


            }




            daysCl.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    ArrayList<TrackedActivity> activities = TrackedActivity.getActivitiesOfTheDay(displayedDate);


                    if(activities.size() < 2) {

                        Intent intentToAddActivity = new Intent(context, AddActivity.class);

                        long dateInSeconds = datesHashMap.get(position).getTime().getTime();

                        intentToAddActivity.putExtra("date", dateInSeconds);

                        context.startActivity(intentToAddActivity);

                    }else{

                        //intent to day overview
                        Intent intentToDayOverview = new Intent(context, DayOverview.class);

                        intentToDayOverview.putExtra("date", displayedDate.getTime().getTime());

                        context.startActivity(intentToDayOverview);

                    }
                }
            });



        }


        return convertView;
    }


    private static int getLayoutHeigth(Context context) {

        DisplayMetrics metrics = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(metrics);

        int height = metrics.heightPixels;

        height -= 50;

        //getting the weeks of the month
        if (weeksOfMonth == 0) {
            weeksOfMonth = 6;

        }

        return height / (weeksOfMonth + 1);
    }

    private static void setUpFirstEntryOfTheDay() {


        CardView cardView_1 = currentConvertView.findViewById(R.id.cardView_Adapter_1);

        cardView_1.setCardBackgroundColor(Color.parseColor(activitiesOfTheDay.get(0).getActivityColor()));

        cardView_1.setVisibility(View.VISIBLE);

        TextView textView_1 = currentConvertView.findViewById(R.id.ActivityTV_1);

        textView_1.setText(activitiesOfTheDay.get(0).getActivityName());

    }

    private static void setUpSecondEntryOfTheDay() {


        CardView cardView_2 = currentConvertView.findViewById(R.id.cardView_Adapter_2);

        cardView_2.setCardBackgroundColor(Color.parseColor(activitiesOfTheDay.get(1).getActivityColor()));

        cardView_2.setVisibility(View.VISIBLE);

        TextView textView_2 = currentConvertView.findViewById(R.id.ActivityTV_2);

        textView_2.setText(activitiesOfTheDay.get(1).getActivityName());

    }

    private static void setUpThirdEntryOfTheDay() {

        CardView cardView_3 = currentConvertView.findViewById(R.id.cardView_Adapter_3);

        cardView_3.setCardBackgroundColor(Color.parseColor(activitiesOfTheDay.get(2).getActivityColor()));

        cardView_3.setVisibility(View.VISIBLE);

        TextView textView_3 = currentConvertView.findViewById(R.id.ActivityTV_3);

        textView_3.setText(activitiesOfTheDay.get(2).getActivityName());

    }

    private static void setUpForthEntryOfTheDay() {

        CardView cardView_4 = currentConvertView.findViewById(R.id.cardView_Adapter_4);

        cardView_4.setCardBackgroundColor(Color.parseColor(activitiesOfTheDay.get(3).getActivityColor()));

        cardView_4.setVisibility(View.VISIBLE);

        TextView textView_4 = currentConvertView.findViewById(R.id.ActivityTV_4);

        textView_4.setText(activitiesOfTheDay.get(3).getActivityName());

    }

    private static void setUpMoreThanFourEntryOfTheDay() {

        CardView cardView_4 = currentConvertView.findViewById(R.id.cardView_Adapter_4);

        cardView_4.setCardBackgroundColor(Color.parseColor("#FFFFFF"));


        cardView_4.setVisibility(View.VISIBLE);

        TextView textView_4 = currentConvertView.findViewById(R.id.ActivityTV_4);

        textView_4.setText("  .  .  .  ");

        textView_4.setPadding(0, 0, 0, 0);


    }

    private static void onClickListenerForFirstActivity(Context context){

        final Context mContext = context;

        TextView textView_entry_1 = currentConvertView.findViewById(R.id.ActivityTV_1);

        final String firstActivity = activitiesOfTheDay.get(0).getActivityName();

        textView_entry_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intentToActivityOverview = new Intent(mContext, ActivityOverview.class);


                intentToActivityOverview.putExtra("activity", firstActivity);


                mContext.startActivity(intentToActivityOverview);

            }
        });


    }
}
