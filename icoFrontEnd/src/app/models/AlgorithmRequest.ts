import { Coordinate } from "./Coordinate";
import { Order } from "./Order";
import { Vehicle } from "./Vehicle";

export class AlgorithmRequest {
    
    vehicles: Vehicle[] = new Array();
    orders: Order[] = new Array();
    depot!: Coordinate
    
}