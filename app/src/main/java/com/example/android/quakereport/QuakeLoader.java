package com.example.android.quakereport;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.util.Log;

import java.util.ArrayList;


public class QuakeLoader extends AsyncTaskLoader<ArrayList<Quake>> {
    private String mUrl;
    private static final String LOG_TAG=QuakeLoader.class.getSimpleName();
    public   ArrayList<Quake> earthquakes;

    public QuakeLoader(Context context,String url){
        super(context);
        Log.v(LOG_TAG,"Now in QuakeLoader object created");
        mUrl=url;
    }
    @Override
    protected void onStartLoading() {
        Log.v(LOG_TAG,"Now in onStartLoading");
        forceLoad();

    }

    @Override
    public ArrayList<Quake> loadInBackground() {
        Log.v(LOG_TAG,"Now in loadInBackground");
        if(mUrl==null) return null;
       earthquakes=QueryUtils.fetchEarthQuakeData(mUrl);
        return earthquakes;
    }
}
