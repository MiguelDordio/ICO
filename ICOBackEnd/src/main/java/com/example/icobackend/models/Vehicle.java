package com.example.icobackend.models;

import lombok.Data;

@Data
public class Vehicle {

    private int capacity;
    private double costPerDistance;
    private String type;

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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
