package com.example.routeplanner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;



public class PersonalStatisticsActivity extends AppCompatActivity {


    DatabaseReference rootRef;
    String ID;

    TextView Display_distance;
    TextView Display_time;
    TextView Display_avrspeed;

    public int totalTime;
    public double AvrSpeed;
    public double totalDistance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_statistics);

        //Get reference
        rootRef= FirebaseDatabase.getInstance().getReference().child("PersonalStats");
        ID="-MAD-Y5CEBd4OA0yp7g4";

        //Setting textview
        Display_distance = findViewById(R.id.displaydistance);
        Display_time = findViewById(R.id.displaytime);
        Display_avrspeed =findViewById(R.id.displayspeed);

        //Getting lastest stats
        rootRef.child(ID).addListenerForSingleValueEvent(new ValueEventListener() {
                 @Override
                 public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                     //Getting info to stats
                     totalTime=dataSnapshot.getValue(PersonalStats.class).getTime();
                     AvrSpeed=Calspeed(dataSnapshot.getValue(PersonalStats.class).getNum(),dataSnapshot.getValue(PersonalStats.class).getTotalspeed());
                     totalDistance=dataSnapshot.getValue(PersonalStats.class).getTotaldistance();

                     //formating statistics
                     DecimalFormat df = new DecimalFormat("#.00");

                     //Viewing staticstics
                     if (totalTime<60){
                         Display_time.setText("Total Time:\n"+totalTime+" s");
                     }
                     else {
                         Display_time.setText("Total Time:\n"+totalTime/60+" min");
                     }
                     Display_distance.setText("Total distance:\n"+df.format(totalDistance)+" km");
                     Display_avrspeed.setText("Average speed:\n"+df.format(AvrSpeed)+" km/h");
                 }

                 @Override
                 public void onCancelled(@NonNull DatabaseError databaseError) {

                 }
             });
    }


    private double Calspeed(int num, double total )  {

            return (total / (double) num );

    }
}



