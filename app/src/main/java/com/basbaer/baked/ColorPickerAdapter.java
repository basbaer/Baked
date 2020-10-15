package com.basbaer.baked;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import androidx.cardview.widget.CardView;

public class ColorPickerAdapter extends BaseAdapter {

    private Context context;

    //implementing an constructor
    public ColorPickerAdapter(Context context){
        super();

        this.context = context;
        ColorHandler.currentlySelectedColor = null;
    }

    public ColorPickerAdapter(Context context, String selectedColor){
        super();

        this.context = context;
        ColorHandler.currentlySelectedColor = selectedColor;


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
        final CardView outerCardView = view.findViewById(R.id.outerCardView);
        final String currentColor = ColorHandler.getColorsArrayList().get(position);

        //sets the color as selected
        if(ColorHandler.currentlySelectedColor != null && ColorHandler.currentlySelectedColor.equals(currentColor)) {

            outerCardView.setCardBackgroundColor(Color.parseColor("#000000"));

        }



        //sets the different colors as backgrounds
        colorImageView.setBackgroundColor(Color.parseColor(currentColor));


        //creating on click listener
        colorImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //sets the selected color as the color for creating a new activity
                AddActivity.color = currentColor;


                //sets the tapped color as selected
                //outerCardView.setCardBackgroundColor(Color.parseColor("#000000"));
                AddActivity.colorPickerAdapter = new ColorPickerAdapter(context, currentColor);



                Log.i("TappedColor", ColorHandler.getColorsArrayList().get(position));

            }
        });

        return view;
    }



}
