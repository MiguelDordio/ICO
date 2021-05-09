package com.example.icobackend.models;

public class Point {

    private String c1;
    private String c2;
    private int distance;

    public Point(String c1, String c2, int distance) {
        this.c1 = c1;
        this.c2 = c2;
        this.distance = distance;
    }

    public String getC1() {
        return c1;
    }

    public void setC1(String c1) {
        this.c1 = c1;
    }

    public String getC2() {
        return c2;
    }

    public void setC2(String c2) {
        this.c2 = c2;
    }

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }
}
