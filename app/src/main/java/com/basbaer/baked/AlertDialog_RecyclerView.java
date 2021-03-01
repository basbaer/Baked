package com.basbaer.baked;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.Window;


import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.basbaer.baked.databinding.AlertDialogLayoutSelectingCategoriesBinding;

public class AlertDialog_RecyclerView extends Dialog{

    AlertDialogLayoutSelectingCategoriesBinding alertDialogSelectCategoriesBinding;

    public Activity activity;
    RecyclerView recyclerView;
    Adapter_RecyclerView_AlertDialog adapter;


    public AlertDialog_RecyclerView(Activity activity, Adapter_RecyclerView_AlertDialog adapter){
        super(activity);

        this.activity = activity;
        this.adapter = adapter;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        alertDialogSelectCategoriesBinding = AlertDialogLayoutSelectingCategoriesBinding.inflate(getLayoutInflater());
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(alertDialogSelectCategoriesBinding.getRoot());


        //set up the RecyclerView for AlertDialog
        recyclerView = alertDialogSelectCategoriesBinding.recyclerViewAlertDialog;

        //setting the layout manager
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(activity);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(adapter);


    }
}
