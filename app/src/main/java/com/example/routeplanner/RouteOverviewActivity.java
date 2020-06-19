package com.example.routeplanner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ListActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class RouteOverviewActivity extends AppCompatActivity {

    private static final String TAG = "RouteOverview";
    private ArrayList<RouteListItem> routeListItems = new ArrayList<>();
    DatabaseReference ref;
    RouteListItem member;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.route_overview_activity);

        Log.i(TAG, "onCreate called");

        //Setting reference
        ref= FirebaseDatabase.getInstance().getReference().child("RouteListItem");

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ds : dataSnapshot.getChildren()) {

                    addRoute(ds.getValue(RouteListItem.class).getName(),
                            ds.getValue(RouteListItem.class).getDistance(),
                            ds.getValue(RouteListItem.class).getCompletions(),
                            ds.getValue(RouteListItem.class).getAvgSpeed());

                    ListView listView = (ListView) findViewById(R.id.listView);


                    RouteListAdapter adapter = new RouteListAdapter
                            (RouteOverviewActivity.this, R.layout.route_overview_listadapter, routeListItems);
                    listView.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    public void addRoute(String name, String distance, String completions, String avgSpeed) {
        RouteListItem item = new RouteListItem();
        item.setAvgSpeed(avgSpeed);
        item.setCompletions(completions);
        item.setName(name);
        item.setDistance(distance);
        routeListItems.add(item);

    }

    public void removeRoute(int position) {
        routeListItems.remove(position);
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





