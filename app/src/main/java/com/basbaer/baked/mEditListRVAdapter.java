package com.basbaer.baked;

import android.app.AlertDialog;
import android.content.Context;
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

    private Context context;






    public mEditListRVAdapter(HashMap<mCategories, ArrayList<TrackedActivity>> incomingHashMap, Context context){

        this.context = context;

        setUpLists(incomingHashMap);

    }


    private void setUpLists(HashMap<mCategories, ArrayList<TrackedActivity>> incomingHashMap){

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

        Log.i("Adapter", arrayListIds.toString() + "ids: " + hashMapIndicesWichAreCategories.toString());

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
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {




        //categories
        if(hashMapIndicesWichAreCategories.containsKey(position)){

            holder.textView.setText(hashMapIndicesWichAreCategories.get(position));

            holder.textView.setTextSize(25f);



        }else{

            String text =  TrackedActivity.getActivityNameById(arrayListIds.get(position));

            Log.i("position", String.valueOf(position));


            holder.textView.setText(text);

            holder.textView.setTextSize(22f);

        }

        holder.imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String message;
                final boolean isCategory;

                if(hashMapIndicesWichAreCategories.containsKey(position)){
                    message = context.getString(R.string.categoryWillBeDeleted);
                    isCategory = true;
                }else{
                    message = context.getString(R.string.activityWillBeDelete);
                    isCategory = false;
                }


                new AlertDialog.Builder(holder.imageButton.getContext())
                        .setIcon(android.R.drawable.ic_dialog_alert)

                        .setTitle(context.getString(R.string.reallyDelete))
                        .setMessage(message)
                        .setPositiveButton(context.getString(R.string.delete), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                if(isCategory){
                                    TrackedActivity.deleteCategory(arrayListIds.get(position));
                                }else{
                                    TrackedActivity.deleteActivity(arrayListIds.get(position));
                                }


                                mCategories.updateCategoriesList();


                                ArrayList<mCategories> categoriesArrayList = TrackedActivity.getDifferentCategories();

                                HashMap<mCategories, ArrayList<TrackedActivity>> hm = EditCategoriesActivitesList.createHashMap(categoriesArrayList);

                                setUpLists(hm);

                                notifyDataSetChanged();


                            }
                        })
                        .setNegativeButton(context.getString(R.string.cancel), null)
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
