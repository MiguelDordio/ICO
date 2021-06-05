export class Vehicle {
    
    capacity: number;
    costPerDistance: number;
    type: string;

    constructor(capacity: number, costPerDistance: number, type: string) {
        this.capacity = capacity;
        this.costPerDistance = costPerDistance;
        this.type = type;
    }
}