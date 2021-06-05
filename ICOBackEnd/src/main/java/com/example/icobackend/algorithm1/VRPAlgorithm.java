package com.example.icobackend.algorithm1;

import com.example.icobackend.models.AlgorithmRequest;
import com.example.icobackend.models.Coordinate;
import com.example.icobackend.models.Order;
import com.example.icobackend.models.Vehicle;
import com.graphhopper.jsprit.analysis.toolbox.GraphStreamViewer;
import com.graphhopper.jsprit.core.algorithm.VehicleRoutingAlgorithm;
import com.graphhopper.jsprit.core.algorithm.box.Jsprit;
import com.graphhopper.jsprit.core.problem.Location;
import com.graphhopper.jsprit.core.problem.VehicleRoutingProblem;
import com.graphhopper.jsprit.core.problem.job.Service;
import com.graphhopper.jsprit.core.problem.solution.VehicleRoutingProblemSolution;
import com.graphhopper.jsprit.core.problem.vehicle.VehicleImpl;
import com.graphhopper.jsprit.core.problem.vehicle.VehicleType;
import com.graphhopper.jsprit.core.problem.vehicle.VehicleTypeImpl;
import com.graphhopper.jsprit.core.reporting.SolutionPrinter;
import com.graphhopper.jsprit.core.util.Solutions;

import java.util.Collection;
import java.util.List;

public class VRPAlgorithm {

    public void simulate(AlgorithmRequest algorithmRequest) {

        VehicleRoutingProblem.Builder vrpBuilder = setupRequest(algorithmRequest);
        calculateVRP(vrpBuilder);

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
                    .setLocation(Location.newInstance(order.getDestiny().getLat(), order.getDestiny().getLng()))
                    .build());
        }

        // Define the vehicles
        for (int i = 0; i < algorithmRequest.getVehicles().size(); i++) {
            Vehicle vehicle = algorithmRequest.getVehicles().get(i);
            VehicleType type = VehicleTypeImpl.Builder.newInstance(vehicle.getType())
                    .addCapacityDimension(0, vehicle.getCapacity())
                    .setCostPerDistance(1.0)
                    .build();

            VehicleImpl fVehicle = VehicleImpl.Builder.newInstance(String.valueOf(i))
                    .setStartLocation(Location.newInstance(algorithmRequest.getDepot().getLat(), algorithmRequest.getDepot().getLng()))
                    .setType(type).build();

            vrpBuilder.addVehicle(fVehicle);
        }

        return vrpBuilder;
    }

    private void calculateVRP(VehicleRoutingProblem.Builder vrpBuilder) {
        //set fleet size finite
        vrpBuilder.setFleetSize(VehicleRoutingProblem.FleetSize.FINITE);

        //build problem
        VehicleRoutingProblem vrp = vrpBuilder.build();

        VehicleRoutingAlgorithm vra = Jsprit.createAlgorithm(vrp);
        Collection<VehicleRoutingProblemSolution> solutions = vra.searchSolutions();

        VehicleRoutingProblemSolution best = Solutions.bestOf(solutions);

        SolutionPrinter.print(vrp, best, SolutionPrinter.Print.VERBOSE);

        new GraphStreamViewer(vrp, best).setRenderDelay(100).display();
    }
}
