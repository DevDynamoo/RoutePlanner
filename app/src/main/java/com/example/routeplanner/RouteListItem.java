package com.example.routeplanner;

import com.google.android.gms.maps.model.Marker;

import java.io.Serializable;
import java.util.Stack;

public class RouteListItem implements Serializable {

    private String name;
    private float distance;
    private String postions;
    private boolean cyclic;

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

    public String getPostions() {
        return postions;
    }

    public void setPostions(String postions) {
        this.postions = postions;
    }

    public boolean isCyclic() {
        return cyclic;
    }

    public void setCyclic(boolean cyclic) {
        this.cyclic = cyclic;
    }
}