package com.basbaer.baked;

import android.content.Context;

import java.util.Calendar;
import java.util.HashMap;

public class CalendarDay {

    Calendar displayedDate;
    HashMap<Integer, Calendar> datesHashMap;
    Context context;
    int position;

    public CalendarDay(Calendar date, HashMap<Integer, Calendar> datesHashMap, Context context, int position){
        this.displayedDate = date;
        this.datesHashMap = datesHashMap;
        this.context = context;
        this.position = position;

    }
}
