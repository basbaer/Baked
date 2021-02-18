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

    private final Context context;
    private int BLACK = Color.parseColor("#000000");
    private int GREY = Color.parseColor("#AAAAAA");
    private String currentlySelectedColor;

    //implementing an constructor
    public ColorPickerAdapter(Context context){
        super();

        this.context = context;
        currentlySelectedColor = "";
    }

    public ColorPickerAdapter(Context context, String selectedColor){
        super();

        this.context = context;
        this.currentlySelectedColor = selectedColor;


    }


    //returns the amount of items
    @Override
    public int getCount() {
        return ColorHandler.getColorsArrayList().size();
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

        if (convertView == null){

            LayoutInflater vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            convertView = vi.inflate(R.layout.color_picker_layout, parent, false);

        }


        ImageView colorImageView = convertView.findViewById(R.id.colorIV);
        final CardView outerCardView = convertView.findViewById(R.id.outerCardView);
        final String currentColor = ColorHandler.getColorsArrayList().get(position);

        //sets the color as selected
        if(currentlySelectedColor.equals(currentColor)) {

            outerCardView.setCardBackgroundColor(BLACK);

        }else{

            outerCardView.setCardBackgroundColor(GREY);

        }



        //sets the different colors as backgrounds
        colorImageView.setBackgroundColor(Color.parseColor(currentColor));


        //creating on click listener
        colorImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //sets the selected color as the color for creating a new activity
                AddActivity.color = currentColor;

                //deselect
                if(outerCardView.getCardBackgroundColor().getDefaultColor() == BLACK){

                    outerCardView.setCardBackgroundColor(GREY);

                    currentlySelectedColor = "";

                }else{

                    //sets the tapped color as selected
                    outerCardView.setCardBackgroundColor(BLACK);


                    //changes the currently selected color in the color handler
                    currentlySelectedColor = currentColor;

                    AddActivity.color = currentColor;

                }

                notifyDataSetChanged();




            }
        });

        return convertView;
    }

    public void selectColor(String color){

        this.currentlySelectedColor = color;

        notifyDataSetChanged();

    }



}
