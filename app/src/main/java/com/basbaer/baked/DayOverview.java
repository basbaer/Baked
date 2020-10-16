package com.basbaer.baked;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.basbaer.baked.databinding.ActivityDayOverviewBinding;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class DayOverview extends AppCompatActivity {

    ActivityDayOverviewBinding activityDayOverviewBinding;
    private static ArrayList<TrackedActivity> activitiesAL;
    private RecyclerView mainRecyclerView;
    private RecyclerView.LayoutManager lm;
    private AdapterDayOverViewRecyclerview adapterForMainRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityDayOverviewBinding = ActivityDayOverviewBinding.inflate(getLayoutInflater());
        View view = activityDayOverviewBinding.getRoot();
        setContentView(view);

        activitiesAL = new ArrayList<>();
        mainRecyclerView = activityDayOverviewBinding.recyclerViewDayOverView;
        lm = new LinearLayoutManager(this);
        mainRecyclerView.setLayoutManager(lm);


        //ArrayList with Activities of the Day
        Intent intentFromCalendarAdapter = getIntent();

        long dateInMs = intentFromCalendarAdapter.getLongExtra("date", -1);

        if(dateInMs != -1){
            String printDate = DateFormat.getDateInstance().format(new Date(dateInMs));

            getSupportActionBar().setTitle(printDate);

            //gets the Calendar to get the activities of this day
            Calendar tappedDayCalendar = Calendar.getInstance();

            tappedDayCalendar.setTime(new Date(dateInMs));

            //gets all activities of the day
            activitiesAL = TrackedActivity.getActivitiesOfTheDay(tappedDayCalendar);

            adapterForMainRecyclerView = new AdapterDayOverViewRecyclerview(this, activitiesAL);
            mainRecyclerView.setAdapter(adapterForMainRecyclerView);





        }else{

            //something went wrong by getting the date
            Toast.makeText(this, "Oops...We are sorry, something went wrong", Toast.LENGTH_LONG).show();

            Intent intentToMainActivity = new Intent(this, MainActivity.class);

            startActivity(intentToMainActivity);

        }




    }
}
