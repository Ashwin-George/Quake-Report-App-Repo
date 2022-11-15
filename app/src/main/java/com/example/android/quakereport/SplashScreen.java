package com.example.android.quakereport;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;


import android.content.Intent;
import android.os.Handler;

import android.content.Intent;
import android.os.Handler;

import android.os.Bundle;
import android.view.WindowManager;

import com.example.android.quakereport.MainActivity;
import com.example.android.quakereport.R;

public class SplashScreen extends AppCompatActivity {
    ActionBar mActionBar;


    private static int SPLASH_SCREEN_TIME_OUT=2000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //This method is used so that your splash activity
        //can cover the entire screen.
//        mActionBar=System.get
//        mActionBar.hide();
        getSupportActionBar().hide();
        setContentView(R.layout.activity_splash_screen);
        //this will bind your MainActivity.class file with activity_main.

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i=new Intent(SplashScreen.this,
                        MainActivity.class);
                //Intent is used to switch from one activity to another.

                startActivity(i);
                //invoke the SecondActivity.

                finish();
                //the current activity will get finished.
            }
        }, SPLASH_SCREEN_TIME_OUT);
    }
}

