package com.basbaer.baked;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.Calendar;
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

        return list;



    }
}
