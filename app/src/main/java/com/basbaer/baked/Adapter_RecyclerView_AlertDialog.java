package com.basbaer.baked;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class Adapter_RecyclerView_AlertDialog extends RecyclerView.Adapter<Adapter_RecyclerView_AlertDialog.mViewHolder> {

    public static class mViewHolder extends RecyclerView.ViewHolder{

        public CheckBox checkBox;

        public mViewHolder(View view){
            super(view);

            checkBox = view.findViewById(R.id.checkbox_categories_main_activity);

        }


    }



    public Adapter_RecyclerView_AlertDialog(){


    }

    @NonNull
    @Override
    public mViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_category_checkboxes, parent, false);

        mViewHolder mViewHolder = new mViewHolder(v);

        return mViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull mViewHolder holder, int position) {
        String s = mCategories.allCategories.get(position).getName();

        holder.checkBox.setText(s);

        holder.checkBox.setChecked(mCategories.allCategories.get(position).isChecked());

    }

    @Override
    public int getItemCount() {
        return mCategories.allCategories.size();
    }
}
