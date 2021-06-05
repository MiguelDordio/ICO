package com.example.icobackend.models;

import lombok.Data;

import java.util.List;

@Data
public class Order {

    private Coordinate destiny;
    private int weight;

    public Coordinate getDestiny() {
        return destiny;
    }

    public void setDestiny(Coordinate destiny) {
        this.destiny = destiny;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }
}
