package com.example.icobackend.models;

import lombok.Data;

@Data
public class AlgorithmRequest {

    private int nVehicles;
    private int maxCargo;
    private double vehicleConsumption;
    private int nDestinies;
    private int demand;

    @Override
    public String toString() {
        return "AlgorithmRequest{" +
                "nVehicles=" + nVehicles +
                ", maxCargo=" + maxCargo +
                ", vehicleConsumption=" + vehicleConsumption +
                ", nDestinies=" + nDestinies +
                ", demand=" + demand +
                '}';
    }
}
