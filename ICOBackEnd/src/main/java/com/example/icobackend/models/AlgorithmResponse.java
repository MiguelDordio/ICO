package com.example.icobackend.models;

import java.util.List;

public class AlgorithmResponse {

    private List<Coordinate> routes;
    private double solutionCost;

    public AlgorithmResponse(List<Coordinate> routes, double solutionCost) {
        this.routes = routes;
        this.solutionCost = solutionCost;
    }

    public List<Coordinate> getRoutes() {
        return routes;
    }

    public void setRoutes(List<Coordinate> routes) {
        this.routes = routes;
    }

    public double getSolutionCost() {
        return solutionCost;
    }

    public void setSolutionCost(double solutionCost) {
        this.solutionCost = solutionCost;
    }
}
