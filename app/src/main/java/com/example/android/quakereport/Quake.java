package com.example.android.quakereport;

import android.app.Activity;
import android.content.Context;

import androidx.core.content.ContextCompat;

public class Quake {
    private String mCity;
    private String mMagnitude;
    private String mDate;
    private String mTime;
    private String mUrl;

    public Quake(String city, String  mag, String date,String time,String url) {
        mCity = city;
        mMagnitude = mag;
        mDate = date;
        mTime=time;
        mUrl=url;
    }

    public String getmCity() {
        return mCity;
    }

    public String  getmMagnitude() {
        return mMagnitude;
    }

    public String getmDate() {
        return mDate;
    }

    public String getmTime(){return mTime;}
    public String getmUrl(){
        return mUrl;
    }


}
