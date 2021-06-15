package com.example.icobackend.models;

import lombok.Data;

import java.util.List;

@Data
public class AlgorithmResponse {

    private List<Vehicle> vehicleRoutes;
    private double solutionCost;

    public AlgorithmResponse(List<Vehicle> vehicleRoutes, double solutionCost) {
        this.vehicleRoutes = vehicleRoutes;
        this.solutionCost = solutionCost;
    }

    public List<Vehicle> getVehicleRoutes() {
        return vehicleRoutes;
    }

    public void setVehicleRoutes(List<Vehicle> vehicleRoutes) {
        this.vehicleRoutes = vehicleRoutes;
    }

    public double getSolutionCost() {
        return solutionCost;
    }

    public void setSolutionCost(double solutionCost) {
        this.solutionCost = solutionCost;
    }
}
