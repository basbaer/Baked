package com.basbaer.baked;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class TrackedActivity{

    private static SQLiteDatabase database;
    private static final String ACTIVITIES_DB = "activities";

    //--------------------------------------------------------------------------------------
    //Indices of db
    private static int idIndex;
    private static int activityIndex;
    private static int categoryIndex;
    private static int isCheckedIndex;
    private static int exactDateIndex;
    private static int dateDayIndex;
    private static int dateMonthIndex;
    private static int dateYearIndex;
    private static int colorIndex;

    //-----------------------------------------------------------------------------------
    //Coloum names
    private static final String ACTIVITY = "activity";
    private static final String CATEGORY = "category";
    private static final String ISCHECKED = "isChecked";
    private static final String EXACTDATE = "exactDate";
    private static final String DATEDAY = "dateDay";
    private static final String DATEMONTH = "dateMonth";
    private static final String DATEYEAR = "dateYear";
    private static final String COLOR = "color";


    //-----------------------------------------------------------------------------------
    private long id;
    private String activity;
    private String category;
    //1 for true, 0 for false
    private int isChecked;
    private long exactDate;
    private int dateDay;
    private int dateMonth;
    private int dateYear;
    private String color;
    private static Context mcontext;

    public static HashMap<Long, View> currentMonthHashMap;



    public TrackedActivity(String activity, String category, long date, String color, Context context) {


        if (database == null) {
            createDB(context);
        }

        Log.i("TrackedActivity", "long value: " + date);

        Calendar givenDateCalendar = Calendar.getInstance();

        givenDateCalendar.setTime(new Date(date));

        this.activity = activity;
        this.category = category;
        this.isChecked = 1;
        this.exactDate = date;
        this.dateDay = givenDateCalendar.get(Calendar.DAY_OF_MONTH);
        this.dateMonth = givenDateCalendar.get(Calendar.MONTH);
        this.dateYear = givenDateCalendar.get(Calendar.YEAR);
        this.color = color;
        this.id = -1;
        mcontext = context;


    }

    public void insertInDb() {

        ContentValues values = new ContentValues();

        values.put("activity", this.activity);
        values.put("category", this.category);
        values.put("isChecked", this.isChecked);
        values.put("exactDate", this.exactDate);
        values.put("dateDay", this.dateDay);
        values.put("dateMonth", this.dateMonth);
        values.put("dateYear", this.dateYear);
        values.put("color", this.color);

        this.id = database.insert("activities", null, values);


    }

    /**
     * create / opens the db
     * @param context: context of activity
     */
    public static void createDB(Context context) {

        try {

            database = context.openOrCreateDatabase("activitiesDB", MODE_PRIVATE, null);

            String createTableSqlCode = "CREATE TABLE IF NOT EXISTS activities ("
                    + "id INTEGER PRIMARY KEY, "
                    + ACTIVITY + " VARCHAR, "
                    + CATEGORY + " VARCHAR, "
                    + ISCHECKED + " INTEGER, "
                    + EXACTDATE + " INTEGER, "
                    + DATEDAY + " INTEGER, "
                    + DATEMONTH + " INTEGER, "
                    + DATEYEAR + " INTEGER, "
                    + COLOR + " VARCHAR)";

            database.execSQL(createTableSqlCode);

            Cursor c = database.rawQuery("SELECT * FROM activities", null);

            idIndex = c.getColumnIndex("id");
            activityIndex = c.getColumnIndex(ACTIVITY);
            categoryIndex = c.getColumnIndex(CATEGORY);
            isCheckedIndex = c.getColumnIndex(ISCHECKED);
            exactDateIndex = c.getColumnIndex(EXACTDATE);
            dateDayIndex = c.getColumnIndex(DATEDAY);
            dateMonthIndex = c.getColumnIndex(DATEMONTH);
            dateYearIndex = c.getColumnIndex(DATEYEAR);
            colorIndex = c.getColumnIndex(COLOR);


        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    //-----------------------------------------------------------------------------------
    //getter


    public String getActivityName() {

        return this.activity;
    }

    public String getActivityColor() {
        return this.color;
    }

    public Calendar getCalendarDate() {

        Calendar c = Calendar.getInstance();

        c.setTime(new Date(this.exactDate));

        return c;

    }

    public long getId() {

        if (this.id == -1) {

            String sql = "SELECT * FROM activities WHERE "
                    + "exactDate = "
                    + this.exactDate;

            Cursor c = database.rawQuery(sql, null);

            if (c.moveToFirst()) {

                this.id = c.getLong(idIndex);

            }


        }

        return this.id;

    }

    public static ArrayList<TrackedActivity> getActivitiesOfTheDay(Calendar calendar) {

        ArrayList<TrackedActivity> arrayListActivies = new ArrayList<>();

        String sql = "SELECT * FROM activities WHERE "
                + DATEDAY + " = " + calendar.get(Calendar.DAY_OF_MONTH)
                + " AND "
                + DATEMONTH + " = " + calendar.get(Calendar.MONTH)
                + " AND "
                + DATEYEAR + " = " + calendar.get(Calendar.YEAR)
                + " AND "
                + ISCHECKED + " = 1";

        Cursor c = database.rawQuery(sql, null);

        boolean moreEntries = c.moveToFirst();

        while (moreEntries) {

            //create a new TrackedActivity
            TrackedActivity i = new TrackedActivity(c.getString(activityIndex), c.getString(categoryIndex), c.getLong(exactDateIndex), c.getString(colorIndex), mcontext);

            arrayListActivies.add(i);

            moreEntries = c.moveToNext();

        }

        c.close();

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

    public static void deleteEntry(TrackedActivity trackedActivity) {

        String sql = "DELETE FROM activities WHERE "
                + "id = "
                + trackedActivity.getId();

        database.execSQL(sql);

    }

    public static List<String> getAllActivties() {

        List<String> list = new ArrayList<>();
        Cursor c = database.rawQuery("SELECT * FROM activities", null);

        boolean moreEntries = c.moveToFirst();

        while (moreEntries) {

            String nameOfActivity = c.getString(activityIndex);

            if (!list.contains(nameOfActivity)) {

                list.add(nameOfActivity);


            }

            moreEntries = c.moveToNext();


        }

        //sort the list
        Collections.sort(list);

        int pos = -1;

        //checks if there is an last-selected activity and puts the the index of it in the array list in the sharedPreference
        //so it can be put as a starting selection
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).equals(AddActivity.sharedPreferences.getString("activity", null))) {
                pos = i;

            }
        }

        AddActivity.sharedPreferences.edit().putInt("positionOfPreviousSelectedActivity", pos).apply();

        return list;


    }

    public static ArrayList<mCategories> getDifferentCategories() {

        ArrayList<mCategories> list = new ArrayList<>();
        Cursor c = database.rawQuery("SELECT * FROM activities GROUP BY category", null);


        boolean moreEntries = c.moveToFirst();

        while (moreEntries) {

            String nameOfCategory = c.getString(categoryIndex);



            boolean isInList = false;

            for(int i = 0; i < list.size(); i++){

                if(list.get(i).getName().equals(nameOfCategory)){
                    isInList = true;
                }

            }


            if (!isInList) {

                //converts the saved integer in boolean
                boolean isChecked;
                int isCheckedInt = c.getInt(isCheckedIndex);

                if(isCheckedInt == 0){
                    isChecked = false;
                }else{
                    isChecked = true;
                }

                list.add(new mCategories(nameOfCategory, isChecked));


            }

            moreEntries = c.moveToNext();


        }

        //sort the list alphabetically
        Collections.sort(list, new Comparator<mCategories>() {
            @Override
            public int compare(mCategories o1, mCategories o2) {
                return o1.getName().compareTo(o2.getName());
            }
        });


        String lastSelectedActivity = getCategory(AddActivity.sharedPreferences.getString("activity", null));


        int pos = -1;

        //checks if there is an last-selected activity and puts the the index of it in the array list in the sharedPreference
        //so it can be put as a starting selection
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).equals(lastSelectedActivity)) {
                pos = i;

            }
        }

        AddActivity.sharedPreferences.edit().putInt("positionOfPreviousSelectedCategory", pos).apply();

        return list;

    }

    public static void updateIsChecked(String category, boolean isChecked){

        int isCheckedInt;
        if(isChecked){
            isCheckedInt = 1;
        }else{
            isCheckedInt = 0;
        }

        String sql = "UPDATE activities SET "
                + "isChecked = "
                + isCheckedInt
                + " WHERE "
                + "category = '"
                + category
                + "'";

        database.execSQL(sql);


    }

    public static void setIsCheckedForAll(boolean isChecked){

        int isChecked_int = 0;

        if(isChecked){
            isChecked_int = 1;
        }

        database.execSQL("UPDATE " + ACTIVITIES_DB
                + " SET " + ISCHECKED
                + " = " + String.valueOf(isChecked_int));



    }


    public static String getCategory(String activity) {

        String sql = "SELECT * FROM activities WHERE "
                + "activity = '"
                + activity
                + "' LIMIT 1";

        Cursor c = database.rawQuery(sql, null);

        if (c.moveToFirst()) {

            return c.getString(categoryIndex);

        } else {
            return null;
        }


    }

    public static String getColor(String activity) {

        String sql = "SELECT * FROM activities WHERE "
                + "activity = '"
                + activity
                + "' LIMIT 1";

        Cursor c = database.rawQuery(sql, null);

        if (c.moveToFirst()) {

            return c.getString(colorIndex);

        } else {
            return null;
        }


    }

    public static ArrayList<String> getActivitiesOfCategory(String category) {

        String sql = "SELECT * FROM activities WHERE "
                + "category = '"
                + category
                + "'";

        Cursor c = database.rawQuery(sql, null);

        ArrayList<String> activitiesAL = new ArrayList<>();

        boolean moreEntries = c.moveToFirst();

        while (moreEntries) {

            if (!activitiesAL.contains(c.getString(activityIndex))) {



                activitiesAL.add(c.getString(activityIndex));

            }

            moreEntries = c.moveToNext();

        }

        return activitiesAL;


    }

    public static int getTotalAmountActivityWasDone(String activity) {

        String sql = "SELECT * FROM activities WHERE "
                + "activity = '"
                + activity
                + "'";

        Cursor c = database.rawQuery(sql, null);

        return c.getCount();

    }

    public static Date dateActivityWasLastDone(String activity) {

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

        while (moreEntries) {
            //sets the one with highest exactDate value (which is a long variable and represents the ms gone since 1970) as latest activity
            //but if there is an already an activity in the future, it does not take it into account
            if (c.getLong(exactDateIndex) > latestActivity && c.getLong(exactDateIndex) < currentDateLong) {

                latestActivity = c.getLong(exactDateIndex);

            }

            moreEntries = c.moveToNext();

        }

        if (latestActivity == 0) {
            return null;
        } else {
            return new Date(latestActivity);
        }


    }


    public static int getTimesDoneThisMonth(String activity) {

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

        while (moreEntries) {

            if (c.getLong(exactDateIndex) < currentDateLong && c.getLong(exactDateIndex) > firstOfMonthDate) {

                timesDoneThisMonth++;

            }

            moreEntries = c.moveToNext();

        }

        return timesDoneThisMonth;

    }

    public static int getTimesDoneThisYear(String activity) {

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
                firstOfYearCalendar.getActualMinimum(Calendar.MONTH),
                firstOfYearCalendar.getActualMinimum(Calendar.DAY_OF_MONTH),
                firstOfYearCalendar.getActualMinimum(Calendar.HOUR_OF_DAY),
                firstOfYearCalendar.getActualMinimum(Calendar.MINUTE),
                firstOfYearCalendar.getActualMinimum(Calendar.SECOND));

        long firstOfYearLong = firstOfYearCalendar.getTime().getTime();

        int timesDoneThisYear = 0;

        while (moreEntries) {

            if (c.getLong(exactDateIndex) < currentDateLong && c.getLong(exactDateIndex) > firstOfYearLong) {

                timesDoneThisYear++;

            }

            moreEntries = c.moveToNext();

        }

        return timesDoneThisYear;

    }

    public static int getAmountofDayGoneSince(String activity, long date) {

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

        while (moreEntries) {

            long dateOfCurrentActivity = c.getLong(exactDateIndex);

            if (dateOfCurrentActivity < currentDateLong && dateOfCurrentActivity > date) {

                amountInPeriod++;
            }

            moreEntries = c.moveToNext();

        }

        return amountInPeriod;

    }


}


