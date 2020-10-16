package com.basbaer.baked;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.database.DataSetObserver;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.basbaer.baked.databinding.ActivityOverviewBinding;
import com.basbaer.baked.databinding.SpinnerOverviewActivityBinding;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;

public class ActivityOverview extends AppCompatActivity {

    private ActivityOverviewBinding activityOverviewBinding;
    private SpinnerOverviewActivityBinding spinnerLayoutBinding;
    String activityName;
    TextView activityNameOverview;

    //code: 0 is days, 1 is weeks, 2 is months, 3 is years
    int selectedAmountItem;
    String selectedAmount;
    TextView amountInSelectedPeriod;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityOverviewBinding = ActivityOverviewBinding.inflate(getLayoutInflater());
        View view = activityOverviewBinding.getRoot();
        setContentView(view);

        amountInSelectedPeriod = activityOverviewBinding.doneThisVariableValue;
        spinnerLayoutBinding = SpinnerOverviewActivityBinding.inflate(getLayoutInflater());

        Intent intentFromCalendarAdapter = getIntent();

        activityName = intentFromCalendarAdapter.getStringExtra("activity");

        //if something went wrong, the user is redirected to the MainActivity
        if (activityName == null) {

            Toast.makeText(this, "Uups, something went wrong", Toast.LENGTH_LONG).show();

            Intent intentToMainActivity = new Intent(getApplicationContext(), MainActivity.class);

            startActivity(intentToMainActivity);

        }


        getSupportActionBar().setTitle("Activity overview");


        //sets the text of the textView
        activityNameOverview = activityOverviewBinding.activityNameValue;

        activityNameOverview.setText(activityName);

        setBackgroundColor();

        //getting the total amount the activity was done
        TextView totalAmountTV = activityOverviewBinding.totalDoneValue;

        //getting the amount
        int totalAmount = TrackedActivity.getTotalAmountActivityWasDone(activityName);

        totalAmountTV.setText(String.valueOf(totalAmount));


        //set the last done value
        TextView lastDoneTV = activityOverviewBinding.lastDoneValue;

        lastDoneTV.setText(getLastDone());


        //set the times done this month value
        TextView timesDoneThisMonthTV = activityOverviewBinding.doneThisMonthValue;

        timesDoneThisMonthTV.setText(String.valueOf(TrackedActivity.getTimesDoneThisMonth(activityName)));


        //set the times done this year value
        TextView timesDoneThisYearTV = activityOverviewBinding.doneThisYearValue;

        timesDoneThisYearTV.setText(String.valueOf(TrackedActivity.getTimesDoneThisYear(activityName)));


        //setting up the last line of the overview activity
        EditText amountET = activityOverviewBinding.amountEditText;
        selectedAmount = amountET.getText().toString();
        selectedAmountItem = 0; //since the previous selected item will always be days
        changeAmountValue();



        amountET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                selectedAmount = s.toString();

