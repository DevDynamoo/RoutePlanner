package com.example.routeplanner;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
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

                Stack<Marker> markerStack = ((CreateRouteActivity) getActivity()).getMarkerStack();
                Stack<Polyline> polylineStack = ((CreateRouteActivity) getActivity()).getPolylineStack();

                if (markerStack.size() >= 2) {
                    LatLng lastPosition = markerStack.get(markerStack.size()-1).getPosition();
                    LatLng secondLastPosition = markerStack.get(markerStack.size()-2).getPosition();
                    removeDistance(lastPosition, secondLastPosition);
                }

                if (!markerStack.isEmpty() && !polylineStack.isEmpty()) {
                    markerStack.pop().remove();
                    polylineStack.pop().remove();
                    markerStack.peek().setDraggable(true);
                }
            }
        });
    }

    private void removeDistance(LatLng lastPosition, LatLng secondLastPosition) {
        ArrayList<Float> routeLength =  ((CreateRouteActivity) getActivity()).getRouteLength();
        routeLength.remove(routeLength.size()-1);
        ((CreateRouteActivity) getActivity()).updateCalcLengthText();
    }
}