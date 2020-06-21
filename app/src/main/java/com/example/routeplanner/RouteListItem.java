package com.example.routeplanner;

import com.google.android.gms.maps.model.Marker;

import java.util.Stack;

public class RouteListItem {

    private int id;
    private String name;
    private String distance;
    private int completions;
    private String avgSpeed;
    private Stack<Marker> markers;
    private boolean cyclic;

    public RouteListItem(String name, String distance, Stack<Marker> markers, boolean cyclic) {
        this.name = name;
        this.distance = distance;
        this.markers = markers;
        this.cyclic = cyclic;
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

    public int getCompletions() {
        return completions;
    }

    public void setCompletions(int completions) {
        this.completions = completions;
    }

    public String getAvgSpeed() {
        return avgSpeed;
    }

    public void setAvgSpeed(String avgSpeed) {
        this.avgSpeed = avgSpeed;
    }

    public boolean isCyclic() {
        return cyclic;
    }

    public void setCyclic(boolean cyclic) { this.cyclic = cyclic; }

    public Stack<Marker> getMarkers() {
        return markers;
    }

    public void setMarkers(Stack<Marker> markers) {
        this.markers = markers;
    }
}
