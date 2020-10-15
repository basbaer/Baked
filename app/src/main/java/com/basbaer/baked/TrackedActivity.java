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
    private static int categoryIndex;
    private static int exactDateIndex;
    private static int dateDayIndex;
    private static int dateMonthIndex;
    private static int dateYearIndex;
    private static int colorIndex;
    private String activity;
    private String category;
    private long exactDate;
    private int dateDay;
    private int dateMonth;
    private int dateYear;
    private String color;
    private static Context mcontext;

    protected static SharedPreferences sharedPreferences;


    public TrackedActivity(String activity, String category, long date, String color, Context context) {


        if (database == null) {
            createDB(context);
        }

        Log.i("TrackedActivity", "long value: " + date);

        Calendar givenDateCalendar = Calendar.getInstance();

        givenDateCalendar.setTime(new Date(date));

        this.activity = activity;
        this.category = category;
        this.exactDate = date;
        this.dateDay = givenDateCalendar.get(Calendar.DAY_OF_MONTH);
        this.dateMonth = givenDateCalendar.get(Calendar.MONTH);
        this.dateYear = givenDateCalendar.get(Calendar.YEAR);
        this.color = color;
        mcontext = context;





    }

    public void insertInDb(){

        String sql = "INSERT INTO activities ("
                + "activity, category, exactDate, dateDay, dateMonth, dateYear, color"
                + ") VALUES ('"
                + this.activity + "', '"
                + this.category + "', "
                + this.exactDate + ", "
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
                    + "category VARCHAR, "
                    + "exactDate INTEGER, "
                    + "dateDay INTEGER, "
                    + "dateMonth INTEGER, "
                    + "dateYear INTEGER, "
                    + "color VARCHAR)";

            database.execSQL(createTableSqlCode);

            Cursor c = database.rawQuery("SELECT * FROM activities", null);

            idIndex = c.getColumnIndex("id");
            activityIndex = c.getColumnIndex("activity");
            categoryIndex = c.getColumnIndex("category");
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
            TrackedActivity i = new TrackedActivity(c.getString(activityIndex), c.getString(categoryIndex), c.getLong(exactDateIndex), c.getString(colorIndex), mcontext);

            arrayListActivies.add(i);

            moreEntries = c.moveToNext();

        }

        return arrayListActivies;



    }


    public static void printDatabase() {


        String sql = "SELECT * FROM activities";

        Cursor c = database.rawQuery(sql, null);

        boolean moreEntries = c.moveToFirst();

        while (moreEntries) {

            String printString = "id: "
                    + c.getString(idIndex)
                    + "  activity: "
                    + c.getString(activityIndex)
                    + "  activityType: "
                    + c.getString(categoryIndex)
                    + "  date: "
                    + c.getLong(exactDateIndex)
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



    public static void clearDatabase() {

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

            String nameOfCategory = c.getString(categoryIndex);

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

            return c.getString(categoryIndex);

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

    public static ArrayList<String> getActivitiesOfCategory(String category){

        String sql = "SELECT * FROM activities WHERE "
                + "category = '"
                + category
                + "'";

        Cursor c = database.rawQuery(sql, null);

        ArrayList<String> activitiesAL = new ArrayList<>();

        boolean moreEntries = c.moveToFirst();

        if(moreEntries){

            activitiesAL.add(c.getString(activityIndex));

            c.moveToNext();

        }

        return activitiesAL;


    }

    public static int getTotalAmountActivityWasDone(String activity){

        String sql = "SELECT * FROM activities WHERE "
                + "activity = '"
                + activity
                + "'";

        Cursor c = database.rawQuery(sql, null);

        return c.getCount();

    }

    public static Date dateActivityWasLastDone(String activity){

        //gets all entries of this activity out of the database
        String sql = "SELECT * FROM activities WHERE "
                + "activity = '"
                + activity
                + "'";

        Cursor c = database.rawQuery(sql, null);

        boolean moreEntries = c.moveToFirst();

        long latestActivity = 0;
        Date currentDate = new Date();
        long currentDateLong = currentDate.getTime();

        while (moreEntries){
            //sets the one with highest exactDate value (which is a long variable and represents the ms gone since 1970) as latest activity
            //but if there is an already an activity in the future, it does not take it into account
            if(c.getLong(exactDateIndex) > latestActivity && c.getLong(exactDateIndex) < currentDateLong){

                latestActivity = c.getLong(exactDateIndex);

            }

            moreEntries = c.moveToNext();

        }

        if(latestActivity == 0){
            return null;
        }else{
            return new Date(latestActivity);
        }


    }


    public static int getTimesDoneThisMonth(String activity){

        //gets all entries of this activity out of the database
        String sql = "SELECT * FROM activities WHERE "
                + "activity = '"
                + activity
                + "'";

        Cursor c = database.rawQuery(sql, null);

        boolean moreEntries = c.moveToFirst();
        Date currentDate = new Date();
        long currentDateLong = currentDate.getTime();

        Calendar firstOfMonth = Calendar.getInstance();
        //setting the calendar to the very first second of the month
        firstOfMonth.set(firstOfMonth.get(Calendar.YEAR),
                firstOfMonth.get(Calendar.MONTH),
                firstOfMonth.getActualMinimum(Calendar.DAY_OF_MONTH),
                firstOfMonth.getActualMinimum(Calendar.HOUR_OF_DAY),
                firstOfMonth.getActualMinimum(Calendar.MINUTE),
                firstOfMonth.getActualMinimum(Calendar.SECOND));

        long firstOfMonthDate = firstOfMonth.getTime().getTime();

        int timesDoneThisMonth = 0;

        while(moreEntries){

            if(c.getLong(exactDateIndex) < currentDateLong && c.getLong(exactDateIndex) > firstOfMonthDate){

                timesDoneThisMonth++;

            }

            moreEntries = c.moveToNext();

        }

        return timesDoneThisMonth;

    }

    public static int getTimesDoneThisYear(String activity){

        //gets all entries of this activity out of the database
        String sql = "SELECT * FROM activities WHERE "
                + "activity = '"
                + activity
                + "'";

        Cursor c = database.rawQuery(sql, null);

        boolean moreEntries = c.moveToFirst();
        Date currentDate = new Date();
        long currentDateLong = currentDate.getTime();

        Calendar firstOfYearCalendar = Calendar.getInstance();
        //setting the calendar to the very first second of the year
        firstOfYearCalendar.set(firstOfYearCalendar.get(Calendar.YEAR),
                firstOfYearCalendar.getActualMinimum(Calendar.YEAR),
                firstOfYearCalendar.getActualMinimum(Calendar.DAY_OF_MONTH),
                firstOfYearCalendar.getActualMinimum(Calendar.HOUR_OF_DAY),
                firstOfYearCalendar.getActualMinimum(Calendar.MINUTE),
                firstOfYearCalendar.getActualMinimum(Calendar.SECOND));

        long firstOfYearLong = firstOfYearCalendar.getTime().getTime();

        int timesDoneThisMonth = 0;

        while(moreEntries){

            if(c.getLong(exactDateIndex) < currentDateLong && c.getLong(exactDateIndex) > firstOfYearLong){

                timesDoneThisMonth++;

            }

            moreEntries = c.moveToNext();

        }

        return timesDoneThisMonth;

    }

    public static int getAmountofDayGoneSince(String activity, long date){

        //gets all entries of this activity out of the database
        String sql = "SELECT * FROM activities WHERE "
                + "activity = '"
                + activity
                + "'";

        Cursor c = database.rawQuery(sql, null);

        boolean moreEntries = c.moveToFirst();
        Date currentDate = new Date();
        long currentDateLong = currentDate.getTime();

        int amountInPeriod = 0;

        while(moreEntries){

            long dateOfCurrentActivity = c.getLong(exactDateIndex);

            if(dateOfCurrentActivity < currentDateLong && dateOfCurrentActivity > date){

                amountInPeriod++;
            }

            moreEntries = c.moveToNext();

        }

        return amountInPeriod;

    }
}


