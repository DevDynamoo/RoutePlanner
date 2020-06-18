package com.example.routeplanner;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class RouteListItem {
    @PrimaryKey
    private int id;

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
