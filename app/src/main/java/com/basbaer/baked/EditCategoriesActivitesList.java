package com.basbaer.baked;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.basbaer.baked.databinding.ActivityEditCategoriesActivitesListBinding;

import java.util.ArrayList;
import java.util.HashMap;

public class EditCategoriesActivitesList extends AppCompatActivity {

    ArrayList<mCategories> categoriesArrayList;

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    public static mEditListRVAdapter rvAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        com.basbaer.baked.databinding.ActivityEditCategoriesActivitesListBinding activityEditCategoriesActivitesListBinding = ActivityEditCategoriesActivitesListBinding.inflate(getLayoutInflater());
        View view = activityEditCategoriesActivitesListBinding.getRoot();
        setContentView(view);

        categoriesArrayList = TrackedActivity.getDifferentCategories();

        HashMap<mCategories, ArrayList<TrackedActivity>> hm = createHashMap(categoriesArrayList);

        Log.i("HashMap", String.valueOf(hm));

        recyclerView = activityEditCategoriesActivitesListBinding.recyclerViewEditList;

        layoutManager = new LinearLayoutManager(this);

        recyclerView.setLayoutManager(layoutManager);

        rvAdapter = new mEditListRVAdapter(hm, getApplicationContext());

        recyclerView.setAdapter(rvAdapter);







    }

    //key category, value: list of activities
    protected static HashMap<mCategories, ArrayList<TrackedActivity>> createHashMap(ArrayList<mCategories> categoriesArrayList){

        HashMap<mCategories, ArrayList<TrackedActivity>> hm = new HashMap<>();

        for(int i = 0; i < categoriesArrayList.size(); i++){
            hm.put(categoriesArrayList.get(i), categoriesArrayList.get(i).getActivitiesInstancesArray());

        }

        return hm;

    }
}

