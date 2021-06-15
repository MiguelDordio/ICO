package com.tabusearch;

import com.example.icobackend.algorithm.SolutionEvaluator;
import com.example.icobackend.models.AlgorithmRequest;
import com.example.icobackend.models.AlgorithmResponse;
import com.example.icobackend.models.Coordinate;
import com.example.icobackend.models.Order;
import com.graphhopper.jsprit.core.problem.solution.VehicleRoutingProblemSolution;
import com.graphhopper.jsprit.core.problem.solution.route.VehicleRoute;
import com.graphhopper.jsprit.core.problem.solution.route.activity.TourActivity;

import java.util.ArrayList;
import java.util.List;

public class TabuSearchAlgorithm {

    public AlgorithmResponse vrpSearchAlgo(AlgorithmRequest algorithmRequest, boolean printDistanceMatrix) {

        int maxIterations = calculateMaxIterations(algorithmRequest);

        //Problem Parameters
        int NoOfCustomers = algorithmRequest.getOrders().size();
        int NoOfVehicles = algorithmRequest.getVehicles().size();
        int VehicleCap = algorithmRequest.getVehicles().get(0).getCapacity();

        //Depot Coordinates
        int Depot_x = (int) Math.round(algorithmRequest.getDepot().getLat());
        int Depot_y = (int) Math.round(algorithmRequest.getDepot().getLng());

        //Tabu Parameter
        int TABU_Horizon = 10;

        //Initialise
        //Create Random Customers
        Node[] Nodes = new Node[NoOfCustomers + 1];
        Node depot = new Node(Depot_x, Depot_y);

        Nodes[0] = depot;
        for (int i = 1; i <= NoOfCustomers; i++) {
            Order order = algorithmRequest.getOrders().get(i-1);
            Nodes[i] = new Node(i, //Id ) is reserved for depot
                    (int) Math.round(order.getDestiny().getLat()), //Random Cordinates
                    (int) Math.round(order.getDestiny().getLng()),
                    order.getWeight() //Random Demand
            );
        }

        double[][] distanceMatrix = new double[NoOfCustomers + 1][NoOfCustomers + 1];
        double Delta_x, Delta_y;
        for (int i = 0; i < NoOfCustomers; i++) {
            for (int j = i + 1; j < NoOfCustomers; j++) //The table is summetric to the first diagonal
            {                                      //Use this to compute distances in O(n/2)

                Delta_x = (Nodes[i].Node_X - Nodes[j].Node_X);
                Delta_y = (Nodes[i].Node_Y - Nodes[j].Node_Y);

                double distance = Math.sqrt((Delta_x * Delta_x) + (Delta_y * Delta_y));

                distance = Math.round(distance);                //Distance is Casted in Integer
                //distance = Math.round(distance*100.0)/100.0; //Distance in double

                distanceMatrix[i][j] = distance;
                distanceMatrix[j][i] = distance;
            }
        }

        if (printDistanceMatrix){
            for (int i = 0; i <= NoOfCustomers; i++) {
                for (int j = 0; j <= NoOfCustomers; j++) {
                    System.out.print(distanceMatrix[i][j] + "  ");
                }
                System.out.println();
            }
        }

        //Compute the greedy Solution
        System.out.println("Attempting to resolve Vehicle Routing Problem (VRP) for "+NoOfCustomers+
                " Customers and "+NoOfVehicles+" Vehicles"+" with "+VehicleCap + " units of capacity\n");

        Solution s = new Solution(NoOfCustomers, NoOfVehicles, VehicleCap);

        s.GreedySolution(Nodes, distanceMatrix);

        s.TabuSearch(TABU_Horizon, distanceMatrix, maxIterations);

        s.SolutionPrint("Solution After Tabu Search");

        Draw.drawRoutes(s, "TABU_Solution");

        // Calculate algorithm performance
        List<com.example.icobackend.models.Vehicle> vehiclesRoutes = extractRoutePath(s, algorithmRequest);
        double solutionCost = SolutionEvaluator.routeEvaluator(vehiclesRoutes);
        System.out.println(solutionCost);

        return new AlgorithmResponse(vehiclesRoutes, solutionCost);
    }

    private List<com.example.icobackend.models.Vehicle> extractRoutePath(Solution solution, AlgorithmRequest algorithmRequest) {
        List<com.example.icobackend.models.Vehicle> vehiclesRoutes = new ArrayList<>();
        //routePath.add(new Coordinate((int) Math.round(algorithmRequest.getDepot().getLat()), (int) Math.round(algorithmRequest.getDepot().getLng())));
        for (Vehicle v: solution.VehiclesForBestSolution) {
            com.example.icobackend.models.Vehicle vehicle = new com.example.icobackend.models.Vehicle(
                    v.capacity,
                    1);
            List<Coordinate> route = new ArrayList<>();
            for (Node node: v.Route) {
                route.add(new Coordinate(node.Node_X, node.Node_Y));
            }
            vehicle.setRoute(route);
            vehiclesRoutes.add(vehicle);
        }
        //routePath.add(new Coordinate((int) Math.round(algorithmRequest.getDepot().getLat()), (int) Math.round(algorithmRequest.getDepot().getLng())));
        return vehiclesRoutes;
    }

    private int calculateMaxIterations(AlgorithmRequest algorithmRequest) {
        int maxIterations = 0;

        if (algorithmRequest.getOrders().size() <= 10) {
            maxIterations = 1;
        } else if (algorithmRequest.getOrders().size() <= 20) {
            maxIterations = 5;
        } else if (algorithmRequest.getOrders().size() <= 50) {
            maxIterations = 40;
        } else if (algorithmRequest.getOrders().size() <= 100) {
            maxIterations = 75;
        } else if (algorithmRequest.getOrders().size() > 100) {
            maxIterations = 150;
        }

        return maxIterations;
    }
}