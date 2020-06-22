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
    private boolean isCyclic;

    public RouteListItem(String name, String distance, Stack<Marker> markerStack, boolean isCyclic) {
        this.name = name;
        this.distance = distance;
        this.markerStack = markerStack;
        this.isCyclic = isCyclic;
        avgSpeed = "Route not run yet";
    }

    public RouteListItem() {
        avgSpeed = "Route not run yet";
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

    public Stack<Marker> getMarkerStack() {
        return markerStack;
    }

    public void setMarkerStack(Stack<Marker> markerStack) {
        this.markerStack = markerStack;
    }

    public boolean isCyclic() {
        return isCyclic;
    }

    public void setCyclic(boolean cyclic) {
        isCyclic = cyclic;
    }
}