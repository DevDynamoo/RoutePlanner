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

import java.util.ArrayList;

public class RouteOverviewActivity extends AppCompatActivity {
    private static final String TAG = "RouteOverview";
    private static ArrayList<RouteListItem> routeListItems = new ArrayList<>();
    private FirebaseDatabase appDatabase;
    private RouteListItem dbItem;
    public static final String EXTRA_MESSAGE_ROUTE_OVERVIEW = "com.example.routeplanner.GET_LIST_ITEM";

    DatabaseReference ref;
    RouteListItem member;

    boolean startedForResult;
    RouteListAdapter adapter;
    private ListView listView;

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
        listView = (ListView) findViewById(R.id.listView);

        adapter = new RouteListAdapter
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
                    returnIntent.putExtra("positions", ((RouteListItem) arg0.getItemAtPosition(position)).getPositions());
                    returnIntent.putExtra("cyclic", ((RouteListItem) arg0.getItemAtPosition(position)).isCyclic());
                    setResult(RESULT_OK, returnIntent);
                    finish();
                } else {
                    Log.d(TAG, "Item not clicked");
                }
            }
        });

        Log.i(TAG, "onCreate called");

        // appDatabase = FirebaseDatabase.getInstance();
        // DatabaseReference ref = appDatabase.getReference("Routes");
        // ref.setValue(example1, example2);

        //Setting reference
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference ref = db.getReference().child("RouteListItem");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ds : dataSnapshot.getChildren()) {

                    dbItem = ds.getValue(RouteListItem.class);

                    if (!containsRouteWithName(dbItem.getName())) {
                        addRoute(dbItem.getName(), dbItem.getDistance(),
                                dbItem.getPositions(), dbItem.isCyclic());
                        adapter.notifyDataSetChanged();
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }
        });
    }

    public boolean containsRouteWithName(String name) {
        for (int i = 0; i < routeListItems.size(); i++) {
            if (routeListItems.get(i).getName().equals(name)) {
                return true;
            }
        }
        return false;
    }

    public void addRoute(String name, float distance, String positions, boolean cyclic) {
        RouteListItem item = new RouteListItem(name, distance, positions, cyclic);
        routeListItems.add(item);
    }

    public void removeRoute(int position) {
        routeListItems.remove(position);
    }

    public static ArrayList<RouteListItem> getRouteListItems() {
        return routeListItems;
    }
}
