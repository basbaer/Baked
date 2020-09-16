package com.basbaer.baked;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import static android.content.Context.MODE_PRIVATE;

public class TrackedActivity {

    private static SQLiteDatabase database;
    private static int idIndex;
    private static int activityIndex;
    private static int activityTypeIndex;
    private static int dateIndex;
    private static int colorIndex;


    public TrackedActivity(String activity, String activityType, Long date, String color, Context context){

        if(database == null){
            createDB(context);
        }

        String sql = "INSERT INTO activities ("
                + "activity, activityType, date, color"
                + ") VALUES ('"
                + activity + "', '"
                + activityType + "', "
                + date.intValue() + ", '"
                + color + "')";

        database.execSQL(sql);


    }

    public static void createDB(Context context){

        try {

            database = context.openOrCreateDatabase("activitiesDB", MODE_PRIVATE, null);

            String createTableSqlCode = "CREATE TABLE IF NOT EXISTS activities ("
                    + "id INTEGER PRIMARY KEY, "
                    + "activity VARCHAR, "
                    + "activityType VARCHAR, "
                    + "date INTEGER, "
                    + "color VARCHAR)";

            database.execSQL(createTableSqlCode);

            Cursor c = database.rawQuery("SELECT * FROM activities", null);

            idIndex = c.getColumnIndex("id");
            activityIndex = c.getColumnIndex("activity");
            activityTypeIndex = c.getColumnIndex("activityType");
            dateIndex = c.getColumnIndex("date");
            colorIndex = c.getColumnIndex("color");


        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    public void printDatabase(){


        String sql = "SELECT * FROM activities";

        Cursor c = database.rawQuery(sql, null);

        boolean moreEntries = c.moveToFirst();

        while(moreEntries){

            String printString = "id: "
                    + c.getString(idIndex)
                    + "  activity: "
                    + c.getString(activityIndex)
                    + "  activityType: "
                    + c.getString(activityTypeIndex)
                    + "  date: "
                    + c.getInt(dateIndex)
                    + "  color: "
                    + c.getString(colorIndex);

            Log.i("DatabaseContent", printString);

            moreEntries = c.moveToNext();



        }



    }

    public void clearDatabase(){

        database.execSQL("DELETE FROM activities");



    }


}
