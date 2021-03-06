package com.example.icobackend.models;

import lombok.Data;

import java.util.List;

@Data
public class Vehicle {

    private int capacity;
    private double costPerDistance;
    private List<Coordinate> route;

    public Vehicle(int capacity, double costPerDistance) {
        this.capacity = capacity;
        this.costPerDistance = costPerDistance;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public double getCostPerDistance() {
        return costPerDistance;
    }

    public void setCostPerDistance(double costPerDistance) {
        this.costPerDistance = costPerDistance;
    }

    public List<Coordinate> getRoute() {
        return route;
    }

    public void setRoute(List<Coordinate> route) {
        this.route = route;
    }
}
