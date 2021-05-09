export class AlgorithmRequest {
    
    nVehicles: number;
    maxCargo: number;
    nDestinies: number;

    constructor(nVehicles: number, maxCargo: number, nDestinies: number) {
        this.nVehicles = nVehicles;
        this.maxCargo = maxCargo;
        this.nDestinies = nDestinies;
    }
}