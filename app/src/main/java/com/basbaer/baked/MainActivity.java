package com.basbaer.baked;


import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.GridView;

import com.basbaer.baked.databinding.ActivityMainBinding;


import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding activityMainBinding;

    //Grid View for all the Dates
    GridView calendarGV;

    //Calendar that gets displayed in the calendarGV
    //A calendar variable is always respresents one day with its corresponding information
    protected static CalendarAdapter calendarAdapter;



    //needed for checking if a swipe is done
    float startingPointOfTouch = 0;
    float endPointOfTouch = 0;

    //Variable that represents the current displayed month
    public static Calendar displayedMonthCalendar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityMainBinding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = activityMainBinding.getRoot();
        setContentView(view);

        //Initializing the variables
        calendarGV = activityMainBinding.calendarGridView;

        if (displayedMonthCalendar == null) {
            displayedMonthCalendar = Calendar.getInstance();
            displayedMonthCalendar.set(Calendar.DAY_OF_MONTH, 1);
        }


        Intent intent = getIntent();

        long date = intent.getLongExtra("date", -1);

        if (date == -1) {

            updateCalendar(Calendar.getInstance());

        } else {
            Calendar activeCalendar = Calendar.getInstance();

            activeCalendar.setTime(new Date(date));
        }






        //checks if a swipe to left or right is done
        calendarGV.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {


                Log.d("TouchEvent", event.toString());
                if (event.getAction() == MotionEvent.ACTION_DOWN) {

                    startingPointOfTouch = event.getX();

                }


                if (event.getAction() == MotionEvent.ACTION_UP) {

                    endPointOfTouch = event.getX();

                    detectIfSwipeIsDone(startingPointOfTouch, endPointOfTouch);
                }

                return false;
            }
        });





    }


    public void updateCalendar(Calendar calendar) {

        //setting up the calendar
        calendar.setFirstDayOfWeek(Calendar.MONDAY);

        Log.i("d", String.valueOf(calendar.get(Calendar.YEAR)));

        getSupportActionBar().setTitle(calendar.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault())
                + " "
                + calendar.get(Calendar.YEAR));


        calendarAdapter = new CalendarAdapter(this, getDatesToBeDisplayed((Calendar) calendar.clone()));



        calendarGV.setAdapter(calendarAdapter);


    }

    private HashMap<Integer, Calendar> getDatesToBeDisplayed(Calendar calendar) {
        //HashMap where the key says if it's for the weekdays in the first line(key = 0 - 6) or for the
        //actual dates (key = 7 - ...)
        HashMap<Integer, Calendar> allDisplayedDatesAL = new HashMap<Integer, Calendar>();

        //Calendar instance to loop through the week days
        Calendar weekdayscalendar = Calendar.getInstance();

        weekdayscalendar.set(Calendar.DAY_OF_WEEK, 2);

        for(int i = 0; i < 7; i++){

            allDisplayedDatesAL.put(i, (Calendar) weekdayscalendar.clone());

            weekdayscalendar.set(Calendar.DAY_OF_WEEK, weekdayscalendar.get(Calendar.DAY_OF_WEEK) +1);

        }

        Log.i("AL", weekdayscalendar.toString());





        //determine how many day have to be added at the end

        //amount of days in this month (needed later)
        int amountOfDaysThisMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);

        //setting the calendar to the last day of the month
        calendar.set(Calendar.DAY_OF_MONTH, amountOfDaysThisMonth);

        //getting the week day
        int lastWeekDayOfMonth = calendar.get(Calendar.DAY_OF_WEEK);

        //determining the weekdays that have to be added at the end
        int weekDaysToBeAdded;
        if (lastWeekDayOfMonth == 1) {
            weekDaysToBeAdded = 0;
        } else {
            weekDaysToBeAdded = 8 - lastWeekDayOfMonth;
        }

        //get where the calendar starts
        //sets the current calendar variable to the first day of the month
        calendar.set(Calendar.DAY_OF_MONTH, 1);

        //getting the current week day
        int currentWeekDay = calendar.get(Calendar.DAY_OF_WEEK);


        //amount of days of the past month that have to be displayed
        int displayedPastDays;

        //special case sunday
        if (currentWeekDay == 1) {
            displayedPastDays = 6;
        } else {
            displayedPastDays = currentWeekDay - 2;
        }

        //jumps back to the first day that has to be displayed
        calendar.add(Calendar.DAY_OF_MONTH, -displayedPastDays);


        //determining how many dates have to be displayed
        int amountOfDates = displayedPastDays + amountOfDaysThisMonth + weekDaysToBeAdded;

        //sets the key
        int keySeter = 7;

        //adding all needed dates to the ArrayList
        while (allDisplayedDatesAL.size()-7 < amountOfDates) {

            //adds the first date that should be displayed
            allDisplayedDatesAL.put(keySeter, (Calendar) calendar.clone());

            keySeter++;

            //jumps one day forward
            calendar.add(Calendar.DAY_OF_MONTH, 1);

        }

        return allDisplayedDatesAL;

    }


    private void detectIfSwipeIsDone(float start, float end) {

        //leftswipe
        if (start > end + 400) {

            Log.i("leftSwipe", "Done");

            startingPointOfTouch = 0;
            endPointOfTouch = 0;

            leftSwipe();
        } else if (end > start + 400) {
            Log.i("rightSwipe", "Done");

            startingPointOfTouch = 0;
            endPointOfTouch = 0;

            rightSwipe();
        }

    }

    private void rightSwipe() {

        Log.i("Calendar", displayedMonthCalendar.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault()));

        //jumping from Januray to December
        if (displayedMonthCalendar.get(Calendar.MONTH) == 0) {

            displayedMonthCalendar.set(Calendar.MONTH, 11);
            displayedMonthCalendar.set(Calendar.YEAR, displayedMonthCalendar.get(Calendar.YEAR)-1);

        } else {

            displayedMonthCalendar.set(Calendar.MONTH, displayedMonthCalendar.get(Calendar.MONTH) - 1);
        }

        Log.i("Calendar", displayedMonthCalendar.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault()));

        updateCalendar(displayedMonthCalendar);

    }

    private void leftSwipe() {

        Log.i("Calendar", displayedMonthCalendar.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault()));

        //jumping from Januaray to December
        if (displayedMonthCalendar.get(Calendar.MONTH) == 11) {

            displayedMonthCalendar.set(Calendar.MONTH, 0);
            displayedMonthCalendar.set(Calendar.YEAR, displayedMonthCalendar.get(Calendar.YEAR)+1);

        } else {

            displayedMonthCalendar.set(Calendar.MONTH, displayedMonthCalendar.get(Calendar.MONTH) + 1);

        }

        updateCalendar(displayedMonthCalendar);

    }


}
