package com.basbaer.baked;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import java.security.AccessControlContext;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;

class CalendarAdapter extends BaseAdapter {

    private ArrayList<Calendar> datesAL;
    private final Context context;
    private TextView textView;
    private int paddingBottom;


    public CalendarAdapter(Context context, ArrayList<Calendar> datesAL, int paddingBottom){
        super();

        this.context = context;
        this.datesAL = datesAL;
        this.paddingBottom = paddingBottom;

    }


    @Override
    public int getCount() {
        return datesAL.size();
    }

    @Override
    public Object getItem(int position) {
        return datesAL.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        //inflates the View with the custom_calendar_day.xml
        convertView = LayoutInflater.from(context).inflate(R.layout.custom_calendar_day, parent, false);


        textView = convertView.findViewById(R.id.daysTV);

        textView.setPadding(5,5,5, paddingBottom);

        int displayedDate = datesAL.get(position).get(Calendar.DAY_OF_MONTH);

        textView.setText(String.valueOf(displayedDate));

        textView.setClickable(false);

        //gets the constraint layout
        ConstraintLayout cl = convertView.findViewById(R.id.daysCL);


        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context, AddActivity.class);

                long dateInSeconds = datesAL.get(position).getTime().getTime();

                intent.putExtra("date", dateInSeconds);



                context.startActivity(intent);


            }
        });

        return textView;
    }
}
