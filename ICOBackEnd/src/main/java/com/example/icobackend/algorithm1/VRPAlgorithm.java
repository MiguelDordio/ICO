package com.example.icobackend.algorithm1;

import com.example.icobackend.models.*;
import com.example.icobackend.models.Vehicle;
import com.graphhopper.jsprit.analysis.toolbox.GraphStreamViewer;
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

public class VRPAlgorithm {

    public AlgorithmResponse simulate(AlgorithmRequest algorithmRequest) {
        VehicleRoutingProblem.Builder vrpBuilder = setupRequest(algorithmRequest);
        return calculateVRP(vrpBuilder);
    }

    private VehicleRoutingProblem.Builder setupRequest(AlgorithmRequest algorithmRequest) {

        VehicleRoutingProblem.Builder vrpBuilder = VehicleRoutingProblem.Builder.newInstance();

        /** Significado de capacidade neste algoritmo
         * Many problems involve multiple capacity dimensions. For example,
         * in many cases vehicles are constrained by their maximum weight AND volume.
         * These are two different dimensions. If you want to take them into account in jsprit,
         * use .addCapacityDimension when specifying your vehicle types. Assign index 0 to weight (e.g. 2700kg)
         * and 1 to volume (e.g. 17m^3) and add their maximum values as follows:
         *
         * VehicleTypeImpl.Builder.newInstance("vehicleType").addCapacityDimension(0, 2700).addCapacityDimension(1,17)
         */

        // Define customers
        for (int i = 0; i < algorithmRequest.getOrders().size(); i++) {
            Order order = algorithmRequest.getOrders().get(i);
            vrpBuilder.addJob(Service.Builder.newInstance(String.valueOf(i))
                    .addSizeDimension(0, order.getWeight())
                    .setLocation(Location.newInstance(i+10, i+40))
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
                    .setStartLocation(Location.newInstance(40, 40))
                    .setType(type).build();

            vrpBuilder.addVehicle(fVehicle);
        }

        return vrpBuilder;
    }

    private AlgorithmResponse calculateVRP(VehicleRoutingProblem.Builder vrpBuilder) {
        //set fleet size finite
        vrpBuilder.setFleetSize(VehicleRoutingProblem.FleetSize.FINITE);

        //build problem
        VehicleRoutingProblem vrp = vrpBuilder.build();

        VehicleRoutingAlgorithm vra = Jsprit.createAlgorithm(vrp);

        // todo
        vra.setMaxIterations(100);
        Collection<VehicleRoutingProblemSolution> solutions = vra.searchSolutions();

        VehicleRoutingProblemSolution best = Solutions.bestOf(solutions);

        SolutionPrinter.print(vrp, best, SolutionPrinter.Print.VERBOSE);
        //new GraphStreamViewer(vrp, best).setRenderDelay(100).display();

        // Calculate algorithm performance
        List<Coordinate> routePath = extractRoutePath(best);
        double solutionCost = SolutionEvaluator.routeEvaluator(routePath);
        System.out.println(solutionCost);

        AlgorithmResponse algorithmResponse = new AlgorithmResponse(routePath, solutionCost);

        return algorithmResponse;
    }


    private List<Coordinate> extractRoutePath(VehicleRoutingProblemSolution solution) {
        List<Coordinate> routePath = new ArrayList<>();
        for (VehicleRoute vehicleRoute: solution.getRoutes()) {

            for (TourActivity ta: vehicleRoute.getActivities()) {
                routePath.add(new Coordinate(ta.getLocation().getCoordinate().getX(), ta.getLocation().getCoordinate().getY()));
            }
        }
        return routePath;
    }
}
