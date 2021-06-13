package com.example.icobackend.algorithm1;

import com.example.icobackend.models.Coordinate;

import java.util.List;

public class SolutionEvaluator {

    public static double routeEvaluator(List<Coordinate> routePath) {

        double cost = 0;

        for (int i = 0; i < routePath.size(); i++) {
            if (i+1 < routePath.size()) {
                cost += Coordinate.distance(
                        routePath.get(i).getLat(), routePath.get(i).getLng(), // current point
                        routePath.get(i+1).getLat(), routePath.get(i+1).getLng(), // next point
                        0, 0);
            }
        }

        return cost;
    }
}
