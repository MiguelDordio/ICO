package com.example.icobackend.algorithm;

import com.example.icobackend.models.Coordinate;
import com.example.icobackend.models.Vehicle;

import java.util.List;

public class SolutionEvaluator {

    public static double routeEvaluator(List<Vehicle> vehicleRoutes) {

        double cost = 0;

        for (Vehicle vehicle: vehicleRoutes) {
            for (int i = 0; i < vehicle.getRoute().size(); i++) {
                Coordinate location = vehicle.getRoute().get(i);
                if (i+1 < vehicle.getRoute().size()) {
                    cost += Coordinate.distance(
                            location.getLat(), location.getLng(), // current point
                            vehicle.getRoute().get(i+1).getLat(), vehicle.getRoute().get(i+1).getLng(), // next point
                            0, 0);
                }
            }
        }

        return cost;
    }
}
