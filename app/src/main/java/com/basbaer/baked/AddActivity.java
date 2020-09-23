package com.basbaer.baked;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.basbaer.baked.databinding.ActivityAddBinding;
import com.basbaer.baked.databinding.AlertDialogLayoutActivityBinding;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class AddActivity extends AppCompatActivity {

    ActivityAddBinding activityAddBinding;
    AlertDialogLayoutActivityBinding alertDialogLayoutActivityBinding;

    protected static EditText dateEditText;
    protected static String activity;
    Spinner activitySpinner;
    EditText activityTypeEditText;
    EditText colorEditText;

    //list for the drop down Spinner
    List<String> previousActivtiesList;

    //
    AlertDialog alertDialog = null;
    EditText alertDialogEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityAddBinding = ActivityAddBinding.inflate(getLayoutInflater());
        View view = activityAddBinding.getRoot();
        setContentView(view);

        alertDialogLayoutActivityBinding = AlertDialogLayoutActivityBinding.inflate(getLayoutInflater());
        //EditText, where the users types what is requested (e.g. a group name)
        alertDialogEditText = alertDialogLayoutActivityBinding.activityName;

        Intent inputIntent = getIntent();

        long dateLong = inputIntent.getLongExtra("date", -1L);

        colorEditText = activityAddBinding.colorEditText;
        dateEditText = activityAddBinding.editTextDate;
        activityTypeEditText = activityAddBinding.activityTypeEditText;

        String dateString = new SimpleDateFormat("dd.MM.yyyy").format(new Date(dateLong));

        dateEditText.setText(dateString);

        //get all Activities
        previousActivtiesList = TrackedActivity.getDifferentActivties();


        //set the spinner for the Activity
        activitySpinner = activityAddBinding.activitySpinner;
        activitySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                activity = parent.getItemAtPosition(position).toString();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

                Toast.makeText(getApplicationContext(), "Please select or add an Activity", Toast.LENGTH_SHORT).show();

            }
        });

        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter adapter = new ArrayAdapter<>(this,R.layout.spinner_activity_layout, R.id.spinnerAdapterTextView, previousActivtiesList);

        // Specify the layout to use when the list of choices appears
        //adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapter.setDropDownViewResource(R.layout.spinner_activity_layout);
        // Apply the adapter to the spinner
        activitySpinner.setAdapter(adapter);



    }

    public void addActivity(View view) {

        long date = getDate();
        String activityType = getActivityType();
        String color = getColor();

        if (date != -1L && activity != null && color != null) {

            TrackedActivity i = new TrackedActivity(activity, activityType, date, color, getApplicationContext());

            i.printDatabase();

            Intent intent = new Intent(this, MainActivity.class);

            intent.putExtra("date", date);

            startActivity(intent);

        }

    }



    private long getDate() {
        //converting the date


        String date = dateEditText.getText().toString();

        if (date.length() == 10) {

            int day = Integer.valueOf(date.substring(0, 2));
            int month = Integer.valueOf(date.substring(3, 5));
            int year = Integer.valueOf(date.substring(6, 10));


            Calendar dateCalendar = Calendar.getInstance();

            dateCalendar.set(year, month-1, day);

            return dateCalendar.getTime().getTime();

        } else {
            Toast.makeText(this, "Please add the date in format dd.mm.yyyy", Toast.LENGTH_SHORT).show();


            return -1L;
        }


    }

    protected static void changeDate(Calendar calendar){

        Date displaydate = calendar.getTime();

        String date = new SimpleDateFormat("dd.MM.yyyy").format(displaydate);

        dateEditText.setText(date);

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

    public static class DatePickerFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {

            Calendar calendar = Calendar.getInstance();
            calendar.set(year, month, day);

            AddActivity.changeDate((Calendar) calendar.clone());

        }
    }

    public void onImageButtonDateClicked(View view){

        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getSupportFragmentManager(), "datePicker");


    }

    public void onImageButtonActivityClicked(View view){

        //creating the AlertDialog
        AlertDialog.Builder builder = new AlertDialog.Builder(AddActivity.this);

        LayoutInflater layoutInflater = getLayoutInflater();

        //get's the created layout
        View alertdialogView = layoutInflater.inflate(R.layout.alert_dialog_layout_activity, null);

        alertDialogEditText = alertdialogView.findViewById(R.id.activity_name);



        //defines if the builder can be canceled
        builder.setCancelable(true);

        //sets the created Layout for the builder
        builder.setView(alertdialogView);

        alertDialog = builder.create();


        alertDialog.show();


    }

    public void alertDialogButtonClicked(View view){


        previousActivtiesList.add(alertDialogEditText.getText().toString());

        activitySpinner.setSelection(previousActivtiesList.size()-1);


        alertDialog.cancel();
    }





}

