package com.example.routeplanner;

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

    public String getName() {
        return name;
    }

    public float getDistance() {
        return distance;
    }

    public String getPositions() {
        return positions;
    }

    public boolean isCyclic() {
        return cyclic;
    }
}