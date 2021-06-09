package com.example.android.quakereport;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class QuakeAdapter extends ArrayAdapter<Quake> {

    public QuakeAdapter(@NonNull Activity context, ArrayList<Quake> earthquakes) {
        super(context, 0, earthquakes);
           }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Check if the existing view is being reused, otherwise inflate the view
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.list_item, parent, false);
        }

        Quake currentQuake = getItem(position);
        TextView magText = (TextView) listItemView.findViewById(R.id.mag);
        String magnitude = currentQuake.getmMagnitude();
        magText.setText(magnitude);

        String location_city= currentQuake.getmCity();
        String location;
        String city;

        if(location_city.contains(" of ")){
            String parts[]=location_city.split(" of ");
            location=parts[0]+" of ";
            city=parts[1];
        }else{
            location="Near the ";
            city=location_city;
        }

        GradientDrawable magCircle=(GradientDrawable) magText.getBackground();
        int magnitudeColor=getMagnitudeColor(currentQuake.getmMagnitude());
        magCircle.setColor(magnitudeColor);

        TextView cityText = (TextView) listItemView.findViewById(R.id.city);
        cityText.setText(city);

        TextView locationText=(TextView) listItemView.findViewById(R.id.location);
        locationText.setText(location);


        TextView dateText = (TextView) listItemView.findViewById(R.id.date);
        dateText.setText(currentQuake.getmDate());

        TextView timeText=(TextView) listItemView.findViewById(R.id.time_format);
        timeText.setText(currentQuake.getmTime());
        return listItemView;


    }
    public int getMagnitudeColor(String magnitude){
        int mag=(int)(Math.floor(Double.valueOf(magnitude)));
        int colorResourceid;
        switch (mag) {
            case 0:
            case 1:
                colorResourceid = R.color.magnitude1;
                break;
            case 2:
                colorResourceid = R.color.magnitude2;
                break;
            case 3:
                colorResourceid = R.color.magnitude3;
                break;
            case 4:
                colorResourceid = R.color.magnitude4;
                break;
            case 5:
                colorResourceid = R.color.magnitude5;
                break;
            case 6:
                colorResourceid = R.color.magnitude6;
                break;
            case 7:
                colorResourceid = R.color.magnitude7;
                break;
            case 8:
                colorResourceid = R.color.magnitude8;
                break;
            case 9:
                colorResourceid = R.color.magnitude9;
                break;
            default:
                colorResourceid = R.color.magnitude10plus;

        }
        int color = ContextCompat.getColor( getContext(), colorResourceid);
        return color;
    }
}
