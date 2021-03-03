package com.basbaer.baked;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GestureDetectorCompat;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.GridView;
import android.widget.ImageView;

import com.basbaer.baked.databinding.ActivityMainBinding;
import com.google.android.material.floatingactionbutton.FloatingActionButton;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {


    ActivityMainBinding activityMainBinding;

    private GestureDetectorCompat gestureDetectorCompat;

    //the last day a ACTION_DOWN was done on
    public static CalendarDay clickedDay;

    //keeps track if the 'All' is checked
    public static final String ISALLCHECKED = "isAllChecked";
    public static SharedPreferences sharedPreferences;



    AlertDialog_RecyclerView alertDialog_selectCategories;
    Adapter_RecyclerView_AlertDialog adapter_recyclerView_alertDialog;

    //Grid View for all the Dates
    GridView calendarGV;

    //Calendar that gets displayed in the calendarGV
    //A calendar variable is always respresents one day with its corresponding information
    protected CalendarAdapter calendarAdapter;


    //Variable that represents the current displayed month
    public static Calendar displayedMonthCalendar;


    /***
     * links the menu to the activity
     * @param menu: menu in Action Bar
     * @return return of super method
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater = getMenuInflater();

        menuInflater.inflate(R.menu.main_activity_menu, menu);


        return super.onCreateOptionsMenu(menu);
    }


    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityMainBinding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = activityMainBinding.getRoot();
        setContentView(view);

        //initializing db
        TrackedActivity.createDB(this);


        //---------------------------------------------------------------------------------
        //setting up variables



        AddActivity.sharedPreferences = this.getSharedPreferences("com.basbaer.baked", Context.MODE_PRIVATE);

        sharedPreferences = this.getSharedPreferences("com.basbaer.baked", Context.MODE_PRIVATE);

        if(TrackedActivity.currentMonthHashMap == null) {
            TrackedActivity.currentMonthHashMap = new HashMap<>();
        }


        //Initializing the variables
        calendarGV = activityMainBinding.calendarGridView;
        ImageView background = activityMainBinding.backgroudMainActivityIV;

        //create db
        TrackedActivity.createDB(this);

        //delete everything
        //TrackedActivity.clearDatabase();


        //-----------------------------------------------------------------------------
        //Setting up the background



        //adjusting the picture
        ColorMatrix cm = new ColorMatrix();

        // Increase Contrast, Slightly Reduce Brightness
        float contrast = 0.5f;
        float brightness = 50;
        cm.set(new float[] { contrast, 0, 0, 0, brightness, 0,
                contrast, 0, 0, brightness, 0, 0, contrast, 0,
                brightness, 0, 0, 0, 1, 0 });

        background.setColorFilter(new ColorMatrixColorFilter(cm));

        //-----------------------------------------------------------------------------
        //setting up the calendar


        Intent intent = getIntent();

        setUpCalendar(intent);



        //----------------------------------------------------------------------------
        //Setting up of views

        //adding the Add-Button
        FloatingActionButton fab = activityMainBinding.fab;

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent intent = new Intent(getApplicationContext(), AddActivity.class);

                long dateInSeconds = Calendar.getInstance().getTime().getTime();

                intent.putExtra("date", dateInSeconds);

                startActivity(intent);



            }
        });

        setUpSwipeGestureDetector();


        //checks if a swipe to left or right is done
        calendarGV.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                gestureDetectorCompat.onTouchEvent(event);

                return true;
            }
        });



        //updating the categories list before passing it to adapter
        mCategories.updateCategoriesList();



    }


    //-------------------------------------------------------------------------------------
    //Calendar

    /**
     * Sets up up the current month as a calendar
     * @param intent: intent (-1 if there is no intent from an other activity)
     */
    private void setUpCalendar(Intent intent){
        //get's the current month
        if (displayedMonthCalendar == null) {
            displayedMonthCalendar = Calendar.getInstance();
            displayedMonthCalendar.set(Calendar.DAY_OF_MONTH, 1);
        }

        long date = intent.getLongExtra("date", -1);

        if (date == -1) {

            updateCalendar(Calendar.getInstance());

        } else {
            Calendar c = Calendar.getInstance();

            c.setTime(new Date(date));


            updateCalendar(c);
        }


    }

    /**
     * Updates the calendar view in MainActivity
     * @param calendar: Calendar of month and year which should be displayed
     */
    public void updateCalendar(Calendar calendar) {

        //setting up the calendar
        calendar.setFirstDayOfWeek(Calendar.MONDAY);


        Objects.requireNonNull(getSupportActionBar()).setTitle(calendar.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault())
                + " "
                + calendar.get(Calendar.YEAR));


        calendarAdapter = new CalendarAdapter(this, getDatesToBeDisplayed((Calendar) calendar.clone()));


        calendarGV.setAdapter(calendarAdapter);




    }

    /***
     * Handles what happens when a menu item is tapped
     * @param item: MenuItem which is tapped
     * @return return of super method
     */
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == R.id.filter_categories){
            buildAlertDialog();

        }

        if (item.getItemId() == R.id.menu_editCategoriesActivitiesList){
            Intent intent = new Intent(getApplicationContext(), EditCategoriesActivitesList.class);

            startActivity(intent);
        }




        return super.onOptionsItemSelected(item);
    }


    /***
     * Builds the AlertDialog if the filter-item of the menu in the action bar is tapped
     */
    private void buildAlertDialog(){

        adapter_recyclerView_alertDialog = new Adapter_RecyclerView_AlertDialog();

        alertDialog_selectCategories = new AlertDialog_RecyclerView(MainActivity.this, adapter_recyclerView_alertDialog);

        alertDialog_selectCategories.setCancelable(true);


        alertDialog_selectCategories.show();
    }


    /**
     * Setting up filter function
     * @param v: Checkbox of filter view
     */
    public void onCheckBoxClicked(View v){

        CheckBox checkbox = (CheckBox) v;

        boolean checked = checkbox.isChecked();


        int amountSelected = 0;


        for (mCategories category : mCategories.allCategories) {

            if(checkbox.getText().toString().equals(getString(R.string.all))) {

                TrackedActivity.setIsCheckedForAll(checked);
                sharedPreferences.edit().putBoolean(ISALLCHECKED, checked).apply();

                mCategories.updateCategoriesList();

                amountSelected = -1;

                break;
            }


            if (checkbox.getText().toString().equals(category.getName())) {
                Log.i("Checkbox", category.getName());
                category.setChecked(checked);
            }

            if(category.isChecked()){
                amountSelected++;
            }


        }

        //-1 since the "all" categorie does not count for this
        if(amountSelected != -1 &&
                amountSelected != mCategories.allCategories.size() &&
                sharedPreferences.getBoolean(ISALLCHECKED, true)) {
            //it means minimum one checkbox is not checked
            sharedPreferences.edit().putBoolean(ISALLCHECKED, false).apply();
        }else if(amountSelected == mCategories.allCategories.size()-1 && !sharedPreferences.getBoolean(ISALLCHECKED, false)){
            sharedPreferences.edit().putBoolean(ISALLCHECKED, true).apply();
        }

        mCategories.updateCategoriesList();

        updateCalendar(Calendar.getInstance());

        adapter_recyclerView_alertDialog.notifyDataSetChanged();


    }

    //---------------------------------------------------------------------------------------------
    //Gestures

    /**
     * Detects if a swipe to the right or left is done and updates the calendar
     */
    public void setUpSwipeGestureDetector(){

        //if a click is done, the CalendarAdapter get's informed
        //clickedDay is the last Day a ACTION_DOWN was done on
        SwipeGestureDetector swipeGestureDetector = new SwipeGestureDetector(new SwipeActions() {
            @Override
            public void onSwipeLeft() {
                leftSwipe();
            }

            @Override
            public void onSwipeRight() {
                rightSwipe();
            }

            @Override
            public void onSwipeUp() {

            }

            @Override
            public void onSwipeDown() {

            }

            @Override
            public void onClick() {

                if (clickedDay != null) {
                    //if a click is done, the CalendarAdapter get's informed
                    //clickedDay is the last Day a ACTION_DOWN was done on
                    performClickX(clickedDay);
                }

            }
        });

        gestureDetectorCompat = new GestureDetectorCompat(getApplicationContext(), swipeGestureDetector);


    }

    //method is called from MainActivity and does, what normally the onClickListener would do
    //onClickListener ist not setUp, since it cosumes the DOWN_EVENT and it's not possible for
    //the GridView to know if a probably a swipe is done
    public void performClickX(CalendarDay calendarDay){


        ArrayList<TrackedActivity> activities = TrackedActivity.getActivitiesOfTheDay(calendarDay.displayedDate);


        if (activities.size() < 2) {

            Intent intentToAddActivity = new Intent(getApplicationContext(), AddActivity.class);

            long dateInSeconds = calendarDay.datesHashMap.get(calendarDay.position).getTime().getTime();

            intentToAddActivity.putExtra("date", dateInSeconds);

            startActivity(intentToAddActivity);

        } else {

            //intent to day overview
            Intent intentToDayOverview = new Intent(getApplicationContext(), DayOverview.class);

            intentToDayOverview.putExtra("date", calendarDay.displayedDate.getTime().getTime());

            startActivity(intentToDayOverview);

        }

    }




    private HashMap<Integer, Calendar> getDatesToBeDisplayed(Calendar calendar) {
        //HashMap where the key says if it's for the weekdays in the first line(key = 0 - 6) or for the
        //actual dates (key = 7 - ...)
        HashMap<Integer, Calendar> allDisplayedDatesAL = new HashMap<>();

        //Calendar instance to loop through the week days
        Calendar weekdayscalendar = (Calendar) calendar.clone();

        weekdayscalendar.set(Calendar.DAY_OF_WEEK, 2);

        for(int i = 0; i < 7; i++){

            allDisplayedDatesAL.put(i, (Calendar) weekdayscalendar.clone());

            weekdayscalendar.set(Calendar.DAY_OF_WEEK, weekdayscalendar.get(Calendar.DAY_OF_WEEK) +1);

        }

        Log.i("AL", weekdayscalendar.toString());





        //determine how many day have to be added at the end
        Calendar calendarClone = (Calendar) calendar.clone();

        //amount of days in this month (needed later)
        int amountOfDaysThisMonth = calendarClone.getActualMaximum(Calendar.DAY_OF_MONTH);

        //setting the calendar to the last day of the month
        calendarClone.set(Calendar.DAY_OF_MONTH, amountOfDaysThisMonth);

        //getting the week day
        int lastWeekDayOfMonth = calendarClone.get(Calendar.DAY_OF_WEEK);

        //determining the weekdays that have to be added at the end
        int weekDaysToBeAdded;
        if (lastWeekDayOfMonth == 1) {
            weekDaysToBeAdded = 0;
        } else {
            weekDaysToBeAdded = 8 - lastWeekDayOfMonth;
        }

        //get where the calendar starts
        //sets the current calendar variable to the first day of the month
        calendarClone.set(Calendar.DAY_OF_MONTH, 1);

        //getting the current week day
        int currentWeekDay = calendarClone.get(Calendar.DAY_OF_WEEK);


        //amount of days of the past month that have to be displayed
        int displayedPastDays;

        //special case sunday
        if (currentWeekDay == 1) {
            displayedPastDays = 6;
        } else {
            displayedPastDays = currentWeekDay - 2;
        }

        //jumps back to the first day that has to be displayed
        calendarClone.add(Calendar.DAY_OF_MONTH, -displayedPastDays);


        //determining how many dates have to be displayed
        int amountOfDates = displayedPastDays + amountOfDaysThisMonth + weekDaysToBeAdded;

        CalendarAdapter.weeksOfMonth = amountOfDates/7;

        //sets the key
        int keySeter = 7;

        //adding all needed dates to the ArrayList
        while (allDisplayedDatesAL.size()-7 < amountOfDates) {

            //adds the first date that should be displayed
            allDisplayedDatesAL.put(keySeter, (Calendar) calendarClone.clone());

            keySeter++;

            //jumps one day forward
            calendarClone.add(Calendar.DAY_OF_MONTH, 1);

        }

        return allDisplayedDatesAL;

    }

    private void rightSwipe() {

        Log.i("Calendar", displayedMonthCalendar.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault()));

        //jumping from Januray to December
        if (displayedMonthCalendar.get(Calendar.MONTH) == Calendar.JANUARY) {

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
        if (displayedMonthCalendar.get(Calendar.MONTH) == Calendar.DECEMBER) {

            displayedMonthCalendar.set(Calendar.MONTH, 0);
            displayedMonthCalendar.set(Calendar.YEAR, displayedMonthCalendar.get(Calendar.YEAR)+1);

        } else {

            displayedMonthCalendar.set(Calendar.MONTH, displayedMonthCalendar.get(Calendar.MONTH) + 1);

        }

        updateCalendar(displayedMonthCalendar);

    }


}
