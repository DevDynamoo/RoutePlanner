package com.example.routeplanner;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

public class RouteOverviewActivity extends AppCompatActivity {

    private static final String TAG = "RouteOverview";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.route_overview_activity);

        Log.i(TAG, "onCreate called");
    }
}