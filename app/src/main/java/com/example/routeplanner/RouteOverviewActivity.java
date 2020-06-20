package com.example.routeplanner;

import androidx.appcompat.app.AppCompatActivity;


import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class RouteOverviewActivity extends AppCompatActivity {

    private static final String TAG = "RouteOverview";
    private static ArrayList<RouteListItem> routeListItems = new ArrayList<>();
    private FirebaseDatabase appDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.route_overview_activity);

        Log.i(TAG, "onCreate called");

        ListView listView = (ListView) findViewById(R.id.listView);

        // Example routes to be deleted later
        RouteListItem example1 = new RouteListItem("My route", "10 km", "2", "15 km/h");
        RouteListItem example2 = new RouteListItem("Marathon", "42 km", "0", "15 km/h");

       // appDatabase = FirebaseDatabase.getInstance();
       // DatabaseReference ref = appDatabase.getReference("Routes");
       // ref.setValue(example1, example2);

        // Example routes created in onCreate method
        // Should be added in another way through user interaction
        if (!containsRouteWithName(example1.getName())) {
            addRoute(example1.getName(), example1.getDistance(), example1.getCompletions(), example1.getAvgSpeed());
        }
        if (!containsRouteWithName(example2.getName())) {
            addRoute(example2.getName(), example2.getDistance(), example2.getCompletions(), example2.getAvgSpeed());
        }

        RouteListAdapter adapter = new RouteListAdapter
                (RouteOverviewActivity.this, R.layout.route_overview_listadapter, routeListItems);
        listView.setAdapter(adapter);
    }

    public boolean containsRouteWithName(String name) {
        for (int i = 0; i < routeListItems.size(); i++) {
            if (routeListItems.get(i).getName() == name) {
                return true;
            }
        }
        return false;
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










