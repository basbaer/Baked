package com.basbaer.baked;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.basbaer.baked.databinding.ActivityOverviewBinding;

public class ActivityOverview extends AppCompatActivity {

    private ActivityOverviewBinding activityOverviewBinding;
    String activityName;
    TextView activityNameOverview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityOverviewBinding = ActivityOverviewBinding.inflate(getLayoutInflater());
        View view = activityOverviewBinding.getRoot();
        setContentView(view);

        Intent intentFromCalendarAdapter = getIntent();

        activityName = intentFromCalendarAdapter.getStringExtra("activity");

        //if something went wrong, the user is redirected to the MainActivity
        if(activityName == null){

            Toast.makeText(this, "Uups, something went wrong", Toast.LENGTH_LONG).show();

            Intent intentToMainActivity = new Intent(getApplicationContext(), MainActivity.class);

            startActivity(intentToMainActivity);

        }

        getSupportActionBar().setTitle("Activity overview: ");


        //sets the text of the textView
        activityNameOverview = activityOverviewBinding.activityNameOverview;

        activityNameOverview.setText("Activity: " + activityName);

    }
}
