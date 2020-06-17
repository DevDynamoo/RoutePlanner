package com.example.routeplanner;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button prepareRunActivityButton = (Button) findViewById(R.id.button_run_route);
        prepareRunActivityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent startPrepareRouteIntent = new Intent(MainActivity.this, PrepareRunActivty.class);
                startActivity(startPrepareRouteIntent);
            }
        });
        Button createRouteActivityButton = (Button) findViewById(R.id.button_create_route);
        createRouteActivityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent startCreateRouteIntent = new Intent(MainActivity.this, CreateRouteActivity.class);
                startActivity(startCreateRouteIntent);
            }
        });
        Button routeOverviewActivityButton = (Button) findViewById(R.id.button_route_overview);
        routeOverviewActivityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent startRouteOverviewIntent = new Intent(MainActivity.this, RouteOverviewActivity.class);
                startActivity(startRouteOverviewIntent);
            }
        });
    }
}