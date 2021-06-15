import { Coordinate } from "./Coordinate";

export class Vehicle {
    
    capacity: number;
    costPerDistance: number;
    route!: Coordinate[];

    constructor(capacity: number, costPerDistance: number) {
        this.capacity = capacity;
        this.costPerDistance = costPerDistance;
    }
}