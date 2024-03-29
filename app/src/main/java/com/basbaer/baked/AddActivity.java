package com.basbaer.baked;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.basbaer.baked.databinding.ActivityAddBinding;
import com.basbaer.baked.databinding.AlertDialogLayoutActivityBinding;
import com.basbaer.baked.databinding.AlertDialogLayoutCategoryBinding;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class AddActivity extends AppCompatActivity {

    //---------------------------------------------------------------------------------------
    //Strings
    private static final String PREVIOUSSELECTEDCATEGORY = "previousSelectedCategory";
    public static final String PREVIOUSSELCTEDACTIVITY = "previousSelectedActivity";


    //--------------------------------------------------------------------------------------------
    //binding
    ActivityAddBinding activityAddBinding;
    AlertDialogLayoutActivityBinding alertDialogLayoutActivityBinding;
    AlertDialogLayoutCategoryBinding alertDialogLayoutCategoryBinding;

    //----------------------------------------------------------------------------------------
    //features (spinners, button, etc.)
    Spinner activitySpinner;
    ArrayAdapter<String> activitySpinnerAdapter;
    Spinner categorySpinner;
    ArrayAdapter<String> adapterCategorySpinner;
    GridView colorPickerGridView;

    AlertDialog alertDialogActivity = null;
    EditText alertDialogEditTextActivity;

    AlertDialog alertDialogCategory = null;
    EditText alertDialogEditTextCategory;

    ColorPickerAdapter colorPickerAdapter;
    TextView dateEditText;

    DatePickerDialog.OnDateSetListener onDateSetListener;


    //--------------------------------------------------------------------------------------
    //variables

    //saves which activiy and category was previously selected
    public static SharedPreferences sharedPreferences;


    protected static String activity_name;
    protected static String category_name;
    protected static String color;
    protected static String previousSelectedColor;
    protected static long dateLong;


    //list for the drop down Spinner
    List<String> activtiesList;
    List<String> categoryNamesList;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityAddBinding = ActivityAddBinding.inflate(getLayoutInflater());
        View view = activityAddBinding.getRoot();
        setContentView(view);

        //initialize db
        TrackedActivity.createDB(this);
        //----------------------------------------------------------------------------------
        //setting up features
        alertDialogLayoutActivityBinding = AlertDialogLayoutActivityBinding.inflate(getLayoutInflater());
        alertDialogLayoutCategoryBinding = AlertDialogLayoutCategoryBinding.inflate(getLayoutInflater());
        //EditText, where the users types what is requested (e.g. a group name)
        alertDialogEditTextActivity = alertDialogLayoutActivityBinding.activityName;
        alertDialogEditTextCategory = alertDialogLayoutCategoryBinding.categoryName;
        colorPickerGridView = activityAddBinding.colorGridView;
        dateEditText = activityAddBinding.editTextDate;

        onDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                Calendar calendar = Calendar.getInstance();
                calendar.set(year, month, dayOfMonth);

                dateLong = calendar.getTimeInMillis();

                Date displaydDate = calendar.getTime();



                String date = DateFormat.getDateInstance().format(displaydDate);

                dateEditText.setText(date);

            }
        };

        Objects.requireNonNull(getSupportActionBar()).setTitle("Add your Activity");

        //--------------------------------------------------------------------------------


        Intent inputIntent = getIntent();

        //setting the date
        dateLong = inputIntent.getLongExtra("date", -1L);
        String dateString = DateFormat.getDateInstance().format(new Date(dateLong));
        dateEditText.setText(dateString);

        //-------------------------------------------------------------------------------------
        //Setting up Category-Spinner

        categoryNamesList = mCategories.getCategoryNamesList();
        activtiesList = new ArrayList<>();


        categorySpinner = activityAddBinding.categorySpinner;
        adapterCategorySpinner = new ArrayAdapter<>(this, R.layout.spinner_activity_layout, R.id.spinnerAdapterTextView, categoryNamesList);
        adapterCategorySpinner.setDropDownViewResource(R.layout.spinner_activity_layout);
        categorySpinner.setAdapter(adapterCategorySpinner);



        //setting up the previousSelectedCategory
        String lastSelectedCategory = sharedPreferences.getString(PREVIOUSSELECTEDCATEGORY, "");

        int positionOfLastSelectedCategory = -1;

        for(int i = 0; i < categoryNamesList.size(); i++){

            if(categoryNamesList.get(i).equals(lastSelectedCategory)){
                positionOfLastSelectedCategory = i;
                break;
            }
        }

        if (positionOfLastSelectedCategory != -1) {

            categorySpinner.setSelection(positionOfLastSelectedCategory);

            category_name = lastSelectedCategory;


            //only shows activities of this category in the activity Spinner
            List<String> activities = TrackedActivity.getActivitiesOfCategory(category_name);

            activtiesList.clear();

            activtiesList.addAll(activities);



        }


        //--------------------------------------------------------------------------------------
        //Setting up Activity-Spinner

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
        //setting up the previousSelectedCategory
        String lastSelectedActivity = sharedPreferences.getString(PREVIOUSSELCTEDACTIVITY, "");

        int positionOfLastSelectedActivity = -1;

        for(int i = 0; i < activtiesList.size(); i++){

            if(activtiesList.get(i).equals(lastSelectedActivity)){
                positionOfLastSelectedActivity = i;
                break;
            }
        }

        if (positionOfLastSelectedActivity != -1) {

            activitySpinner.setSelection(positionOfLastSelectedActivity);

            activity_name = lastSelectedActivity;


            //selects the color automatically if a activity was selected, that already existed
            previousSelectedColor = TrackedActivity.getColor(activity_name);

            //since the user does not select a color, the previous selected color is set automatically
            color = previousSelectedColor;


            //selects the color automatically if a activity was selected, that already existed
            if(colorPickerAdapter == null){

                colorPickerAdapter = new ColorPickerAdapter(getApplicationContext(), previousSelectedColor);

                colorPickerGridView.setAdapter(colorPickerAdapter);

            }else{
                colorPickerAdapter.selectColor(color);
            }





        }





        categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                category_name = parent.getItemAtPosition(position).toString();


                //only shows activities of this category in the activity Spinner
                List<String> activities = TrackedActivity.getActivitiesOfCategory(category_name);
                activtiesList.clear();

                activtiesList.addAll(activities);

                if(!activtiesList.isEmpty()) {

                    activity_name = activtiesList.get(0);

                    color = TrackedActivity.getColor(activtiesList.get(0));

                    colorPickerAdapter.selectColor(color);

                }



                activitySpinnerAdapter.notifyDataSetChanged();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });





        activitySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                activity_name = parent.getItemAtPosition(position).toString();



                previousSelectedColor = TrackedActivity.getColor(activity_name);

                //since the user does not select a color, the previous selected color is set automatically
                color = previousSelectedColor;



                //selects the color automatically if a activity was selected, that already existed
                if(colorPickerAdapter == null){

                    colorPickerAdapter = new ColorPickerAdapter(getApplicationContext(), previousSelectedColor);

                    colorPickerGridView.setAdapter(colorPickerAdapter);



                }else{

                    colorPickerAdapter.selectColor(color);
                }





            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

                Toast.makeText(getApplicationContext(), "Please select or add an Activity", Toast.LENGTH_SHORT).show();

            }
        });


        if(colorPickerAdapter == null){

            colorPickerAdapter = new ColorPickerAdapter(getApplicationContext());

            colorPickerGridView.setAdapter(colorPickerAdapter);

        }


        //-------------------------------------------------------------------------------
        //Buttons

        ImageButton changeDateButton = activityAddBinding.dateButton;

        changeDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Calendar c = Calendar.getInstance();
                c.setTime(new Date(dateLong));

                DatePickerDialog datePickerDialog = new DatePickerDialog(AddActivity.this, android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        onDateSetListener, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));

                datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.GRAY));

                datePickerDialog.show();

            }
        });


        ImageButton addCategoryButton = activityAddBinding.addCategoryImageButton;

        addCategoryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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
        });

        ImageButton addActivityButton = activityAddBinding.addActivityImageButton;

        addActivityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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
        });

        Button addActivitButton = activityAddBinding.addActivityButton;

        addActivitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                long date = dateLong;
                String category = getCategory();


                if (date != -1L && activity_name != null && color != null) {

                    TrackedActivity i = new TrackedActivity(activity_name, category, date, color);

                    i.insertInDb();


                    //add category to category list
                    mCategories.allCategories.add(new mCategories(-1, category, true));

                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);

                    intent.putExtra("date", date);

                    sharedPreferences.edit().putString(PREVIOUSSELECTEDCATEGORY, category_name).apply();

                    sharedPreferences.edit().putString(PREVIOUSSELCTEDACTIVITY, activity_name).apply();

                    startActivity(intent);

                }

            }
        });



    }




    /***
     * Gets the category_name which is currently set
     * @return name of the category or "unknown" if the EditText is empty
     */
    private String getCategory() {


        if (category_name != null && !category_name.isEmpty()) {

            return category_name;

        } else {

            return "unknown";

        }

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


        if(!categoryNamesList.contains(alertDialogEditTextCategory.getText().toString())) {

            category_name = alertDialogEditTextCategory.getText().toString();

            categoryNamesList.add(category_name);

            categorySpinner.setSelection(categoryNamesList.size() - 1);


        }else{

            categorySpinner.setSelection(categoryNamesList.indexOf(alertDialogEditTextCategory.getText().toString()));

        }

        adapterCategorySpinner.notifyDataSetChanged();



        alertDialogCategory.cancel();

    }




}

