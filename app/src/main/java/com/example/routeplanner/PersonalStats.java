package com.example.routeplanner;

public class PersonalStats {
        private float totalspeed;
        private int num;
        private float totaldistance;
        private int time;

    public PersonalStats(float totalspeed, int num, float totaldistance, int time) {
        this.totalspeed = totalspeed;
        this.num = num;
        this.totaldistance = totaldistance;
        this.time = time;
    }

    public float getTotalspeed() {
        return totalspeed;
    }

    public int getNum() {
        return num;
    }

    public float getTotaldistance() {
        return totaldistance;
    }

    public int getTime() {
        return time;
    }

}

