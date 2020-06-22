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
    public double totalAvrSpeed;
    public double totalDistance;
    public int num;

    //New stats to add
    public int time;
    public double speed;
    public double distance;


    DatabaseReference refps;

    StatisticsData member;
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

                                //Distance
                                distance = 10.0;

                                //time converted to seconds
                                time=(int) (currentTimePassed/1000);
                                System.out.println(time);

                                //Average speed
                                speed = distance / (currentTimePassed/1000.0/60.0/60.0);
                                double roundedAvgSpeed =  Math.round(speed*100) / 100.0;

                                System.out.println(speed);

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

 /*
        //Create member
        member= new StatisticsData();
        member.setAvrSpeed(distance);
        member.setDistance(speed);
        member.setTime(time);

        //Add member
        ref.push().setValue(member);
        Toast.makeText(PrepareRunActivity.this,"Data Added",Toast.LENGTH_LONG).show();
*/


