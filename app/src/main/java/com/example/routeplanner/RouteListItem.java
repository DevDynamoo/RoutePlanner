package com.example.routeplanner;

import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.Polyline;

import java.util.Stack;

public class RouteListItem {

    private int id;
    private String name;
    private String distance;
    private String completions;
    private String avgSpeed;

    private Stack<Marker> markerStack = new Stack<Marker>();
    private Stack<Polyline> polylineStack = new Stack<Polyline>();
    private Polyline cycleLine;

    public RouteListItem(String name, String distance, String completions, String avgSpeed) {
        this.name = name;
        this.distance = distance;
        this.completions = completions;
        this.avgSpeed = avgSpeed;
    }

    public Stack<Marker> getMarkerStack() {
        return markerStack;
    }

    public void setMarkerStack(Stack<Marker> markerStack) {
        this.markerStack = markerStack;
    }

    public Stack<Polyline> getPolylineStack() {
        return polylineStack;
    }

    public void setPolylineStack(Stack<Polyline> polylineStack) {
        this.polylineStack = polylineStack;
    }

    public Polyline getCycleLine() {
        return cycleLine;
    }

    public void setCycleLine(Polyline cycleLine) {
        this.cycleLine = cycleLine;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getCompletions() {
        return completions;
    }

    public void setCompletions(String completions) {
        this.completions = completions;
    }

    public String getAvgSpeed() {
        return avgSpeed;
    }

    public void setAvgSpeed(String avgSpeed) {
        this.avgSpeed = avgSpeed;
    }
}
