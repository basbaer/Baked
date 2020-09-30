package com.basbaer.baked;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

public class ColorPickerAdapter extends BaseAdapter {

    private Context context;

    //implementing an constructor
    public ColorPickerAdapter(Context context){
        super();

        this.context = context;
    }


    //returns the amount of items
    @Override
    public int getCount() {
        return 15;
    }



    @Override
    public Object getItem(int position) {
        return ColorHandler.getColorsArrayList().get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        //get's the layout
        View view = LayoutInflater.from(context).inflate(R.layout.color_picker_layout, parent, false);

        ImageView colorImageView = view.findViewById(R.id.colorIV);

        //sets the different colors as backgrounds
        //colorImageView.setBackgroundColor(Color.parseColor(ColorHandler.getColorsArrayList().get(position)));


        //creating on click listener
        colorImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.i("TappedColor", ColorHandler.getColorsArrayList().get(position));

            }
        });

        return view;
    }
}
