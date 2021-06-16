package com.example.icobackend.algorithm;

import com.example.icobackend.models.*;
import com.graphhopper.jsprit.analysis.toolbox.StopWatch;
import com.tabusearch.TabuSearchAlgorithm;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class PerformanceTests {

    private final static Coordinate DEPOT = new Coordinate(50, 50);

    private final static int VEHICLE_CAPACITY = 200;
    private final static int VEHICLE_COST_PER_DISTANCE = 1;
    private final static int ORDER_WEIGHT = 25;

    public static void main(String[] args) {

        runTest(20, 100, 1, false, true, true);
        runTest(20, 100, 30, false, true, true);
        runTest(20, 100, 50, false, true, true);
        runTest(20, 100, 100, false, true, true);
        runTest(20, 100, 150, false, true, true);


    }

    private static void runTest(int numberOfVehicles, int numberOfOrders, int maxIterations, boolean printDetails
            , boolean runJsprit, boolean runTabu) {

        System.out.println("----- Begin Test ----");
        AlgorithmRequest algorithmRequest = new AlgorithmRequest(DEPOT,
                generateDummyVehicles(numberOfVehicles, VEHICLE_CAPACITY, VEHICLE_COST_PER_DISTANCE),
                generateDummyOrders(numberOfOrders));

        if (runJsprit && runTabu) {
            JspritVRPAlgorithm jspritVRPAlgorithm = new JspritVRPAlgorithm();
            AlgorithmResponse jspritResponse = jspritVRPAlgorithm.simulateManual(algorithmRequest, maxIterations,
                    printDetails);
            System.out.println("Jsprit cost: " + jspritResponse.getSolutionCost());

            StopWatch stopwatch = new StopWatch();
            stopwatch.start();
            TabuSearchAlgorithm tabuSearchAlgorithm = new TabuSearchAlgorithm();
            AlgorithmResponse tabuResponse = tabuSearchAlgorithm.vrpSearchAlgoManual(algorithmRequest, false, maxIterations, printDetails);
            stopwatch.stop();
            System.out.println("TabuSearch cost: " + tabuResponse.getSolutionCost());
            System.out.println("Runtime: " + stopwatch.getCompTimeInSeconds());

            System.out.print("Best performer: ");
            System.out.println(jspritResponse.getSolutionCost() < tabuResponse.getSolutionCost() ? "JSPRIT" : "TABU_SEARCH");
        } else if (runJsprit) {
            JspritVRPAlgorithm jspritVRPAlgorithm = new JspritVRPAlgorithm();
            AlgorithmResponse jspritResponse = jspritVRPAlgorithm.simulateManual(algorithmRequest, maxIterations,
                    printDetails);
            System.out.println("Jsprit cost: " + jspritResponse.getSolutionCost());
        } else if (runTabu) {
            StopWatch stopwatch = new StopWatch();
            stopwatch.start();
            TabuSearchAlgorithm tabuSearchAlgorithm = new TabuSearchAlgorithm();
            AlgorithmResponse tabuResponse = tabuSearchAlgorithm.vrpSearchAlgoManual(algorithmRequest, false, maxIterations, printDetails);
            stopwatch.stop();
            System.out.println("TabuSearch cost: " + tabuResponse.getSolutionCost());
            System.out.println("Runtime: " + stopwatch.getCompTimeInSeconds());
        }

        System.out.println("----- End Test ----");
    }

    public static AlgorithmRequest generateTest(int numberOfVehicles, int numberOfOrders) {
        return new AlgorithmRequest(DEPOT,
                generateDummyVehicles(numberOfVehicles, VEHICLE_CAPACITY, VEHICLE_COST_PER_DISTANCE),
                generateDummyOrders(numberOfOrders));
    }

    private static List<Order> generateDummyOrders(int quantity) {
        List<Order> orderList = new ArrayList<>();
        Random rand = new Random(100);
        for (int i = 0; i < quantity; i++) {

            // Portugal continental varia aprox. 4 graus em latitude e 2 em longitude
            Double randLat = rand.nextDouble()*4;
            Double randLong = rand.nextDouble()*2;
            orderList.add(new Order(new Coordinate(randLat, randLong), ORDER_WEIGHT));

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