                if(selectedAmount != null && !selectedAmount.isEmpty()){

                    changeAmountValue();

                }


            }
        });


        //setting up the spinner
        Spinner spinner = activityOverviewBinding.amountSpinner;

        final String[] spinnerArray = {"day(s)", "week(s)", "month(s)", "year(s)"};
        SpinnerAdapter spinnerAdapter = new SpinnerAdapter() {
            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent) {

                View row = convertView;
                LayoutInflater inflater = getLayoutInflater();
                row = inflater.inflate(R.layout.spinner_overview_activity, null);
                TextView text = row.findViewById(R.id.textViewSpinnerDropDownOverview);
                text.setText(spinnerArray[position]);
                ImageView iv = row.findViewById(R.id.spinnerDropDownArrowIV);
                iv.setVisibility(View.GONE);

                return row;
            }

            @Override
            public void registerDataSetObserver(DataSetObserver observer) {
            }

            @Override
            public void unregisterDataSetObserver(DataSetObserver observer) {

            }

            @Override
            public int getCount() {
                return spinnerArray.length;
            }

            @Override
            public Object getItem(int position) {

                return spinnerArray[position];
            }

            @Override
            public long getItemId(int position) {
                return 0;
            }

            @Override
            public boolean hasStableIds() {
                return false;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View row = convertView;
                LayoutInflater inflater = getLayoutInflater();
                row = inflater.inflate(R.layout.spinner_overview_activity, null);
                TextView text = row.findViewById(R.id.textViewSpinnerDropDownOverview);
                text.setText(spinnerArray[position]);

                return row;
            }

            @Override
            public int getItemViewType(int position) {
                return 0;
            }

            @Override
            public int getViewTypeCount() {
                return 1;
            }

            @Override
            public boolean isEmpty() {
                return false;
            }
        };

        spinner.setAdapter(spinnerAdapter);

        //sets the wright code for the selectAmoutItem
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                selectedAmountItem = position;
                changeAmountValue();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //back to CAlendar Button
        Button backToCalendarButton = activityOverviewBinding.backToCalendarButton;

        backToCalendarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =  new Intent(getApplicationContext(), MainActivity.class);

                startActivity(intent);
            }
        });


    }

    private void setBackgroundColor() {

        ConstraintLayout cL = activityOverviewBinding.constraintLayoutOverview;

        String colorString = TrackedActivity.getColor(activityName);

        int mcolor = Color.parseColor(colorString);


        cL.setBackgroundColor(mcolor);


    }


    private String getLastDone() {

        Date latestDateOfActivity = TrackedActivity.dateActivityWasLastDone(activityName);

        if (latestDateOfActivity != null) {

            //getting the difference between today and the day the activity was last done
            //0 -> activity was last done today
            //-5 -> activity was last done 5 days ago
            // 8 -> activity will be done 8 days in the future
            int daysBetween = getDifferenceBetweenLastDoneAndCurrentDate(latestDateOfActivity);

            String dateOfLastDone = DateFormat.getDateInstance().format(latestDateOfActivity);

            if (daysBetween < -1) {

                return Math.abs(daysBetween) + " days ago \n(" + dateOfLastDone + ")";

            } else if (daysBetween == -1) {

                return Math.abs(daysBetween) + " day ago \n(" + dateOfLastDone + ")";

            } else if (daysBetween == 0) {

                return "today";

            } else if (daysBetween == 1) {

                return "will be done in " + daysBetween + " day (" + dateOfLastDone + ")";

            } else {

                return "will be done in " + daysBetween + " days (" + dateOfLastDone + ")";

            }


        } else {

            return "-";

        }


    }

    private int getDifferenceBetweenLastDoneAndCurrentDate(Date latestDateOfActivity) {

        Calendar latestCalendarOfActivity = Calendar.getInstance();

        latestCalendarOfActivity.setTime(latestDateOfActivity);

        Calendar currentCalendar = Calendar.getInstance();

        //set the currentCalendar variable to the last millisecond of the current date (so that the comparison is always right)
        //otherwise, the could be a false outcome
        currentCalendar.set(Calendar.HOUR_OF_DAY, currentCalendar.getActualMaximum(Calendar.HOUR_OF_DAY));
        currentCalendar.set(Calendar.MINUTE, currentCalendar.getActualMaximum(Calendar.MINUTE));
        currentCalendar.set(Calendar.SECOND, currentCalendar.getActualMaximum(Calendar.SECOND));
        currentCalendar.set(Calendar.MILLISECOND, currentCalendar.getActualMaximum(Calendar.MILLISECOND));

        long milliSecondsBetween = (latestCalendarOfActivity.getTime().getTime() - currentCalendar.getTime().getTime());

        int daysAdd = 0;

        if (milliSecondsBetween > 0L) {
            daysAdd = 1;
        }

        int daysbetween = (int) (milliSecondsBetween / (1000 * 60 * 60 * 24));

        return daysbetween + daysAdd;


    }

    private void changeAmountValue() {

        Calendar currentCalendar = Calendar.getInstance();
        //setting the calendar to the very first second of the day
        currentCalendar.set(currentCalendar.get(Calendar.YEAR), currentCalendar.get(Calendar.MONTH), currentCalendar.get(Calendar.DATE),
                currentCalendar.getActualMinimum(Calendar.HOUR_OF_DAY),
                currentCalendar.getActualMinimum(Calendar.MINUTE),
                currentCalendar.getActualMinimum(Calendar.SECOND));

        int daysInBetween = 0;

        if (Integer.valueOf(selectedAmount) > 0) {

            if (selectedAmountItem == 0) {


                //here are the days between today and the day depending on the selection saved
                daysInBetween = Integer.valueOf(selectedAmount);


                //if 'weeks' was selected in the spinner
            } else if (selectedAmountItem == 1) {


                Calendar calendarInPast = Calendar.getInstance();
                calendarInPast.set(Calendar.DAY_OF_WEEK, currentCalendar.getFirstDayOfWeek());


                //adds the days of the started week
                daysInBetween = currentCalendar.get(Calendar.DAY_OF_WEEK) - calendarInPast.get(Calendar.DAY_OF_WEEK);

                daysInBetween += 7 * (Integer.valueOf(selectedAmount) - 1);


                //if months was selected by the spinner
            } else if (selectedAmountItem == 2) {
                Calendar calendarInPast = Calendar.getInstance();
                calendarInPast.set(Calendar.DAY_OF_MONTH, currentCalendar.getActualMinimum(Calendar.DAY_OF_MONTH));

                daysInBetween = currentCalendar.get(Calendar.DATE);

                for(int i = 0; i < Integer.valueOf(selectedAmount)-1; i++){
                    calendarInPast.roll(Calendar.MONTH, -1);

                    if(calendarInPast.get(Calendar.MONTH) == calendarInPast.getActualMaximum(Calendar.MONTH)){

                        calendarInPast.roll(Calendar.YEAR, -1);

                    }

                    daysInBetween += calendarInPast.getActualMaximum(Calendar.MONTH);

                }

                //if year was selected by the spinner
            }else if(selectedAmountItem == 3){
                Calendar calendarInPast = Calendar.getInstance();
                calendarInPast.set(Calendar.DAY_OF_YEAR, currentCalendar.getActualMinimum(Calendar.DAY_OF_YEAR));

                daysInBetween = currentCalendar.get(Calendar.DAY_OF_YEAR);

                for(int i = 0; i < Integer.valueOf(selectedAmount)-1; i++){

                    calendarInPast.roll(Calendar.YEAR, -1);

                    daysInBetween += calendarInPast.getActualMaximum(Calendar.DAY_OF_YEAR);

                }

            }
        }

        long ms = (long) daysInBetween * (1000 * 60 * 60 * 24);


        //calculates the milliseconds from which on the activities will be counted
        long msSince1970 = currentCalendar.getTimeInMillis() - ms;


        int absoluteAmount = TrackedActivity.getAmountofDayGoneSince(activityName, msSince1970);

        amountInSelectedPeriod.setText(String.valueOf(absoluteAmount));

    }


}
