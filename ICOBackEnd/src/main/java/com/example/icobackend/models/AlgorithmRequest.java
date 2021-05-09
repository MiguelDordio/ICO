package com.example.icobackend.models;

import lombok.Data;

@Data
public class AlgorithmRequest {

    private int nVehicles;
    private int maxCargo;
    private int nDestinies;

    @Override
    public String toString() {
        return "AlgorithmRequest{" +
                "nVehicles=" + nVehicles +
                ", maxCargo=" + maxCargo +
                ", nDestinies=" + nDestinies +
                '}';
    }
}
