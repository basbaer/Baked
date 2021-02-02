package com.basbaer.baked;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Spinner;
import android.widget.Toast;

import com.basbaer.baked.databinding.ActivityAddBinding;
import com.basbaer.baked.databinding.AlertDialogLayoutActivityBinding;
import com.basbaer.baked.databinding.AlertDialogLayoutCategoryBinding;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class AddActivity extends AppCompatActivity {

    ActivityAddBinding activityAddBinding;
    AlertDialogLayoutActivityBinding alertDialogLayoutActivityBinding;
    AlertDialogLayoutCategoryBinding alertDialogLayoutCategoryBinding;

    //saves which activiy and category was previously selected
    public static SharedPreferences sharedPreferences;

    protected static EditText dateEditText;
    protected static String activity;
    protected static String category;
    protected static String color;
    protected static String previousSelectedColor;
    protected static ColorPickerAdapter colorPickerAdapter;
    Spinner activitySpinner;
    ArrayAdapter activitySpinnerAdapter;
    Spinner categorySpinner;
    ArrayAdapter adapterCategorySpinner;
    GridView colorPickerGridView;


    //list for the drop down Spinner
    List<String> activtiesList;
    List<mCategories> categoryList;


    AlertDialog alertDialogActivity = null;
    EditText alertDialogEditTextActivity;

    AlertDialog alertDialogCategory = null;
    EditText alertDialogEditTextCategory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityAddBinding = ActivityAddBinding.inflate(getLayoutInflater());
        View view = activityAddBinding.getRoot();
        setContentView(view);

        alertDialogLayoutActivityBinding = AlertDialogLayoutActivityBinding.inflate(getLayoutInflater());
        alertDialogLayoutCategoryBinding = AlertDialogLayoutCategoryBinding.inflate(getLayoutInflater());
        //EditText, where the users types what is requested (e.g. a group name)
        alertDialogEditTextActivity = alertDialogLayoutActivityBinding.activityName;
        alertDialogEditTextCategory = alertDialogLayoutCategoryBinding.categoryName;

        getSupportActionBar().setTitle("Add your Activity");

        Intent inputIntent = getIntent();

        long dateLong = inputIntent.getLongExtra("date", -1L);

        colorPickerGridView = activityAddBinding.colorGridView;
        dateEditText = activityAddBinding.editTextDate;



        String dateString = new SimpleDateFormat("dd.MM.yyyy").format(new Date(dateLong));

        dateEditText.setText(dateString);

        //-------------------------------------------------------------------------------------
        //Setting up Category-Spinner

        categoryList = new ArrayList<>();

        categoryList.addAll(mCategories.allCategories);


        categorySpinner = activityAddBinding.categorySpinner;
        adapterCategorySpinner = new ArrayAdapter<>(this, R.layout.spinner_activity_layout, R.id.spinnerAdapterTextView, categoryList);
        adapterCategorySpinner.setDropDownViewResource(R.layout.spinner_activity_layout);
        categorySpinner.setAdapter(adapterCategorySpinner);

        int positionOfLastSelectedCategory = sharedPreferences.getInt("positionOfPreviousSelectedCategory", -1);
        if (positionOfLastSelectedCategory != -1) {

            categorySpinner.setSelection(positionOfLastSelectedCategory);

        }





        //get all Activities
        //activtiesList = TrackedActivity.getDifferentActivties();
        activtiesList = new ArrayList<>();


        //set the spinner for the Activity
        activitySpinner = activityAddBinding.activitySpinner;

        // Create an ArrayAdapter using the string array and a default spinner layout
        activitySpinnerAdapter = new ArrayAdapter<>(this, R.layout.spinner_activity_layout, R.id.spinnerAdapterTextView, activtiesList);

        // Specify the layout to use when the list of choices appears
        //adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        activitySpinnerAdapter.setDropDownViewResource(R.layout.spinner_activity_layout);

        // Apply the adapter to the spinner
        activitySpinner.setAdapter(activitySpinnerAdapter);

        //setting the previous selection
        int positionOfLastSelectedActivity = sharedPreferences.getInt("positionOfPreviousSelectedActivity", -1);

        if (positionOfLastSelectedActivity != -1) {

            activitySpinner.setSelection(positionOfLastSelectedActivity);

        }





        activitySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                activity = parent.getItemAtPosition(position).toString();


                //selects the color automatically if a activity was selected, that already existed
                previousSelectedColor = TrackedActivity.getColor(activity);

                colorPickerAdapter = new ColorPickerAdapter(getApplicationContext(), previousSelectedColor);

                colorPickerGridView.setAdapter(colorPickerAdapter);

                //since the user does not select a color, the previous selected color is set automatically
                color = previousSelectedColor;


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

                Toast.makeText(getApplicationContext(), "Please select or add an Activity", Toast.LENGTH_SHORT).show();

            }
        });


        categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                category = parent.getItemAtPosition(position).toString();

                //only shows activities of this category in the activity Spinner
                List<String> activities = TrackedActivity.getActivitiesOfCategory(category);
                activtiesList.clear();

                activtiesList.addAll(activities);

                activitySpinnerAdapter.notifyDataSetChanged();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        colorPickerAdapter = new ColorPickerAdapter(getApplicationContext());

        colorPickerGridView.setAdapter(colorPickerAdapter);


    }





    public void addActivity(View view) {

        long date = getDate();
        String category = getCategory();


        if (date != -1L && activity != null && color != null) {

            TrackedActivity i = new TrackedActivity(activity, category, date, color, getApplicationContext());



            TrackedActivity.printDatabase();

            //add category to category list
            mCategories.allCategories.add(new mCategories(-1, category, true));

            Intent intent = new Intent(this, MainActivity.class);

            intent.putExtra("date", date);

            sharedPreferences.edit().putString("activity", activity).apply();

            startActivity(intent);

        }

    }


    private long getDate() {
        //converting the date


        String date = dateEditText.getText().toString();

        if (date.length() == 10) {

            int day = Integer.parseInt(date.substring(0, 2));
            int month = Integer.parseInt(date.substring(3, 5));
            int year = Integer.parseInt(date.substring(6, 10));


            Calendar dateCalendar = Calendar.getInstance();

            dateCalendar.set(year, month - 1, day);

            Log.i("Date", String.valueOf(dateCalendar.getTime().getTime()));

            return dateCalendar.getTime().getTime();

        } else {
            Toast.makeText(this, "Please add the date in format dd.mm.yyyy", Toast.LENGTH_SHORT).show();


            return -1L;
        }


    }

    protected static void changeDate(Calendar calendar) {

        Date displaydate = calendar.getTime();

        String date = new SimpleDateFormat("dd.MM.yyyy").format(displaydate);

        dateEditText.setText(date);

    }


    private String getCategory() {


        if (category != null && !category.isEmpty()) {

            return category;

        } else {

            return "unknown";

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

    public void onImageButtonDateClicked(View view) {

        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getSupportFragmentManager(), "datePicker");


    }

    public void onImageButtonActivityClicked(View view) {

        //creating the AlertDialog
        AlertDialog.Builder builder = new AlertDialog.Builder(AddActivity.this);

        LayoutInflater layoutInflater = getLayoutInflater();

        //get's the created layout
        View alertdialogView = layoutInflater.inflate(R.layout.alert_dialog_layout_activity, null);

        alertDialogEditTextActivity = alertdialogView.findViewById(R.id.activity_name);

        alertDialogEditTextActivity.setTag(0);

        //defines if the builder can be canceled
        builder.setCancelable(true);

        //sets the created Layout for the builder
        builder.setView(alertdialogView);

        alertDialogActivity = builder.create();


        alertDialogActivity.show();


    }

    public void onImageButtonCategoryClicked(View view) {

        //creating the AlertDialog
        AlertDialog.Builder builder = new AlertDialog.Builder(AddActivity.this);

        LayoutInflater layoutInflater = getLayoutInflater();

        //get's the created layout
        View alertdialogView = layoutInflater.inflate(R.layout.alert_dialog_layout_category, null);

        alertDialogEditTextCategory = alertdialogView.findViewById(R.id.category_name);


        //defines if the builder can be canceled
        builder.setCancelable(true);

        //sets the created Layout for the builder
        builder.setView(alertdialogView);

        alertDialogCategory = builder.create();


        alertDialogCategory.show();


    }

    public void alertDialogButtonClicked(View view) {


        if(!activtiesList.contains(alertDialogEditTextActivity.getText().toString())) {
            activtiesList.add(alertDialogEditTextActivity.getText().toString());
            activitySpinner.setSelection(activtiesList.size() - 1);
        }else{

            activitySpinner.setSelection(activtiesList.indexOf(alertDialogEditTextActivity.getText().toString()));

        }

        activitySpinnerAdapter.notifyDataSetChanged();



        alertDialogActivity.cancel();

    }

    public void alertDialogCategoryButtonClicked(View view) {
        if(!categoryList.contains(alertDialogEditTextCategory.getText().toString())) {

            categoryList.add(alertDialogEditTextCategory.getText().toString());

            categorySpinner.setSelection(categoryList.size() - 1);
        }else{

            categorySpinner.setSelection(categoryList.indexOf(alertDialogEditTextCategory.getText().toString()));

        }

        adapterCategorySpinner.notifyDataSetChanged();



        alertDialogCategory.cancel();

    }


    private int getPositionOfCategory(String activity) {

        String category = TrackedActivity.getCategory(activity);


        int pos = -1;

        //checks if there is an last-selected activity and puts the the index of it in the array list in the sharedPreference
        //so it can be put as a starting selection
        for (int i = 0; i < categoryList.size(); i++) {
            if (categoryList.get(i).equals(category)) {
                pos = i;

            }
        }

        return pos;

    }


}

