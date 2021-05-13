package com.example.icobackend.models;

import lombok.Data;

import java.util.List;

@Data
public class AlgorithmRequest {

    private List<Coordinate> clientsCoordinates;
    private List<Vehicle> vehicles;
    private List<Order> orders;
    // dias de entrega
}
