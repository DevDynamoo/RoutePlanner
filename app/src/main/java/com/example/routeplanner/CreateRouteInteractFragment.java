package com.example.routeplanner;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Stack;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CreateRouteInteractFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CreateRouteInteractFragment extends Fragment {
    private TextView mTextViewCalcLength;
    private TextView mTextViewRouteLength;
    private CheckBox cycleCheckBox;
    private Polyline cycleLine;
    private GoogleMap map;

    private ArrayList<Float> routeLength;

    //Info to save to database
    String pos;
    float distance;
    String name;
    boolean isCyclic;

    //Database
    DatabaseReference ref;
    RouteListItem member;

    private CreateRouteActivity parentActivity;

    public static CreateRouteInteractFragment newInstance() {
        CreateRouteInteractFragment fragment = new CreateRouteInteractFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_create_route_interact, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // Reference to database
        ref = FirebaseDatabase.getInstance().getReference().child("RouteListItem");

        //New routelistitem
        member = new RouteListItem();

        mTextViewCalcLength = (TextView) getView().findViewById(R.id.textView_calc_length);
        mTextViewCalcLength.setText("0.0 km");

        mTextViewRouteLength = (TextView) getView().findViewById(R.id.textView_route_length);
        parentActivity = ((CreateRouteActivity) getActivity());

        Button mCreateNewRouteButton = (Button) getView().findViewById(R.id.button_create_route);
        mCreateNewRouteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Implemented: code that generates a new route and stores it
                name = parentActivity.getRouteName();

                distance = parentActivity.updateCalcLengthText();

                Stack<Marker> markerStack = parentActivity.getMarkerStack();

                for (Marker m: markerStack) {
                    pos=pos+m.getPosition().latitude+","+m.getPosition().longitude+";";
                }

                isCyclic = parentActivity.getCycleCheckBox().isChecked();

                member.setPositions(pos);
                member.setCyclic(isCyclic);
                member.setDistance(distance);
                member.setName(name);
                ref.push().setValue(member);


                if (!(markerStack.size() <= 1)) {
                    Toast.makeText(parentActivity, "Route created", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(parentActivity, MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                } else {
                    Toast.makeText(parentActivity, "Route must have at least 2 markers", Toast.LENGTH_SHORT).show();
                }
            }
        });

        Button undoMarkerButton = (Button) getView().findViewById(R.id.button_undo_marker);
        undoMarkerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                map = parentActivity.getMap();

                Stack<Marker> markerStack = parentActivity.getMarkerStack();
                Stack<Polyline> polylineStack = parentActivity.getPolylineStack();

                cycleCheckBox = parentActivity.getCycleCheckBox();
                cycleLine = parentActivity.getCycleLine();

                routeLength = parentActivity.getRouteLength();

                if (markerStack.size() > 1) {

                    // If the checkbox is checked, remove the cycle line,
                    // the last marker, the last line and update the distance
                    if (cycleCheckBox.isChecked()) {
                        cycleLine.remove();
                        markerStack.pop().remove();
                        polylineStack.pop().remove();
                        markerStack.peek().setDraggable(true);

                        // Remove distance of both cycle line and previous line
                        removeDistance();
                        removeDistance();

                        if (markerStack.size() != 1) {
                            cycleLine = map.addPolyline(new PolylineOptions()
                                    .add(markerStack.get(0).getPosition(),markerStack.peek().getPosition())
                                    .width(10)
                                    .color(Color.RED));

                            parentActivity.setCycleLine(cycleLine);

                            float dist = markerStack.size() == 2 ?
                                    parentActivity.getDistanceBetweenMarkers(markerStack.get(0), markerStack.peek()) : 0f;
                            routeLength.add(dist);
                        }
                    } else {
                        if (markerStack.size() > 1) {
                            markerStack.pop().remove();
                            polylineStack.pop().remove();
                            removeDistance();
                            markerStack.peek().setDraggable(true);
                        }
                    }
                } else if (markerStack.size() == 1) {
                    markerStack.pop().remove();
                }
            }
        });

    }

    private void removeDistance() {
        ArrayList<Float> routeLength =  parentActivity.getRouteLength();
        routeLength.remove(routeLength.size()-1);
        parentActivity.updateCalcLengthText();
    }
}