import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { AlgorithmRequest } from '../models/AlgorithmRequest';
import { PagesRoute } from '../models/routingPaths';
import { SimulatorApiService } from '../simulator-api.service';

@Component({
  selector: 'simulator',
  templateUrl: './simulator.component.html',
  styleUrls: ['./simulator.component.scss']
})
export class SimulatorComponent implements OnInit {

	apiResponse: any;
	nVehicles!: number;
	maxCargo!: number;
	vehicleConsumption!: number;
	nDestinies!: number;
	demand!: number;

	constructor(private simulatorApi: SimulatorApiService, private route: ActivatedRoute, private router: Router) { }

	ngOnInit(): void {
		console.log("Simulator Page");
	}

	simulate() {
		let request: AlgorithmRequest = new AlgorithmRequest(this.nVehicles, this.maxCargo, this.vehicleConsumption, this.nDestinies, this.demand);
		this.simulatorApi.simulate(request).subscribe(resp => {
			this.apiResponse = resp;
			console.log("Solution fetched:", resp);
			this.goToSolutionDisplay()
		});
	}

	goToSolutionDisplay() {
		this.router.navigate(["/" + PagesRoute.SIMULATOR_SOLUTION, this.apiResponse]);
	}
}
