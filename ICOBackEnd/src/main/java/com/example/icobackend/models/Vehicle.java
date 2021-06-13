package com.example.icobackend.models;

import lombok.Data;

@Data
public class Vehicle {

    private int capacity;
    private double costPerDistance;

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
}
