package com.example.routeplanner;

public class RouteListItem {
    private String name;
    private String distance;
    private String completions;
    private String avgSpeed;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public void setCompletions(String completions) {
        this.completions = completions;
    }

    public void setAvgSpeed(String avgSpeed) {
        this.avgSpeed = avgSpeed;
    }

    public String getDistance() {
        return distance;
    }

    public String getCompletions() {
        return completions;
    }

    public String getAvgSpeed() {
        return avgSpeed;
    }
}