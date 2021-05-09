export class AlgorithmRequest {
    
    nVehicles: number;
    maxCargo: number;
    vehicleConsumption: number;
    nDestinies: number;

    constructor(nVehicles: number, maxCargo: number, vehicleConsumption: number, nDestinies: number) {
        this.nVehicles = nVehicles;
        this.maxCargo = maxCargo;
        this.vehicleConsumption = vehicleConsumption;
        this.nDestinies = nDestinies;
    }
}