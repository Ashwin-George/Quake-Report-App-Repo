package com.example.android.quakereport;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.content.AsyncTaskLoader;

import android.app.Activity;
import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.preference.PreferenceManager;

import org.json.JSONObject;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import android.app.LoaderManager.LoaderCallbacks;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;


public class MainActivity extends AppCompatActivity implements LoaderCallbacks<ArrayList<Quake>> {
    private static final String USGS_request="https://earthquake.usgs.gov/fdsnws/event/1/query";
    private static final String USGS_request_sample = "https://earthquake.usgs.gov/fdsnws/event/1/query?format=geojson&orderby=time&minmag=6&limit=20";


    private final static String LOG_TAG = MainActivity.class.getSimpleName();
    private QuakeAdapter itemsAdapter;
    private Context context;
    private ArrayList<Quake> result;
    private TextView emptyTextView;
    private int loader_id = 1;

    LottieAnimationView loadingAnim;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loadingAnim= findViewById(R.id.animationView);
        ConnectivityManager cm;
        cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);


        ListView listView = (ListView) findViewById(R.id.list);


        itemsAdapter = new QuakeAdapter(this, new ArrayList<Quake>());
        emptyTextView = (TextView) findViewById(R.id.emptyState);
        emptyTextView.setText("");



        listView.setAdapter(itemsAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Quake object = itemsAdapter.getItem(position);
                String url = object.getmUrl();
                Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(i);


            }
        });

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();

        if (isConnected) {

            LoaderManager loaderManager = getLoaderManager();

            loaderManager.initLoader(loader_id, null, this);
            Log.v(LOG_TAG, "InitLoader called");
        } else {
//            View indicator = findViewById(R.id.animationView);
            loadingAnim.setVisibility(View.GONE);


            emptyTextView = findViewById(R.id.emptyState);
            emptyTextView.setText("No internet connection found");
        }


    }


    @Override
    public Loader<ArrayList<Quake>> onCreateLoader(int id, Bundle args) {

        Log.v(LOG_TAG, "Now in onCreateLoader");

        SharedPreferences sharedPreferences=PreferenceManager.getDefaultSharedPreferences(this);
        String minMagnitude=sharedPreferences.getString(getString(R.string.settings_min_magnitude_key),
                getString(R.string.settings_min_magnitude_default));

        String orderBy=sharedPreferences.getString(getString(R.string.settings_order_by_key),
                getString(R.string.settings_order_by_default));

        Uri baseUri=Uri.parse(USGS_request);
        Uri.Builder builder=baseUri.buildUpon();

        builder.appendQueryParameter("format","geojson");
        builder.appendQueryParameter("limit","20");
        builder.appendQueryParameter("minmag",minMagnitude);
        builder.appendQueryParameter("orderby",orderBy);

        return new QuakeLoader(this, builder.toString());
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<Quake>> loader, ArrayList<Quake> data) {
//        ProgressBar progressBar = (ProgressBar) findViewById(R.id.animationView);
        loadingAnim.setVisibility(View.GONE);
        itemsAdapter.clear();

        Log.v(LOG_TAG, "Now in onLoadFinished");

        if (data != null || !data.isEmpty())
            itemsAdapter.addAll(data);
        else
            emptyTextView.setText("Currently No Earthquakes found :-( ");


        ListView listView = findViewById(R.id.list);

        listView.setEmptyView(emptyTextView);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id=item.getItemId();
        if(id==R.id.action_settings){
            Intent settingsIntent=new Intent(this,SettingsActivity.class);
            startActivity(settingsIntent);
            return true;
        }
        return  super.onOptionsItemSelected(item);
    }

    @Override
    public void onLoaderReset(Loader<ArrayList<Quake>> loader) {
        Log.v(LOG_TAG, "Now in onLoaderReset");
        itemsAdapter.clear();
    }



}

