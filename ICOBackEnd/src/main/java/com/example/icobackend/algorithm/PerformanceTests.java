package com.example.icobackend.algorithm;

import com.example.icobackend.models.*;
import com.tabusearch.TabuSearchAlgorithm;

import java.util.ArrayList;
import java.util.List;

public class PerformanceTests {

    private final static Coordinate DEPOT = new Coordinate(50, 50);

    private final static int VEHICLE_CAPACITY = 200;
    private final static int VEHICLE_COST_PER_DISTANCE = 1;
    private final static int ORDER_WEIGHT = 25;

    public static void main(String[] args) {

        runTest(2, 10);
        //runTest(4, 20);
        //runTest(10, 50);
    }

    private static void runTest(int numberOfVehicles, int numberOfOrders) {

        AlgorithmRequest algorithmRequest = new AlgorithmRequest(DEPOT,
                generateDummyVehicles(numberOfVehicles, VEHICLE_CAPACITY, VEHICLE_COST_PER_DISTANCE),
                generateDummyOrders(numberOfOrders));

        JspritVRPAlgorithm jspritVRPAlgorithm = new JspritVRPAlgorithm();
        AlgorithmResponse jspritResponse = jspritVRPAlgorithm.simulate(algorithmRequest);

        TabuSearchAlgorithm tabuSearchAlgorithm = new TabuSearchAlgorithm();
        AlgorithmResponse tabuResponse = tabuSearchAlgorithm.tabuSearchAlgo(algorithmRequest);

        System.out.println("Jsprit cost: " + jspritResponse.getSolutionCost());
        System.out.println("TabuSearch cost: " + tabuResponse.getSolutionCost());

        AlgorithmResponse finalResponse = jspritResponse.getSolutionCost() > tabuResponse.getSolutionCost() ? jspritResponse : tabuResponse;
        System.out.println(finalResponse.getRoutes());
    }

    private static List<Order> generateDummyOrders(int quantity) {
        List<Order> orderList = new ArrayList<>();

        for (int i = 0; i < quantity; i++) {
            orderList.add(new Order(new Coordinate(i + 50, i + 20), ORDER_WEIGHT));
        }

        return orderList;
    }

    private static List<Vehicle> generateDummyVehicles(int quantity, int capacity, int costPerDistance) {
        List<Vehicle> vehicleList = new ArrayList<>();

        for (int i = 0; i < quantity; i++) {
            vehicleList.add(new Vehicle(capacity, costPerDistance));
        }

        return vehicleList;
    }
}
