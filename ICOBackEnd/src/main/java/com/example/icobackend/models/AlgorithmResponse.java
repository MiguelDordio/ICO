package com.example.icobackend.models;

import java.util.List;

public class AlgorithmResponse {

    private List<String> destiniesOrders;

    public AlgorithmResponse(List<String> destiniesOrders) {
        this.destiniesOrders = destiniesOrders;
    }

    public List<String> getDestiniesOrders() {
        return destiniesOrders;
    }

    public void setDestiniesOrders(List<String> destiniesOrders) {
        this.destiniesOrders = destiniesOrders;
    }
}
