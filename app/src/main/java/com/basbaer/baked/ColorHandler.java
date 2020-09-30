package com.basbaer.baked;


import android.content.SharedPreferences;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

//class that manages the shown colors and provides them in a HashSet
public class ColorHandler {

    private static ArrayList<String> colorsArrayList;

    //setting the colors
    private static String blue = "#343CD6";
    private static String red = "#C70039";
    private static String green = "#65EF75";
    private static String honey_yellow = "#F1C40F";
    private static String yellow = "#F8E800";
    private static String white = "#FFFFFF";
    private static String brown = "#A04000";
    private static String turkis = "#00F8DD";
    private static String purple = "#A569BD";
    private static String greenish = "#1ABC9C";
    private static String light_blue = "#3498DB";
    private static String marine = "#1F618D";
    private static String red_orange = "#E74C3C";
    private static String magenta = "#F200F8";
    private static String green_blue = "#48C9B0";

    //in the sharedPreferences could later be saves by the user be saved
    private static SharedPreferences colorsSharedPreference;

    public static ArrayList<String> getColorsArrayList(){

        //first get's the save HashSet, if the user has changed some of the colors
        //colorsArrayList = (HashSet<String>) colorsSharedPreference.getStringSet("colorsHashSet", null);

        //if the user has not changed anything, the HashSet is implemented with the StandardColors
        if(colorsArrayList == null){

            colorsArrayList = new ArrayList<>(15);

            //adding the colors
            colorsArrayList.add(blue);
            colorsArrayList.add(red);
            colorsArrayList.add(green);
            colorsArrayList.add(yellow);
            colorsArrayList.add(white);
            colorsArrayList.add(light_blue);
            colorsArrayList.add(red_orange);
            colorsArrayList.add(greenish);
            colorsArrayList.add(honey_yellow);
            colorsArrayList.add(purple);
            colorsArrayList.add(turkis);
            colorsArrayList.add(magenta);
            colorsArrayList.add(green_blue);
            colorsArrayList.add(brown);
            colorsArrayList.add(marine);


        }

        return colorsArrayList;


    }


}
