package com.basbaer.baked;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import java.util.ArrayList;
import java.util.HashMap;

public class EditCategoriesActivitesList extends AppCompatActivity {

    ArrayList<mCategories> categoriesArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_categories_activites_list);

        categoriesArrayList = TrackedActivity.getDifferentCategories();

        HashMap<mCategories, ArrayList<TrackedActivity>> hm = createHashMap(categoriesArrayList);







    }

    //key category, value: list of activities
    private static HashMap<mCategories, ArrayList<TrackedActivity>> createHashMap(ArrayList<mCategories> categoriesArrayList){

        HashMap<mCategories, ArrayList<TrackedActivity>> hm = new HashMap<>();

        for(int i = 0; i < categoriesArrayList.size(); i++){
            hm.put(categoriesArrayList.get(i), categoriesArrayList.get(i).getActivitiesInstancesArray());

        }

        return hm;

    }
}

