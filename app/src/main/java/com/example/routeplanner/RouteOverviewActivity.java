package com.example.routeplanner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;
import java.util.ArrayList;

public class RouteOverviewActivity extends AppCompatActivity {
    private static final String TAG = "RouteOverview";
    private static ArrayList<RouteListItem> routeListItems = new ArrayList<>();
    private FirebaseDatabase appDatabase;
    public static final String EXTRA_MESSAGE_ROUTE_OVERVIEW = "com.example.routeplanner.GET_LIST_ITEM";
    DatabaseReference ref;
    RouteListItem member;
    boolean startedForResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.route_overview_activity);

        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if(extras != null) {
                startedForResult = extras.getBoolean(EXTRA_MESSAGE_ROUTE_OVERVIEW);
            }
        } else {
            startedForResult = (boolean) savedInstanceState.getSerializable(EXTRA_MESSAGE_ROUTE_OVERVIEW);
        }

        Log.i(TAG, "onCreate called");

        // appDatabase = FirebaseDatabase.getInstance();
        // DatabaseReference ref = appDatabase.getReference("Routes");
        // ref.setValue(example1, example2);

        //Setting reference
        ref= FirebaseDatabase.getInstance().getReference().child("RouteListItem");


        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ds : dataSnapshot.getChildren()) {

                    addRoute(ds.getValue(RouteListItem.class).getName(),
                            ds.getValue(RouteListItem.class).getDistance(),
                            ds.getValue(RouteListItem.class).getPostions(),
                            ds.getValue(RouteListItem.class).isCyclic());


                }
                ListView listView = (ListView) findViewById(R.id.listView);
                RouteListAdapter adapter = new RouteListAdapter
                        (RouteOverviewActivity.this, R.layout.route_overview_listadapter, routeListItems);
                listView.setAdapter(adapter);
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                        if (startedForResult) {
                            Log.d(TAG, "Item clicked");
                            Intent returnIntent = new Intent(RouteOverviewActivity.this, PrepareRunActivity.class);
                            returnIntent.putExtra("name", ((RouteListItem) arg0.getItemAtPosition(position)).getName());
                            returnIntent.putExtra("distance", ((RouteListItem) arg0.getItemAtPosition(position)).getDistance());
                            returnIntent.putExtra("positions", ((RouteListItem) arg0.getItemAtPosition(position)).getPostions());
                            returnIntent.putExtra("cyclic", ((RouteListItem) arg0.getItemAtPosition(position)).isCyclic());
                            setResult(RESULT_OK, returnIntent);
                            finish();
                        } else {
                            Log.d(TAG, "Item not clicked");
                        }
                    }
                });
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }
        });

//        // Example routes to be deleted later
//        RouteListItem example1 = new RouteListItem("My route", "10 km", "2", "15 km/h");
//        RouteListItem example2 = new RouteListItem("Marathon", "42 km", "0", "15 km/h");
//
//        // Example routes created in onCreate method
//        // Should be added in another way through user interaction
//        if (!containsRouteWithName(example1.getName())) {
//            addRoute(example1.getName(), example1.getDistance(), example1.getCompletions(), example1.getAvgSpeed());
//        }
//        if (!containsRouteWithName(example2.getName())) {
//            addRoute(example2.getName(), example2.getDistance(), example2.getCompletions(), example2.getAvgSpeed());
//        }
    }

    public boolean containsRouteWithName(String name) {
        for (int i = 0; i < routeListItems.size(); i++) {
            if (routeListItems.get(i).getName() == name) {
                return true;
            }
        }
        return false;
    }

    public void addRoute(String name, float distance, String positions, boolean cyclic) {
        RouteListItem item = new RouteListItem();
        item.setPostions(positions);
        item.setCyclic(cyclic);
        item.setName(name);
        item.setDistance(distance);
        routeListItems.add(item);
    }

    public void removeRoute(int position) {
        routeListItems.remove(position);
    }

    public static ArrayList<RouteListItem> getRouteListItems() {
        return routeListItems;
    }
}



// Example routes to be deleted later
// addRoute("My route", "10 km", "2", "5 km/h");
//addRoute("Marathon", "42 km", "0", "5 km/h");


/*
        //ADD to database
        member = new RouteListItem();
        member.setName("Marathon");
        member.setDistance("42 km");
        member.setCompletions("0");
        member.setAvgSpeed("5 km/h");
        ref.push().setValue(member);
 */