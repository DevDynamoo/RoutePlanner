package com.example.routeplanner;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ListActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import java.util.ArrayList;

public class RouteOverviewActivity extends AppCompatActivity {

    private static final String TAG = "RouteOverview";
    private static ArrayList<RouteListItem> routeListItems = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.route_overview_activity);

        Log.i(TAG, "onCreate called");

        ListView listView = (ListView) findViewById(R.id.listView);

        // Example routes to be deleted later
        addRoute("My route", "10 km", "2", "5 km/h");
        addRoute("Marathon", "42 km", "0", "5 km/h");

        RouteListAdapter adapter = new RouteListAdapter
                (RouteOverviewActivity.this, R.layout.route_overview_listadapter, routeListItems);
        listView.setAdapter(adapter);
    }

    public void addRoute(String name, String distance, String completions, String avgSpeed) {
        routeListItems.add(new RouteListItem(name, distance, completions, avgSpeed));
    }

    public void removeRoute(int position) {
        routeListItems.remove(position);
    }

    public static ArrayList<RouteListItem> getRouteListItems() {
        return routeListItems;
    }
}










