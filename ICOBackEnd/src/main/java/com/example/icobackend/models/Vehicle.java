package com.example.icobackend.models;

import java.util.List;

public class Vehicle {

    private double capacity;
    private double fuelConsumption;
    private List<Point> route;

    public double getCapacity() {
        return capacity;
    }

    public void setCapacity(double capacity) {
        this.capacity = capacity;
    }

    public double getFuelConsumption() {
        return fuelConsumption;
    }

    public void setFuelConsumption(double fuelConsumption) {
        this.fuelConsumption = fuelConsumption;
    }

    public List<Point> getRoute() {
        return route;
    }

    public void setRoute(List<Point> route) {
        this.route = route;
    }

    public void addToRoute(Point p) {
        this.route.add(p);
    }
}
