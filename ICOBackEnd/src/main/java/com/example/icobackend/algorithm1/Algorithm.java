package com.example.icobackend.algorithm1;

import com.example.icobackend.models.*;

import java.util.ArrayList;
import java.util.List;

public class Algorithm {

    private Point start = new Point("Lisboa", "Lisboa", 0);
    private Point[][] map = new Point[4][4];

    private AlgorithmRequest request;

    public Algorithm(AlgorithmRequest request) {
        initializeMap();
        this.request = request;
    }

    private void initializeMap() {
        // Lisboa
        map[0][0] = new Point("Lisboa", "Lisboa", 0);
        map[0][1] = new Point("Lisboa", "Evora", 50);
        map[0][2] = new Point("Lisboa", "Porto", 100);
        map[0][3] = new Point("Lisboa", "Setubal", 20);

        // Evora
        map[1][0] = new Point("Evora", "Lisboa", 50);
        map[1][1] = new Point("Evora", "Evora", 0);
        map[1][2] = new Point("Evora", "Porto", 130);
        map[1][3] = new Point("Evora", "Setubal", 30);

        // Porto
        map[2][0] = new Point("Porto", "Lisboa", 100);
        map[2][1] = new Point("Porto", "Evora", 130);
        map[2][2] = new Point("Porto", "Porto", 0);
        map[2][3] = new Point("Porto", "Setubal", 120);

        // Setubal
        map[3][0] = new Point("Setubal", "Lisboa", 20);
        map[3][1] = new Point("Setubal", "Evora", 30);
        map[3][2] = new Point("Setubal", "Porto", 120);
        map[3][3] = new Point("Setubal", "Setubal", 0);
    }

    public void simulate() {

        Point current = start;
        Vehicle v = request.getVehicles().get(0);
        while (request.getOrders().size() > 0) {
            Point c2 = closestOrder(current).getP();
            double capacity = v.getCapacity();
            double time = getTime(current, c2);
            int i = 0;
            v.addToRoute(current);
            List<Order> clients_encomenda_sem_capacidade = new ArrayList<>();
            while ((time  + getTimeToStart(c2) <= 8) && (capacity > 0) && (request.getOrders().size() > 0)) {
                i++;
                v.addToRoute(c2);
                // Elimina encomenda em uso
                request.getOrders().remove(0);
                if (request.getOrders().size() > 0) {
                    current = c2;
                    Order e = closestOrder(current).getE();
                    c2 = closestOrder(current).getP();
                    boolean hasTime = true;

                    // Lidar com casos em que a encomenda ultrapassa a capacidade disponivel do camião
                    if (capacity - e.getWeight() < 0) {
                        clients_encomenda_sem_capacidade.add(e);
                        boolean orderFound = false;
                        while (request.getOrders().size() - clients_encomenda_sem_capacidade.size() > 0
                            && hasTime && orderFound) {
                            c2 = closestValidOrder().getP();
                            e = closestValidOrder().getE();
                            if (capacity - e.getWeight() >= 0) {
                                if (time + getTime(current, c2) + getTimeToStart(c2) <= 8) {
                                    orderFound = true;
                                    time += getTime(current, c2);
                                    capacity -= e.getWeight();
                                } else {
                                    hasTime = false;
                                    time = 8;
                                }
                            } else {
                                clients_encomenda_sem_capacidade.add(e);
                            }

                        }

                        // Quando a encomenda cabe
                    }
                    if (time + getTime(current, c2) + getTimeToStart(c2) <= 8) {
                        time += getTime(current, c2);
                        capacity -= e.getWeight();
                    }
                }
            }
        }
    }

    private double getTimeToStart(Point p) {
        return 0.25;
    }

    private Pair closestOrder(Point p) {
        Pair pair = new Pair();
        for (Order order: request.getOrders()) {
            //TODO
        }
        return pair;
    }

    private Pair closestValidOrder() {
        Pair pair = new Pair();
        return pair;
    }

    private int getDistance(Point s, Point e) {
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map.length; j++) {
                if (s.getC1().equals(e.getC1()))
                    return s.getDistance();
            }
        }
        return 0;
    }

    private double getTime(Point s, Point e) {
        return getDistance(s, e) % 2 + 5;
    }
}
