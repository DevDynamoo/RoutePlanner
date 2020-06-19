package com.example.routeplanner;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

public class RunRouteActivity extends AppCompatActivity {
    public static final String MyData = "Data" ;
    public static final String time = "timeKey";
    public static final String distance = "distanceKey";
    public static final String speed = "speedKey";
    SharedPreferences sharedpreferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_run_route);

        sharedpreferences = getSharedPreferences(MyData, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();

        editor.putInt(time, 5);
        editor.putFloat(distance, 15.00f);
        editor.putFloat(speed, 12.21f);
        editor.commit();



    }
}