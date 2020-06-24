package com.example.routeplanner;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class RouteOverviewActivity extends AppCompatActivity {
    private static final String TAG = "RouteOverview";
    private static ArrayList<RouteListItem> routeListItems = new ArrayList<>();
    private RouteListItem dbItem;

    RouteListAdapter adapter;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.route_overview_activity);

        listView = (ListView) findViewById(R.id.listView);

        adapter = new RouteListAdapter
                (RouteOverviewActivity.this, R.layout.route_overview_listadapter, routeListItems);

        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
            if (getCallingActivity() != null) {

                Log.d(TAG, "Item clicked");

                Intent returnIntent = new Intent(Intent.ACTION_SEND);

                RouteListItem item = (RouteListItem) arg0.getItemAtPosition(position);

                returnIntent.putExtra("name", item.getName());
                returnIntent.putExtra("distance", item.getDistance());
                returnIntent.putExtra("positions", item.getPositions());
                returnIntent.putExtra("cyclic", item.isCyclic());

                setResult(RESULT_OK, returnIntent);
                finish();
            } else {
                Log.d(TAG, "Item not clicked");
            }
            }
        });
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
