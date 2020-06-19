package com.example.routeplanner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
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

import java.util.ArrayList;

import static com.example.routeplanner.RunRouteActivity.MyData;
import static com.example.routeplanner.RunRouteActivity.time;

public class PersonalStatisticsActivity extends AppCompatActivity {

    public static ArrayList<StatisticsData> statisticsDataItems = new ArrayList<>();

    //DatabaseReference ref;
    //StatisticsData member;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_statistics);

        //Setting textview
        TextView Display_distance = findViewById(R.id.displaydistance);
        TextView Display_time = findViewById(R.id.displaytime);
        TextView Display_avrspeed =findViewById(R.id.displayspeed);

        //Calculating statistics
        double caldistance = CalTotalDistance(statisticsDataItems);
        double caltime = CalTotalTime(statisticsDataItems);
        double calspeed = CalOverallAvrSpeed(statisticsDataItems);


        //Viewing staticstics
        Display_time.setText("Total Time: "+caltime);
        Display_distance.setText("Total distance: "+caldistance);
        Display_avrspeed.setText("Overall Average speed:"+calspeed);


    }

    private void loadData() {
        SharedPreferences sharedPreferences = getSharedPreferences(MyData, Context.MODE_PRIVATE);
        int de=sharedPreferences.getInt("time",0);
    }

    private double CalOverallAvrSpeed(ArrayList<StatisticsData> statisticsDataArrayList)  {
        double temp = 0.0;
        if(!statisticsDataArrayList.isEmpty()) {
            for (StatisticsData elm : statisticsDataArrayList) {
                temp += elm.avrSpeed;
            }
            return (temp / statisticsDataArrayList.size());
        }
        return temp;
    }
    private int CalTotalTime(ArrayList<StatisticsData> statisticsDataArrayList)  {
        int temp = 0;
        if(!statisticsDataArrayList.isEmpty()) {
            for (StatisticsData elm : statisticsDataArrayList) {
                temp += elm.time;
            }
            return (temp / statisticsDataArrayList.size());
        }
        return temp;
    }
    private double CalTotalDistance(ArrayList<StatisticsData> statisticsDataArrayList)  {
        double temp = 0.0;
        if(!statisticsDataArrayList.isEmpty()) {
            for (StatisticsData elm : statisticsDataArrayList) {
                temp += elm.distance;
            }
            return (temp / statisticsDataArrayList.size());
        }
        return temp;
    }
}


/*
        //Get reference
        ref= FirebaseDatabase.getInstance().getReference().child("StatisticsData");
        Toast.makeText(PersonalStatisticsActivity.this,"Firebase Connection success",
                Toast.LENGTH_LONG).show();

        //Create member
        member= new StatisticsData();
        member.setAvrSpeed(12.00);
        member.setDistance(51.50);
        member.setTime(14);

        //Add member
        ref.push().setValue(member);
        Toast.makeText(PersonalStatisticsActivity.this,"Data Added",Toast.LENGTH_LONG).show();
*/

//Delete
// deleteItem("");

/*
  private void deleteItem(String key) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("StatisticsData").child(key);
        ref.removeValue();
        Toast.makeText(this,"Data deleted",Toast.LENGTH_LONG).show();

    }
 */