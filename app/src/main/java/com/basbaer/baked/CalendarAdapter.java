package com.basbaer.baked;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
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

    private  HashMap<Integer, Calendar> datesHashMap;
    private final Context context;
    private TextView datesTextView;
    public static int weeksOfMonth;
    private static ArrayList<TrackedActivity> activitiesOfTheDay;
    private static View currentConvertView;




    public CalendarAdapter(Context context, HashMap<Integer, Calendar> datesAL) {
        super();

        this.context = context;
        this.datesHashMap = datesAL;

        TrackedActivity.currentMonthHashMap.clear();


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

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {


        //inflates the View with the custom_calendar_day.xml
        if (convertView == null) {
            currentConvertView = LayoutInflater.from(context).inflate(R.layout.custom_calendar_day, parent, false);

            convertView = currentConvertView;
        } else {
            currentConvertView = convertView;
        }

        final LinearLayout daysCl = currentConvertView.findViewById(R.id.daysCL);
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

            if (!activitiesOfTheDay.isEmpty()) {
                //there is definetly min 1 entry
                setUpFirstEntryOfTheDay();

                if (activitiesOfTheDay.size() == 1) {

                    //intent to the AddActivity (if there is more than one entry, first a dayOverview opens)
                    onClickListenerForFirstActivity(context);

                    //onLongClickListenerForDeleting
                    onLongClickListenerForFirstActivity(context);

                } else if (activitiesOfTheDay.size() > 1) {


                    setUpSecondEntryOfTheDay();

                    if (activitiesOfTheDay.size() > 2) {

                        setUpThirdEntryOfTheDay();

                        if (activitiesOfTheDay.size() == 4) {

                            setUpForthEntryOfTheDay();

                        } else if (activitiesOfTheDay.size() > 4) {

                            setUpMoreThanFourEntryOfTheDay(position);

                        }

                    }

                }


            }

            daysCl.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {

                    //saves this date to the MainActivity, so the MainActivity is able to send it to the
                    //performClickX() method in case, there is no swipe done
                    MainActivity.clickedDay = new CalendarDay(displayedDate, datesHashMap, context, position);

                    return false;
                }
            });

        }

        return convertView;
    }

    //method is called from MainActivity and does, what normally the onClickListener would do
    //onClickListener ist not setUp, since it cosumes the DOWN_EVENT and it's not possible for
    //the GridView to know if a probably a swipe is done
    public static void performClickX(CalendarDay calendarDay){


        ArrayList<TrackedActivity> activities = TrackedActivity.getActivitiesOfTheDay(calendarDay.displayedDate);


        if (activities.size() < 2) {

            Intent intentToAddActivity = new Intent(calendarDay.context, AddActivity.class);

            long dateInSeconds = calendarDay.datesHashMap.get(calendarDay.position).getTime().getTime();

            intentToAddActivity.putExtra("date", dateInSeconds);

            calendarDay.context.startActivity(intentToAddActivity);

        } else {

            //intent to day overview
            Intent intentToDayOverview = new Intent(calendarDay.context, DayOverview.class);

            intentToDayOverview.putExtra("date", calendarDay.displayedDate.getTime().getTime());

            calendarDay.context.startActivity(intentToDayOverview);

        }

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

        TrackedActivity.currentMonthHashMap.put(activitiesOfTheDay.get(0).getId(), cardView_1);

    }

    private static void setUpSecondEntryOfTheDay() {


        CardView cardView_2 = currentConvertView.findViewById(R.id.cardView_Adapter_2);

        cardView_2.setCardBackgroundColor(Color.parseColor(activitiesOfTheDay.get(1).getActivityColor()));

        cardView_2.setVisibility(View.VISIBLE);

        TextView textView_2 = currentConvertView.findViewById(R.id.ActivityTV_2);

        textView_2.setText(activitiesOfTheDay.get(1).getActivityName());

        TrackedActivity.currentMonthHashMap.put(activitiesOfTheDay.get(1).getId(), cardView_2);

    }

    private static void setUpThirdEntryOfTheDay() {

        CardView cardView_3 = currentConvertView.findViewById(R.id.cardView_Adapter_3);

        cardView_3.setCardBackgroundColor(Color.parseColor(activitiesOfTheDay.get(2).getActivityColor()));

        cardView_3.setVisibility(View.VISIBLE);

        TextView textView_3 = currentConvertView.findViewById(R.id.ActivityTV_3);

        textView_3.setText(activitiesOfTheDay.get(2).getActivityName());

        TrackedActivity.currentMonthHashMap.put(activitiesOfTheDay.get(2).getId(), cardView_3);

    }

    private static void setUpForthEntryOfTheDay() {

        CardView cardView_4 = currentConvertView.findViewById(R.id.cardView_Adapter_4);

        cardView_4.setCardBackgroundColor(Color.parseColor(activitiesOfTheDay.get(3).getActivityColor()));

        cardView_4.setVisibility(View.VISIBLE);

        TextView textView_4 = currentConvertView.findViewById(R.id.ActivityTV_4);

        textView_4.setText(activitiesOfTheDay.get(3).getActivityName());

        TrackedActivity.currentMonthHashMap.put(activitiesOfTheDay.get(3).getId(), cardView_4);

    }

    private static void setUpMoreThanFourEntryOfTheDay(int position) {

        CardView cardView_4 = currentConvertView.findViewById(R.id.cardView_Adapter_4);

        cardView_4.setCardBackgroundColor(Color.parseColor("#FFFFFF"));


        cardView_4.setVisibility(View.VISIBLE);

        TextView textView_4 = currentConvertView.findViewById(R.id.ActivityTV_4);

        textView_4.setText("  .  .  .  ");

        textView_4.setPadding(0, 0, 0, 0);

        TrackedActivity.currentMonthHashMap.put(activitiesOfTheDay.get(position).getId(), cardView_4);


    }

    private static void onClickListenerForFirstActivity(Context context) {

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


    private void onLongClickListenerForFirstActivity(final Context context) {

        final Context mContext = context;

        final TextView textView_entry_1 = currentConvertView.findViewById(R.id.ActivityTV_1);

        final TrackedActivity ta = activitiesOfTheDay.get(0);

        textView_entry_1.setOnLongClickListener(new View.OnLongClickListener() {
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
                                textView_entry_1.setVisibility(View.GONE);
                            }
                        })
                        .setNegativeButton("No", null)
                        .show();



                MainActivity.calendarAdapter.notifyDataSetChanged();

                return false;
            }
        });


    }

    public static void deleteEntry(Calendar tappedCalendar, int position){

        ArrayList<TrackedActivity> trackedActivity = TrackedActivity.getActivitiesOfTheDay(tappedCalendar);

        trackedActivity.remove(position);


    }


}
