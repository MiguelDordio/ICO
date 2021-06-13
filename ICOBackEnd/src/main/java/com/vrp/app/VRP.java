package com.vrp.app;

import com.example.icobackend.algorithm1.SolutionEvaluator;
import com.example.icobackend.models.AlgorithmRequest;
import com.example.icobackend.models.AlgorithmResponse;
import com.example.icobackend.models.Coordinate;
import com.vrp.app.components.Node;
import com.vrp.app.components.Route;
import com.vrp.app.components.Solution;
import com.vrp.app.solvers.NearestNeighbor;
import com.vrp.app.solvers.TabuSearch;
import com.vrp.app.utils.Printer;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class VRP {
    public static final boolean DEBUG_ROUTES = false;
    private static int numberOfCustomers = 30;
    private static int numberOfVehicles = 10;
    private static final boolean PRINT = true;

    public AlgorithmResponse tabuSearchAlgo(AlgorithmRequest algorithmRequest) {

        numberOfCustomers = algorithmRequest.getOrders().size();
        numberOfVehicles = algorithmRequest.getVehicles().size();

        Solution finalSolution;
        String solverName = "";

        //setArgs(args);

        ArrayList<Node> allNodes = initCustomers(numberOfCustomers, algorithmRequest);
        Node depot = allNodes.get(0);

        double[][] distanceMatrix = initDistanceMatrix(allNodes);

        NearestNeighbor nearestNeighbor = new NearestNeighbor(numberOfCustomers, numberOfVehicles, depot, distanceMatrix, allNodes);
        nearestNeighbor.run(algorithmRequest);

        TabuSearch tabuSearch = new TabuSearch(numberOfVehicles, numberOfCustomers, allNodes, depot, distanceMatrix);
        tabuSearch.setSolution(nearestNeighbor.getSolution());
        tabuSearch.run(algorithmRequest);
        finalSolution = tabuSearch.getSolution();
        solverName = "\t TABU Search ";

        if (PRINT) {
            Printer.printResults(numberOfVehicles, finalSolution, solverName);
        }

        List<Coordinate> routePath = extractRoutePath(finalSolution);
        double solutionCost = SolutionEvaluator.routeEvaluator(routePath);

        AlgorithmResponse algorithmResponse = new AlgorithmResponse(routePath, solutionCost);

        return algorithmResponse;
    }

    private static double[][] initDistanceMatrix(ArrayList<Node> allNodes) {
        double[][] distanceMatrix = new double[allNodes.size()][allNodes.size()];
        for (int i = 0; i < allNodes.size(); i++) {
            Node from = allNodes.get(i);

            for (int j = 0; j < allNodes.size(); j++) {
                Node to = allNodes.get(j);

                double Delta_x = (from.getX() - to.getX());
                double Delta_y = (from.getY() - to.getY());
                double distance = Math.sqrt((Delta_x * Delta_x) + (Delta_y * Delta_y));

                distance = Math.round(distance);

                distanceMatrix[i][j] = distance;
            }
        }
        return distanceMatrix;
    }

    private static ArrayList<Node> initCustomers(int numberOfCustomers, AlgorithmRequest algorithmRequest) {
        ArrayList<Node> allNodes = new ArrayList<>();

        Node depot = new Node((int) algorithmRequest.getDepot().getLat(), (int) algorithmRequest.getDepot().getLng(), 0);
        depot.setRouted(true);
        allNodes.add(depot);

        Random ran = new Random(150589);

        for (int i = 1; i <= numberOfCustomers; i++) {
            int x = ran.nextInt(100);
            int y = ran.nextInt(100);
            int demand = algorithmRequest.getOrders().get(i).getWeight();
            Node customer = new Node(x, y, i, demand);
            customer.setRouted(false);
            allNodes.add(customer);
        }

        return allNodes;
    }

    private static List<Coordinate> extractRoutePath(Solution solution) {
        List<Coordinate> routePath = new ArrayList<>();
        for (Route route: solution.getRoute()) {
            for (Node node: route.getNodes()) {
                routePath.add(new Coordinate(node.getX(), node.getY()));
            }
        }
        return routePath;
    }
}
