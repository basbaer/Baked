package com.basbaer.baked;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class mCategories {

    public static ArrayList<mCategories> allCategories;
    //same as all categories but without "All"
    public static ArrayList<mCategories> selectableCategoriesList;

    //keeps track if the 'All' is checked
    public static final String ISALLCHECKED = "isAllChecked";
    public static SharedPreferences sharedPreferences;

    private int id;
    private final String name;
    private boolean isChecked;
    private ArrayList<TrackedActivity> actvities_array;


    public mCategories(int id, String name, boolean isChecked) {
        this.id = id;
        this.name = name;
        this.isChecked = isChecked;
        if(id != -1){
            this.actvities_array = TrackedActivity.getActivitiesOfCategory(id);
        }

    }


    public static void updateCategoriesList(Context context){

        allCategories = new ArrayList<>();
        selectableCategoriesList = new ArrayList<>();
        sharedPreferences = context.getSharedPreferences("com.basbaer.baked", Context.MODE_PRIVATE);

        //first entry is the "All" entry

        allCategories.add(new mCategories(-1, context.getString(R.string.all), sharedPreferences.getBoolean(ISALLCHECKED, false)));

        ArrayList<mCategories> categories = TrackedActivity.getDifferentCategories();

        allCategories.addAll(categories);
        selectableCategoriesList.addAll(categories);

        //sort the list alphabetically
        Collections.sort(allCategories, new Comparator<mCategories>() {
            @Override
            public int compare(mCategories o1, mCategories o2) {
                return o1.getName().compareTo(o2.getName());
            }
        });



    }


    public ArrayList<mCategories> getAllSelectabelCategories(){

        return selectableCategoriesList;



    }


    public String getName() {
        return name;
    }

    public boolean isChecked() {
        return isChecked;
    }


    public void setChecked(boolean checked) {
        isChecked = checked;

        TrackedActivity.setIsChecked(this.getName(), checked);

    }

    public ArrayList<TrackedActivity> getActvities_array(){

        this.actvities_array = TrackedActivity.getActivitiesOfCategory(this.id);

        return this.actvities_array;
    }

    public static ArrayList<String> getCategoryNamesList(){
        ArrayList<String> list = new ArrayList<>();
        for(int i = 0; i < selectableCategoriesList.size(); i++){
            list.add(selectableCategoriesList.get(i).getName());

        }

        return list;
    }


    @Override
    public String toString(){
        return this.name;
    }
}
