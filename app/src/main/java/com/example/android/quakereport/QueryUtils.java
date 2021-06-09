package com.example.android.quakereport;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public final class QueryUtils {
    private  static JSONObject response;
    private static double mMagnitude;
    private static String mplace;
    private static String mTime;
    private static String mUrl;
    private final static String LOG_TAG=QueryUtils.class.getSimpleName();


    /**
     * Create a private constructor because no one should ever create a {@link QueryUtils} object.
     * This class is only meant to hold static variables and methods, which can be accessed
     * directly from the class name QueryUtils (and an object instance of QueryUtils is not needed).
     */
    private QueryUtils(){
    }

    // fetchEarthQukeData will be called from the main activity to create our json object
    public static ArrayList<Quake> fetchEarthQuakeData(String USGS_URL){



        URL url= createUrl(USGS_URL);
        String jsonResponse="";
        try {
            jsonResponse= makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG,"Caught IOException in retrieving JSON response");
        }
        ArrayList<Quake> earthquakes=extractEarthquakes(jsonResponse);
        return  earthquakes;
    }

    /**
     * Return a list of {@link Quake} objects that has been built up from
     * parsing a JSON response.
     */
    public static ArrayList<Quake> extractEarthquakes(String jsonResponse)  {

        // Create an empty ArrayList that we can start adding earthquakes to

        if(TextUtils.isEmpty(jsonResponse)) return null;
        ArrayList<Quake> earthquakes = new ArrayList<Quake>();

        // Try to parse the SAMPLE_JSON_RESPONSE. If there's a problem with the way the JSON
        // is formatted, a JSONException exception object will be thrown.
        // Catch the exception so the app doesn't crash, and print the error message to the logs

        try {
            response=new JSONObject(jsonResponse);
            JSONArray features= response.optJSONArray("features");
            JSONObject earthQuakeobject;
            JSONObject property;
            Date dateObject;
            SimpleDateFormat dateFormat;
            SimpleDateFormat timeFormat;
            DecimalFormat decimalFormat;
            String dateTodisplay,timeTodisplay,decimalMagnitude;

            for (int i=0;i< features.length();i++){
                earthQuakeobject=features.getJSONObject(i);
                property=earthQuakeobject.getJSONObject("properties");

                mMagnitude=property.getDouble("mag");
                mplace=property.getString("place");
                mTime=property.getString("time");
                mUrl=property.getString("url");


                long timeInMillisec=Long.valueOf(mTime);
                dateObject=new Date(timeInMillisec);

                dateFormat=new SimpleDateFormat("MMM dd, yyyy");
                timeFormat=new SimpleDateFormat("h:mm a");
                decimalFormat=new DecimalFormat("0.0");



                dateTodisplay=dateFormat.format(dateObject);
                timeTodisplay=timeFormat.format(dateObject);
                decimalMagnitude=decimalFormat.format(mMagnitude);

                earthquakes.add(new Quake(mplace,decimalMagnitude,dateTodisplay,timeTodisplay,mUrl));




            }

            // TODO: Parse the response given by the SAMPLE_JSON_RESPONSE string and
            // build up a list of Earthquake objects with the corresponding data.

        } catch (JSONException e) {
            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash. Print a log message
            // with the message from the exception.
            Log.e("QueryUtils", "Problem parsing the earthquake JSON results", e);
        }

        // Return the list of earthquakes
        return earthquakes;
    }


    //Function to create url from the String object
    private static URL createUrl(String stringUrl){
        URL url=null;
        try{
            url=new URL(stringUrl);
        }catch (MalformedURLException e){
            Log.e(LOG_TAG,"Malformed url occured");
        }
        return url;

    }

    //The below code will make the http request and will handle it accordingly for different possible outcomes

    private static String makeHttpRequest(URL url) throws IOException{
        String jsonResponse="";
        if(url==null) return jsonResponse;

        HttpURLConnection urlConnection=null;
        InputStream inputStream=null;

        try{
            urlConnection=(HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000);
            urlConnection.setConnectTimeout(15000);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            if(urlConnection.getResponseCode()== 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            }else {
                Log.e(LOG_TAG,"Error response code"+urlConnection.getResponseCode());
            }

        }catch (IOException e){
            Log.e(LOG_TAG,"IOException occured in http request");

        }
        finally {
            if(urlConnection!=null) urlConnection.disconnect();

            if(inputStream !=null) inputStream.close();
        }
        return jsonResponse;
    }

    private static String readFromStream(InputStream inputStream) throws IOException{
        StringBuilder builder=new StringBuilder();
        if(inputStream!=null){
            InputStreamReader inputStreamReader=new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader=new BufferedReader(inputStreamReader);
            String line=reader.readLine();
            while(line!=null){
                builder.append(line);
                line=reader.readLine();
            }

        }
       return builder.toString();
    }


}