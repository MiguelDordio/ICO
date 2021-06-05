import { Coordinate } from "./Coordinate";

export class Order {

    weight: number;
    destiny: Coordinate;

    constructor(weight: number, destiny: Coordinate) {
        this.weight = weight;
        this.destiny = destiny;
    }
}