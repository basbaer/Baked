package com.basbaer.baked;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.basbaer.baked.databinding.ActivityDayOverviewBinding;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class DayOverview extends AppCompatActivity {

    ActivityDayOverviewBinding activityDayOverviewBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityDayOverviewBinding = ActivityDayOverviewBinding.inflate(getLayoutInflater());
        View view = activityDayOverviewBinding.getRoot();
        setContentView(view);

        ArrayList<TrackedActivity> activitiesAL;
        RecyclerView mainRecyclerView = activityDayOverviewBinding.recyclerViewDayOverView;
        RecyclerView.LayoutManager lm = new LinearLayoutManager(this);
        mainRecyclerView.setLayoutManager(lm);




        //ArrayList with Activities of the Day
        Intent intentFromCalendarAdapter = getIntent();

        final long dateInMs = intentFromCalendarAdapter.getLongExtra("date", -1);

        if(dateInMs != -1){
            String printDate = DateFormat.getDateInstance().format(new Date(dateInMs));

            getSupportActionBar().setTitle(printDate);

            //gets the Calendar to get the activities of this day
            Calendar tappedDayCalendar = Calendar.getInstance();

            tappedDayCalendar.setTime(new Date(dateInMs));

            //gets all activities of the day
            activitiesAL = TrackedActivity.getActivitiesOfTheDay(tappedDayCalendar);

            AdapterDayOverViewRecyclerview adapterForMainRecyclerView = new AdapterDayOverViewRecyclerview(this, activitiesAL);

            mainRecyclerView.setAdapter(adapterForMainRecyclerView);

            //adding the Add-Button
            FloatingActionButton fab = activityDayOverviewBinding.fabOverview;

            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    Intent intent = new Intent(getApplicationContext(), AddActivity.class);


                    intent.putExtra("date", dateInMs);

                    startActivity(intent);



                }
            });






        }else{

            //something went wrong by getting the date
            Toast.makeText(this, "Oops...We are sorry, something went wrong", Toast.LENGTH_LONG).show();

            Intent intentToMainActivity = new Intent(this, MainActivity.class);

            startActivity(intentToMainActivity);

        }




    }
}
