package com.example.routeplanner;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.Polyline;

import java.util.ArrayList;
import java.util.Stack;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CreateRouteInteractFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CreateRouteInteractFragment extends Fragment {
    TextView mTextViewCalcLength;
    TextView mTextViewRouteLength;
    CheckBox cycleCheckBox;
    Polyline cycleLine;

    ArrayList<Float> routeLength;

    CreateRouteActivity parentActivity;

    public static CreateRouteInteractFragment newInstance() {
        CreateRouteInteractFragment fragment = new CreateRouteInteractFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        parentActivity = ((CreateRouteActivity) getActivity());
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
        mTextViewCalcLength = (TextView) getView().findViewById(R.id.textView_calc_length);
        mTextViewCalcLength.setText("0.0 km");

        mTextViewRouteLength = (TextView) getView().findViewById(R.id.textView_route_length);

        Button mCreateNewRouteButton = (Button) getView().findViewById(R.id.button_create_route);
        mCreateNewRouteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO Implement code that generates a new route and stores it
            }
        });

        Button undoMarkerButton = (Button) getView().findViewById(R.id.button_undo_marker);
        undoMarkerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Stack<Marker> markerStack = parentActivity.getMarkerStack();
                Stack<Polyline> polylineStack = parentActivity.getPolylineStack();

                cycleCheckBox = parentActivity.getCycleCheckBox();
                cycleLine = parentActivity.getCycleLine();

                routeLength = parentActivity.getRouteLength();

                if (markerStack.size() >= 2) {
//                    if (cycleCheckBox.isChecked()) {
//                        cycleLine.remove();
//                        routeLength.remove(routeLength.size()-1);
//                        parentActivity.updateCalcLengthText();
//                    }

//                    parentActivity.getRouteLength().remove(routeLength.size()-1);
//                    parentActivity.updateCalcLengthText();

                    LatLng lastPosition = markerStack.peek().getPosition();
                    LatLng secondLastPosition = markerStack.get(markerStack.size()-2).getPosition();
                    removeDistance(lastPosition, secondLastPosition);

                }

                if (!markerStack.isEmpty() && !polylineStack.isEmpty()) {
                    markerStack.pop().remove();
                    polylineStack.pop().remove();
                    markerStack.peek().setDraggable(true);
                }

                if (cycleCheckBox.isChecked()) {
                    cycleLine.remove();
                    //routeLength.remove(routeLength.size()-1);

                    parentActivity.createCycleLineBetweenFirstAndLast();
                    float cycleDist = parentActivity.getDistanceBetweenMarkers(markerStack.get(0), markerStack.peek());
                    routeLength.add(cycleDist);

                    parentActivity.updateCalcLengthText();

                }
            }
        });
    }

    private void removeDistance(LatLng lastPosition, LatLng secondLastPosition) {
        ArrayList<Float> routeLength =  parentActivity.getRouteLength();
        routeLength.remove(routeLength.size()-1);
        parentActivity.updateCalcLengthText();
    }
}