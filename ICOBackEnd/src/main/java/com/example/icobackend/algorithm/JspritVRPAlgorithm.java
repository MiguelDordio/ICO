package com.example.icobackend.algorithm;

import com.example.icobackend.models.*;
import com.example.icobackend.models.Vehicle;
import com.graphhopper.jsprit.core.algorithm.VehicleRoutingAlgorithm;
import com.graphhopper.jsprit.core.algorithm.box.Jsprit;
import com.graphhopper.jsprit.core.problem.Location;
import com.graphhopper.jsprit.core.problem.VehicleRoutingProblem;
import com.graphhopper.jsprit.core.problem.job.Service;
import com.graphhopper.jsprit.core.problem.solution.VehicleRoutingProblemSolution;
import com.graphhopper.jsprit.core.problem.solution.route.VehicleRoute;
import com.graphhopper.jsprit.core.problem.solution.route.activity.TourActivity;
import com.graphhopper.jsprit.core.problem.vehicle.VehicleImpl;
import com.graphhopper.jsprit.core.problem.vehicle.VehicleType;
import com.graphhopper.jsprit.core.problem.vehicle.VehicleTypeImpl;
import com.graphhopper.jsprit.core.reporting.SolutionPrinter;
import com.graphhopper.jsprit.core.util.Solutions;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class JspritVRPAlgorithm {

    public AlgorithmResponse simulateManual(AlgorithmRequest algorithmRequest, int maxIterations, boolean printDetails) {
        return simulate(algorithmRequest, maxIterations, printDetails);
    }

    public AlgorithmResponse simulateAuto(AlgorithmRequest algorithmRequest) {
        return simulate(algorithmRequest, calculateMaxIterations(algorithmRequest), true);
    }

    public AlgorithmResponse simulate(AlgorithmRequest algorithmRequest, int maxIterations, boolean printDetails) {
        System.out.println("----- JSPRIT -----");
        VehicleRoutingProblem.Builder vrpBuilder = setupRequest(algorithmRequest);
        return calculateVRP(vrpBuilder, algorithmRequest, maxIterations, printDetails);
    }

    /**
     * Significado de capacidade neste algoritmo
     * Many problems involve multiple capacity dimensions. For example,
     * in many cases vehicles are constrained by their maximum weight AND volume.
     * These are two different dimensions. If you want to take them into account in jsprit,
     * use .addCapacityDimension when specifying your vehicle types. Assign index 0 to weight (e.g. 2700kg)
     * and 1 to volume (e.g. 17m^3) and add their maximum values as follows:
     *
     * VehicleTypeImpl.Builder.newInstance("vehicleType").addCapacityDimension(0, 2700).addCapacityDimension(1,17)
     */
    private VehicleRoutingProblem.Builder setupRequest(AlgorithmRequest algorithmRequest) {

        VehicleRoutingProblem.Builder vrpBuilder = VehicleRoutingProblem.Builder.newInstance();

        // Define customers
        for (int i = 0; i < algorithmRequest.getOrders().size(); i++) {
            Order order = algorithmRequest.getOrders().get(i);
            vrpBuilder.addJob(Service.Builder.newInstance(String.valueOf(i))
                    .addSizeDimension(0, order.getWeight())
                    .setLocation(Location.newInstance((int) Math.round(order.getDestiny().getLat()), (int) Math.round(order.getDestiny().getLng())))
                    .build());
        }

        // Define the vehicles
        for (int i = 0; i < algorithmRequest.getVehicles().size(); i++) {
            Vehicle vehicle = algorithmRequest.getVehicles().get(i);
            String vehicleType = "type_" + i;
            VehicleType type = VehicleTypeImpl.Builder.newInstance(vehicleType)
                    .addCapacityDimension(0, vehicle.getCapacity())
                    .setCostPerDistance(1.0)
                    .build();

            VehicleImpl fVehicle = VehicleImpl.Builder.newInstance(String.valueOf(i))
                    .setStartLocation(Location.newInstance((int) Math.round(algorithmRequest.getDepot().getLat()), (int) Math.round(algorithmRequest.getDepot().getLng())))
                    .setType(type).build();

            vrpBuilder.addVehicle(fVehicle);
        }

        return vrpBuilder;
    }

    private AlgorithmResponse calculateVRP(VehicleRoutingProblem.Builder vrpBuilder, AlgorithmRequest algorithmRequest, int maxIterarions, boolean printDetails) {
        //set fleet size finite
        vrpBuilder.setFleetSize(VehicleRoutingProblem.FleetSize.FINITE);

        //build problem
        VehicleRoutingProblem vrp = vrpBuilder.build();

        VehicleRoutingAlgorithm vra = Jsprit.createAlgorithm(vrp);

        vra.setMaxIterations(maxIterarions);
        Collection<VehicleRoutingProblemSolution> solutions = vra.searchSolutions();

        VehicleRoutingProblemSolution best = Solutions.bestOf(solutions);

        System.out.println("JSPRIT Cost: " + best.getCost());
        System.out.println("---- JSPRIT END -----");

        if (printDetails)
            SolutionPrinter.print(vrp, best, SolutionPrinter.Print.VERBOSE);
        //new GraphStreamViewer(vrp, best).setRenderDelay(100).display();

        // Calculate algorithm performance
        List<Vehicle> vehiclesRoutes = extractRoutePath(best, algorithmRequest);
        double solutionCost = SolutionEvaluator.routeEvaluator(vehiclesRoutes);

        return new AlgorithmResponse(vehiclesRoutes, solutionCost);
    }


    private List<Vehicle> extractRoutePath(VehicleRoutingProblemSolution solution, AlgorithmRequest algorithmRequest) {
        List<Vehicle> vehiclesRoutes = new ArrayList<>();
        for (VehicleRoute vehicleRoute: solution.getRoutes()) {
            Vehicle vehicle = new Vehicle(
                    vehicleRoute.getVehicle().getType().getCapacityDimensions().get(1),
                    vehicleRoute.getVehicle().getType().getVehicleCostParams().perDistanceUnit);
            List<Coordinate> route = new ArrayList<>();
            route.add(new Coordinate((int) Math.round(algorithmRequest.getDepot().getLat()), (int) Math.round(algorithmRequest.getDepot().getLng())));
            for (TourActivity ta: vehicleRoute.getActivities()) {
                route.add(new Coordinate(ta.getLocation().getCoordinate().getX(), ta.getLocation().getCoordinate().getY()));
            }
            route.add(new Coordinate((int) Math.round(algorithmRequest.getDepot().getLat()), (int) Math.round(algorithmRequest.getDepot().getLng())));
            vehicle.setRoute(route);
            vehiclesRoutes.add(vehicle);
        }
        return vehiclesRoutes;
    }

    private int calculateMaxIterations(AlgorithmRequest algorithmRequest) {
        int maxIterations = 0;

        if (algorithmRequest.getOrders().size() <= 10) {
            maxIterations = 5;
        } else if (algorithmRequest.getOrders().size() <= 20) {
            maxIterations = 10;
        } else if (algorithmRequest.getOrders().size() <= 50) {
            maxIterations = 100;
        } else if (algorithmRequest.getOrders().size() <= 100) {
            maxIterations = 100;
        } else if (algorithmRequest.getOrders().size() > 100) {
            maxIterations = 150;
        }

        return maxIterations;
    }
}
