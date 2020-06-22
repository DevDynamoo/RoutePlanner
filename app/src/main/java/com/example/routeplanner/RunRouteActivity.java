package com.example.routeplanner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class RunRouteActivity extends AppCompatActivity {
    private Chronometer chronometer;
    private boolean running;
    private long stop;
    String currentTime;
    int secondsPast;


    //Total stats
    public int totalTime;
    public double totalAvrSpeed;
    public double totalDistance;
    public int num;

    //New stats to add
    public int time;
    public double speed;
    public double distance;



    DatabaseReference ref;
    DatabaseReference refps;

    StatisticsData member;
    PersonalStats personalStats;

    String ID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_run_route);

        distance=Double.parseDouble(getIntent().getStringExtra("distance"));
        System.out.println(distance);


        chronometer =findViewById(R.id.chronometer);

        final Button startButton = (Button) findViewById(R.id.start_button);
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!running) {
                    chronometer.setBase(SystemClock.elapsedRealtime()-stop);
                    chronometer.start();
                    running = true;

                }

            }
        });

        Button stopButton = (Button) findViewById(R.id.stop_button);

        stopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chronometer.stop();
                stop = SystemClock.elapsedRealtime()-chronometer.getBase();
                chronometer.setFormat("Total Time: %s");
                running = false;


                //How to get time to string (Format 00:00)
                currentTime=chronometer.getText().toString();
                System.out.println(currentTime);

                //time converted to seconds
                time=(int) (stop/1000);
                System.out.println(time);

                //Average speed
                speed=CalAvrSpeed(time,distance);
                System.out.println(speed);



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



            }

        });




    }

    public double CalAvrSpeed (int timeused, double distanceran) {
        //distance ran is in km, timeused is in second, converting to km/h)
        return ((distanceran/(double) timeused)*3600);
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
