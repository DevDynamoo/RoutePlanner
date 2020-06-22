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

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class RunClockFragment extends Fragment {

    Button startButton, stopButton, finishButton;
    Chronometer chronometer;

    private boolean timeStarted = false;
    private long currentTimePassed;
    private long finishTime;


    //Total stats
    public int totalTime;
    public float totalAvrSpeed;
    public float totalDistance;
    public int num;

    //New stats to add
    public int time;
    public float speed;
    public float distance;


    DatabaseReference refps;

    PersonalStats personalStats;

    String ID;

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
                finishButton.setEnabled(false);
                if (!timeStarted) {
                    chronometer.setBase(SystemClock.elapsedRealtime() - currentTimePassed);
                    chronometer.start();
                    timeStarted = true;
                    System.out.println(parentActivity.distance);
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
                    finishButton.setEnabled(true);

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

                                //Distance
                                distance=parentActivity.distance;

                                //time converted to seconds
                                time=(int) (currentTimePassed/1000);

                                //Average speed
                                speed = (float) (distance / (currentTimePassed/1000.0/60.0/60.0));
                                float roundedAvgSpeed = (Math.round(speed*100.0f) / 100.0f);



                                //TODO implement code that saves avgSpeed in database

                                Toast.makeText(parentActivity, "Finished with speed of " + roundedAvgSpeed + "km/h", Toast.LENGTH_LONG).show();

                                /* Updating statistics */

                                //setting reference
                                refps= FirebaseDatabase.getInstance().getReference().child("PersonalStats");
                                ID="-MAD-Y5CEBd4OA0yp7g4";
                                //getting current values, calcutating new stats & updating it
                                refps.child(ID).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        totalTime=dataSnapshot.getValue(PersonalStats.class).getTime()+time;
                                        totalAvrSpeed=dataSnapshot.getValue(PersonalStats.class).getTotalspeed()+speed;
                                        totalDistance=dataSnapshot.getValue(PersonalStats.class).getTotaldistance()+distance;
                                        num=dataSnapshot.getValue(PersonalStats.class).getNum()+1;

                                        //updating values
                                        HashMap hashMap = new HashMap();

                                        hashMap.put("num",num);
                                        refps.child(ID).updateChildren(hashMap);

                                        hashMap.put("time",totalTime);
                                        refps.child(ID).updateChildren(hashMap);

                                        hashMap.put("totaldistance",totalDistance);
                                        refps.child(ID).updateChildren(hashMap);

                                        hashMap.put("totalspeed",totalAvrSpeed);
                                        refps.child(ID).updateChildren(hashMap);

                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });

                                //Go to main activity
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

