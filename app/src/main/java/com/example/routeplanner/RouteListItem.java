package com.example.routeplanner;

import android.os.Parcel;
import android.os.Parcelable;

public class RouteListItem {

    private String name;
    private float distance;
    private String positions;
    private boolean cyclic;

    public RouteListItem(String name, float distance, String positions, boolean cyclic) {
        this.name = name;
        this.distance = distance;
        this.positions = positions;
        this.cyclic = cyclic;
    }

    public RouteListItem() { }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getDistance() {
        return distance;
    }

    public void setDistance(float distance) {
        this.distance = distance;
    }

    public String getPositions() {
        return positions;
    }

    public void setPositions(String positions) {
        this.positions = positions;
    }

    public boolean isCyclic() {
        return cyclic;
    }

    public void setCyclic(boolean cyclic) {
        this.cyclic = cyclic;
    }
}