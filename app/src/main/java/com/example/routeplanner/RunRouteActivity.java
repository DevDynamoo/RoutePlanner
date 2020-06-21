package com.example.routeplanner;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;

public class RunRouteActivity extends AppCompatActivity {
    private Chronometer chronometer;
    private boolean running;
    private long stop;
    String currentTime;
    int secondsPast;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_run_route);

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

                    //How to convert to seconds
                    secondsPast=(int) (stop/1000);
                    System.out.println(secondsPast);


                }

        });




}
}