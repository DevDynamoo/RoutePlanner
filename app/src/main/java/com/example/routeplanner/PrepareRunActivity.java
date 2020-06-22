package com.example.routeplanner;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;


public class PrepareRunActivity extends AppCompatActivity {
    static private final int GET_LIST_ITEM_REQUEST_CODE = 1;
    static private final int START_ROUTE_RUN_REQUEST_CODE = 2;
    public static final String EXTRA_MESSAGE_ROUTE_OVERVIEW = "com.example.routeplanner.GET_LIST_ITEM";
    String ID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prepare_run_activity);

        Button runRouteButton = findViewById(R.id.button_run_route);
        runRouteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PrepareRunActivity.this, RunRouteActivity.class);
                startActivityForResult(intent, START_ROUTE_RUN_REQUEST_CODE);
            }
        });

        Button chooseRouteButton = findViewById(R.id.button_choose_route);
        chooseRouteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PrepareRunActivity.this, RouteOverviewActivity.class);
                intent.putExtra(EXTRA_MESSAGE_ROUTE_OVERVIEW, true);
                startActivityForResult(intent, GET_LIST_ITEM_REQUEST_CODE);
            }
        });
    }
    protected void OnActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == Activity.RESULT_OK &&
            requestCode == GET_LIST_ITEM_REQUEST_CODE) {

        } else if (resultCode == Activity.RESULT_OK &&
                requestCode == START_ROUTE_RUN_REQUEST_CODE) {

        }
    }
    public void testMethod(){}
}