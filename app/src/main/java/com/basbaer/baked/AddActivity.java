package com.basbaer.baked;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GestureDetectorCompat;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.basbaer.baked.databinding.ActivityAddBinding;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class AddActivity extends AppCompatActivity {

    ActivityAddBinding activityAddBinding;

    EditText datesEditText;
    EditText activityEditText;
    EditText activityTypeEditText;
    EditText colorEditText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityAddBinding = ActivityAddBinding.inflate(getLayoutInflater());
        View view = activityAddBinding.getRoot();
        setContentView(view);

        Intent inputIntent = getIntent();

        long dateLong = inputIntent.getLongExtra("date", -1L);

        colorEditText = activityAddBinding.colorEditText;
        datesEditText = activityAddBinding.editTextDate;
        activityEditText = activityAddBinding.activityEditText;
        activityTypeEditText = activityAddBinding.activityTypeEditText;

        String dateString = new SimpleDateFormat("dd.MM.yyyy").format(new Date(dateLong));

        datesEditText.setText(dateString);


    }

    public void addActivity(View view) {

        long date = getDate();
        String activity = getActivity();
        String activityType = getActivityType();
        String color = getColor();

        if (date != -1L && activity != null && color != null) {

            TrackedActivity i = new TrackedActivity(activity, activityType, date, color, getApplicationContext());

            i.printDatabase();

        }




    }

    private long getDate() {
        //converting the date


        String date = datesEditText.getText().toString();

        if (date.length() == 10) {

            int day = Integer.valueOf(date.substring(0, 2));
            int month = Integer.valueOf(date.substring(3, 5));
            int year = Integer.valueOf(date.substring(6, 10));


            Calendar dateCalendar = Calendar.getInstance();

            dateCalendar.set(year, month, day);

            return dateCalendar.getTime().getTime();

        } else {
            Toast.makeText(this, "Please add the date in format dd.mm.yyyy", Toast.LENGTH_SHORT).show();


            return -1L;
        }


    }

    private String getActivity() {
        String activity = activityEditText.getText().toString();

        if (activity != null && !activity.isEmpty()) {

            return activityEditText.getText().toString();

        } else {

            Toast.makeText(this, "Please enter what you've done", Toast.LENGTH_SHORT).show();

            return null;

        }

    }

    private String getActivityType() {

        String activityType = activityTypeEditText.getText().toString();

        if (activityType != null && !activityType.isEmpty()) {

            return activityType;

        } else {

            return "unknown";

        }

    }


    private String getColor() {


        String color = colorEditText.getText().toString();

        if (color != null && !color.isEmpty()) {

            return "#" + color;


        } else {

            Toast.makeText(this, "please enter a color", Toast.LENGTH_SHORT).show();

            return null;
        }


    }

}

