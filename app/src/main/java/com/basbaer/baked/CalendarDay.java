package com.basbaer.baked;


import java.util.Calendar;
import java.util.HashMap;

public class CalendarDay {

    Calendar displayedDate;
    HashMap<Integer, Calendar> datesHashMap;
    int position;

    public CalendarDay(Calendar date, HashMap<Integer, Calendar> datesHashMap, int position){
        this.displayedDate = date;
        this.datesHashMap = datesHashMap;
        this.position = position;

    }
}
