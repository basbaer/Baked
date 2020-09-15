package com.basbaer.baked;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;

class CalendarAdapter extends BaseAdapter {

    private HashMap<Integer, Calendar> datesHashMap;
    private final Context context;
    private TextView datesTextView;



    public CalendarAdapter(Context context, HashMap<Integer, Calendar> datesAL){
        super();

        this.context = context;
        this.datesHashMap = datesAL;


    }


    @Override
    public int getCount() {
        return datesHashMap.size();
    }

    @Override
    public Object getItem(int position) {
        return datesHashMap.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        //inflates the View with the custom_calendar_day.xml
        convertView = LayoutInflater.from(context).inflate(R.layout.custom_calendar_day, parent, false);

        datesTextView = convertView.findViewById(R.id.daysTV);


        Calendar displayedDate = datesHashMap.get(position);

        if(position < 7){

            datesTextView.setText(displayedDate.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.SHORT, Locale.getDefault()));

            //design
            datesTextView.setTextColor(Color.parseColor("#222222"));
            datesTextView.setTextAppearance(R.style.TextAppearance_AppCompat_Body1);
            datesTextView.setTextSize(18f);
            datesTextView.setPadding(5,5,5,5);

        }else{


            datesTextView.setText(String.valueOf(displayedDate.get(Calendar.DAY_OF_MONTH)));

        }




        datesTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context, AddActivity.class);

                long dateInSeconds = datesHashMap.get(position).getTime().getTime();

                intent.putExtra("date", dateInSeconds);



                context.startActivity(intent);


            }
        });

        return datesTextView;
    }
}
