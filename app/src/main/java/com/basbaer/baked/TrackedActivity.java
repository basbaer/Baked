package com.basbaer.baked;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class TrackedActivity {

    private static SQLiteDatabase database;
    private static int idIndex;
    private static int activityIndex;
    private static int activityTypeIndex;
    private static int exactDateIndex;
    private static int dateDayIndex;
    private static int dateMonthIndex;
    private static int dateYearIndex;
    private static int colorIndex;
    private String activity;
    private String activityType;
    private Long exactDate;
    private int dateDay;
    private int dateMonth;
    private int dateYear;
    private String color;
    private static Context mcontext;

    protected static SharedPreferences sharedPreferences;


    public TrackedActivity(String activity, String activityType, Long date, String color, Context context) {


        if (database == null) {
            createDB(context);
        }

        Calendar givenDateCalendar = Calendar.getInstance();

        givenDateCalendar.setTime(new Date(date));

        this.activity = activity;
        this.activityType = activityType;
        this.exactDate = date;
        this.dateDay = givenDateCalendar.get(Calendar.DAY_OF_MONTH);
        this.dateMonth = givenDateCalendar.get(Calendar.MONTH);
        this.dateYear = givenDateCalendar.get(Calendar.YEAR);
        this.color = color;
        mcontext = context;

        this.insertInDb();



    }

    public void insertInDb(){

        String sql = "INSERT INTO activities ("
                + "activity, activityType, exactDate, dateDay, dateMonth, dateYear, color"
                + ") VALUES ('"
                + this.activity + "', '"
                + this.activityType + "', "
                + this.exactDate.intValue() + ", "
                + this.dateDay + ", "
                + this.dateMonth + ", "
                + this.dateYear + ", '"
                + color + "')";

        database.execSQL(sql);

    }

    public static void createDB(Context context) {

        try {

            database = context.openOrCreateDatabase("activitiesDB", MODE_PRIVATE, null);

            String createTableSqlCode = "CREATE TABLE IF NOT EXISTS activities ("
                    + "id INTEGER PRIMARY KEY, "
                    + "activity VARCHAR, "
                    + "activityType VARCHAR, "
                    + "exactDate INTEGER, "
                    + "dateDay INTEGER, "
                    + "dateMonth INTEGER, "
                    + "dateYear INTEGER, "
                    + "color VARCHAR)";

            database.execSQL(createTableSqlCode);

            Cursor c = database.rawQuery("SELECT * FROM activities", null);

            idIndex = c.getColumnIndex("id");
            activityIndex = c.getColumnIndex("activity");
            activityTypeIndex = c.getColumnIndex("activityType");
            exactDateIndex = c.getColumnIndex("exactDate");
            dateDayIndex = c.getColumnIndex("dateDay");
            dateMonthIndex = c.getColumnIndex("dateMonth");
            dateYearIndex = c.getColumnIndex("dateYear");
            colorIndex = c.getColumnIndex("color");


        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public String getActivityName(){

        return this.activity;
    }

    public String getActivityColor(){
        return this.color;
    }

    public static ArrayList<TrackedActivity> getActivities(Calendar calendar) {

        ArrayList<TrackedActivity> arrayListActivies = new ArrayList<>();

        String sql = "SELECT * FROM activities WHERE "
                + "dateDay = " + calendar.get(Calendar.DAY_OF_MONTH)
                + " AND "
                + "dateMonth = " + calendar.get(Calendar.MONTH)
                + " AND "
                + "dateYear = " + calendar.get(Calendar.YEAR);

        Cursor c = database.rawQuery(sql, null);

        boolean moreEntries = c.moveToFirst();

        while(moreEntries){

            //create a new TrackedActivity
            TrackedActivity i = new TrackedActivity(c.getString(activityIndex), c.getString(activityTypeIndex), c.getLong(exactDateIndex), c.getString(colorIndex), mcontext);

            i.printActiviy();

            arrayListActivies.add(i);

            moreEntries = c.moveToNext();

        }

        return arrayListActivies;



    }

    public void printActiviy(){

        String print = "Date: "
                + this.dateDay + "." + this.dateMonth + "." + this.dateYear
                + ", Activity: " + this.activity
                + ", ActivityType: " + this.activityType
                + ", Color: " + this.color;

        Log.i("currentActivity", print);

    }


    public void printDatabase() {


        String sql = "SELECT * FROM activities";

        Cursor c = database.rawQuery(sql, null);

        boolean moreEntries = c.moveToFirst();

        while (moreEntries) {

            String printString = "id: "
                    + c.getString(idIndex)
                    + "  activity: "
                    + c.getString(activityIndex)
                    + "  activityType: "
                    + c.getString(activityTypeIndex)
                    + "  date: "
                    + c.getInt(exactDateIndex)
                    + "  dateDay: "
                    + c.getInt(dateDayIndex)
                    + "  dateMonth: "
                    + c.getInt(dateMonthIndex)
                    + "  dateYear: "
                    + c.getInt(dateYearIndex)
                    + "  color: "
                    + c.getString(colorIndex);

            Log.i("DatabaseContent", printString);

            moreEntries = c.moveToNext();


        }


    }

    public void clearDatabase() {

        database.execSQL("DELETE FROM activities");


    }

    public static List<String> getDifferentActivties(){

        List<String> list = new ArrayList<>();
        Cursor c = database.rawQuery("SELECT * FROM activities", null);

        boolean moreEntries = c.moveToFirst();

        while(moreEntries){

            String nameOfActivity = c.getString(activityIndex);

            if(!list.contains(nameOfActivity)){

                list.add(nameOfActivity);


            }

            moreEntries = c.moveToNext();


        }

        //sort the list
        Collections.sort(list);

        int pos = -1;

        //checks if there is an last-selected activity and puts the the index of it in the array list in the sharedPreference
        //so it can be put as a starting selection
        for(int i = 0; i < list.size(); i++){
            if(list.get(i).equals(AddActivity.sharedPreferences.getString("activity", null))){
                pos = i;

            }
        }

        AddActivity.sharedPreferences.edit().putInt("positionOfPreviousSelectedActivity", pos).apply();

        return list;



    }

    public static List<String> getDifferentCategories(){

        List<String> list = new ArrayList<>();
        Cursor c = database.rawQuery("SELECT * FROM activities", null);

        boolean moreEntries = c.moveToFirst();

        while(moreEntries){

            String nameOfCategory = c.getString(activityTypeIndex);

            if(!list.contains(nameOfCategory)){

                list.add(nameOfCategory);


            }

            moreEntries = c.moveToNext();


        }

        //sort the list
        Collections.sort(list);

        String category = getCategory(AddActivity.sharedPreferences.getString("activity", null));


        int pos = -1;

        //checks if there is an last-selected activity and puts the the index of it in the array list in the sharedPreference
        //so it can be put as a starting selection
        for(int i = 0; i < list.size(); i++){
            if(list.get(i).equals(category)){
                pos = i;

            }
        }

        AddActivity.sharedPreferences.edit().putInt("positionOfPreviousSelectedCategory", pos).apply();

        return list;

    }

    public static String getCategory(String activity){

        String sql = "SELECT * FROM activities WHERE "
                + "activity = '"
                + activity
                + "' LIMIT 1";

        Cursor c = database.rawQuery(sql, null);

        if(c.moveToFirst()){

            return c.getString(activityTypeIndex);

        }else{
            return null;
        }



    }

    public static String getColor(String activity){

        String sql = "SELECT * FROM activities WHERE "
                + "activity = '"
                + activity
                + "' LIMIT 1";

        Cursor c = database.rawQuery(sql, null);

        if(c.moveToFirst()){

            return c.getString(colorIndex);

        }else{
            return null;
        }


    }
}
