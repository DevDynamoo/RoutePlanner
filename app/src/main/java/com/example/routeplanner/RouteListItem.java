package com.example.routeplanner;

public class RouteListItem {
    private String name;
    private String distance;
    private String completions;
    private String avgSpeed;

    public RouteListItem(String name, String distance, String completions, String avgSpeed) {
        this.name = name;
        this.distance = distance;
        this.completions = completions;
        this.avgSpeed = avgSpeed;
    }

    public String getName() {
        return name;
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