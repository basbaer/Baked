package com.basbaer.baked;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class mEditListRVAdapter extends RecyclerView.Adapter<mEditListRVAdapter.MyViewHolder> {

    //here are the items/views handled, which every entry of the RecyclerView contains
    //this example only contains a TextView
    public static class MyViewHolder extends RecyclerView.ViewHolder{

        public TextView textView;
        public ImageButton imageButton;

        //in this constructor, the views are referenced
        public MyViewHolder(View incomingTextView){
            super(incomingTextView);


            //Option 2
            textView = incomingTextView.findViewById(R.id.editListTVname);
            imageButton = incomingTextView.findViewById(R.id.editListimageButton);

        }
    }



    private  ArrayList<Integer> arrayListIds;

    //save which indexes are categories
    private HashMap<Integer, String> hashMapIndicesWichAreCategories;





    public mEditListRVAdapter(HashMap<mCategories, ArrayList<TrackedActivity>> incomingHashMap){

        arrayListIds = new ArrayList<>();

        hashMapIndicesWichAreCategories = new HashMap<>(incomingHashMap.size());

        int currentIndex = 0;

        for(mCategories key : incomingHashMap.keySet()){

            arrayListIds.add(currentIndex, key.getId());

            hashMapIndicesWichAreCategories.put(currentIndex, key.getName());

            currentIndex++;

            for (TrackedActivity activity : Objects.requireNonNull(incomingHashMap.get(key))){

                arrayListIds.add(currentIndex, activity.getId());

                currentIndex++;

            }

        }

    }


    //Sets up, how the Views in the ViewHolder have to look like and has to return the ViewHolder
    @Override
    public mEditListRVAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //Option 1: Set up given 'look' from Android
        //View viewThatGetsDisplayed = LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_list_item_1, parent, false);

        //Option 2: Set up your own 'look' via a TextView which functions as a template (therefore you have to create an other .xml-file)
        View viewThatGetsDisplayed = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_editlist_layout, parent, false);

        MyViewHolder viewHolder = new MyViewHolder(viewThatGetsDisplayed);

        return viewHolder;
    }

    //this method is called as many times as the getItemCount()-Result
    //-> for each item of the ArrayList once
    //here it sets the text for each entry of the RecyclerView
    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {


        //categories
        if(hashMapIndicesWichAreCategories.containsKey(position)){

            holder.textView.setText(hashMapIndicesWichAreCategories.get(position));



        }else{

            holder.textView.setText(TrackedActivity.getActivityNameById(arrayListIds.get(position)));

        }

        holder.imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String message;
                final boolean isCategory;

                if(hashMapIndicesWichAreCategories.containsKey(position)){
                    message = Resources.getSystem().getString(R.string.categoryWillBeDeleted);
                    isCategory = true;
                }else{
                    message = Resources.getSystem().getString(R.string.activityWillBeDelete);
                    isCategory = false;
                }


                new AlertDialog.Builder(holder.imageButton.getContext())
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle(Resources.getSystem().getString(R.string.reallyDelete))
                        .setMessage(message)
                        .setPositiveButton(Resources.getSystem().getString(R.string.delete), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                if(isCategory){
                                    TrackedActivity.deleteCategory(arrayListIds.get(position));
                                }else{
                                    TrackedActivity.deleteActivity(arrayListIds.get(position));
                                }


                                mCategories.updateCategoriesList();

                                EditCategoriesActivitesList.rvAdapter.notifyDataSetChanged();
                            }
                        })
                        .setNegativeButton(Resources.getSystem().getString(R.string.cancel), null)
                        .show();

            }
        });



    }

    //defines how many times the onBindViewHolder()-Method is called
    @Override
    public int getItemCount() {

        return arrayListIds.size();
    }



}
