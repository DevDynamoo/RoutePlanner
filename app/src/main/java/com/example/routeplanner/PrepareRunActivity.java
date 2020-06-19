package com.example.routeplanner;

import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.*;
import java.util.HashMap;


public class PrepareRunActivity extends AppCompatActivity {

    DatabaseReference ref;
    DatabaseReference refps;

    StatisticsData member;
    PersonalStats personalStats;

    public int totalTime;
    public double totalAvrSpeed;
    public double totalDistance;
    public int num;

    int time;
    double distance;
    double speed;

    String ID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prepare_run_activty);

        //setting reference
        ref= FirebaseDatabase.getInstance().getReference().child("StatisticsData");
        Toast.makeText(PrepareRunActivity.this,"Firebase Connection success",
                Toast.LENGTH_LONG).show();

        //values to save to database
        distance=34.00;
        speed=67.50;
        time=18;

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
}