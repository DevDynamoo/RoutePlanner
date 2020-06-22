package com.example.routeplanner;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

public class RunClockFragment extends Fragment {

    Button startButton, stopButton, finishButton;
    Chronometer chronometer;

    private boolean timeStarted = false;
    private long currentTimePassed;
    private long finishTime;

    private RunRouteActivity parentActivity;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_run_clock, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        parentActivity = (RunRouteActivity) getActivity();

        startButton = parentActivity.findViewById(R.id.start_button);
        stopButton = parentActivity.findViewById(R.id.stop_button);
        finishButton = parentActivity.findViewById(R.id.finish_run_button);

        chronometer = parentActivity.findViewById(R.id.chronometer);

        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finishButton.setEnabled(true);
                if (!timeStarted) {
                    chronometer.setBase(SystemClock.elapsedRealtime() - currentTimePassed);
                    chronometer.start();
                    timeStarted = true;
                }
            }
        });

        stopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (timeStarted) {
                    currentTimePassed = SystemClock.elapsedRealtime() - chronometer.getBase();
                    chronometer.stop();
                    timeStarted = false;
                } else {
                    chronometer.setBase(SystemClock.elapsedRealtime());
                    currentTimePassed = 0;
                    finishButton.setEnabled(false);
                }
            }
        });

        finishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog dialog = new AlertDialog.Builder(parentActivity)
                        .setMessage("Are you sure you want to finish?")
                        .setTitle("Finish route")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                //TODO implement code to retrieve correct routeDistance

                                double routeDistance = 10.0;
                                double avgSpeed = routeDistance / (currentTimePassed/1000.0/60.0/60.0);
                                double roundedAvgSpeed =  Math.round(avgSpeed*100) / 100.0;

                                //TODO implement code that saves avgSpeed in database

                                Toast.makeText(parentActivity, "Finished with speed of " + roundedAvgSpeed + "km/h", Toast.LENGTH_LONG).show();

                                Intent intent = new Intent(parentActivity, MainActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Toast.makeText(parentActivity, "Cancelled", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .create();
                dialog.show();
            }
        });
    }
}