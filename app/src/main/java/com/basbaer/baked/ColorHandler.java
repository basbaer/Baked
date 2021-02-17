package com.basbaer.baked;


import java.util.ArrayList;

//class that manages the shown colors and provides them in a HashSet
public class ColorHandler {

    private static ArrayList<String> colorsArrayList;

    //setting the colors
    private static final String blue = "#0152CF";
    private static final String red = "#C70039";
    private static final String green = "#2ECC71";
    private static final String green_n = "#65EF75";
    private static final String yellow = "#F8E800";
    private static final String white = "#FFFFFF";
    private static final String brown = "#A04000";
    private static final String turkis = "#00F8DD";
    private static final String purple = "#BE00D5";
    private static final String purple_light = "#A569BD";
    private static final String light_blue = "#3498DB";
    private static final String red_orange = "#FA8603";
    private static final String magenta = "#F200F8";
    private static final String magenta_red = "#FA037B";
    private static final String yellow_green = "#C3FC00";

    //needed for the selection of the color
    public static String currentlySelectedColor;




    public static ArrayList<String> getColorsArrayList(){

        //first get's the save HashSet, if the user has changed some of the colors
        //colorsArrayList = (HashSet<String>) colorsSharedPreference.getStringSet("colorsHashSet", null);

        //if the user has not changed anything, the HashSet is implemented with the StandardColors
        if(colorsArrayList == null){

            colorsArrayList = new ArrayList<>(15);

            //adding the colors
            //1st row
            colorsArrayList.add(blue);
            colorsArrayList.add(green);
            colorsArrayList.add(yellow);
            colorsArrayList.add(red);
            colorsArrayList.add(purple);


            //2nd row
            colorsArrayList.add(light_blue);
            colorsArrayList.add(green_n);
            colorsArrayList.add(red_orange);
            colorsArrayList.add(magenta_red);
            colorsArrayList.add(purple_light);


            //3rd row
            colorsArrayList.add(turkis);
            colorsArrayList.add(yellow_green);
            colorsArrayList.add(brown);
            colorsArrayList.add(magenta);
            colorsArrayList.add(white);





        }

        return colorsArrayList;


    }



}
