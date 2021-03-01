package com.basbaer.baked;



import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class mCategories {

    public static ArrayList<mCategories> allCategories;
    //same as all categories but without "All"
    public static ArrayList<mCategories> selectableCategoriesList;



    private final int id;
    private final String name;
    private boolean isChecked;
    private ArrayList<TrackedActivity> actvities_array;
    //only one instance of each activity is saved here
    private ArrayList<TrackedActivity> activitiesInstancesArray;


    public mCategories(int id, String name, boolean isChecked) {
        this.id = id;
        this.name = name;
        this.isChecked = isChecked;
        if(id != -1){
            this.actvities_array = TrackedActivity.getActivitiesOfCategory(id);
            this.activitiesInstancesArray = TrackedActivity.getActivityInstancesOfCategory(id);
        }



    }


    public static void updateCategoriesList(){

        allCategories = new ArrayList<>();
        selectableCategoriesList = new ArrayList<>();


        //first entry is the "All" entry



        allCategories.add(new mCategories(-1, "All", MainActivity.sharedPreferences.getBoolean(MainActivity.ISALLCHECKED, false)));

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

    public int getId(){return id;}

    public boolean isChecked() {
        return isChecked;
    }


    public void setChecked(boolean checked) {
        isChecked = checked;

        TrackedActivity.setIsChecked(this.getName(), checked);

    }

    /**
     *
     * @return ArrayList with all the activities of this category
     */
    public ArrayList<TrackedActivity> getAllActvities_array(){

        this.actvities_array = TrackedActivity.getActivitiesOfCategory(this.id);

        return this.actvities_array;
    }


    /**
     *
     * @return returs Array with one activity of each category as a representative of the activity
     */
    public ArrayList<TrackedActivity> getActivitiesInstancesArray(){

        this.activitiesInstancesArray = TrackedActivity.getActivityInstancesOfCategory(this.id);

        return this.activitiesInstancesArray;

    }


    public static ArrayList<String> getCategoryNamesList(){
        ArrayList<String> list = new ArrayList<>();

        if(selectableCategoriesList == null){
            selectableCategoriesList.addAll(TrackedActivity.getDifferentCategories());
        }
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
