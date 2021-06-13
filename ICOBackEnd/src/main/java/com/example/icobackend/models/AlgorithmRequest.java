package com.example.icobackend.models;

import lombok.Data;

import java.util.List;

@Data
public class AlgorithmRequest {

    private Coordinate depot;
    private List<Vehicle> vehicles;
    private List<Order> orders;

    public AlgorithmRequest(Coordinate depot, List<Vehicle> vehicles, List<Order> orders) {
        this.depot = depot;
        this.vehicles = vehicles;
        this.orders = orders;
    }
}
