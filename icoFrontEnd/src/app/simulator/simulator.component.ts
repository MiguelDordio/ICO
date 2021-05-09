import { Component, OnInit } from '@angular/core';
import { AlgorithmRequest } from '../models/AlgorithmRequest';
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
	nDestinies!: number;

	constructor(private simulatorApi: SimulatorApiService) { }

	ngOnInit(): void {
		console.log("Simulator Page");
	}

	simulate() {
		let request: AlgorithmRequest = new AlgorithmRequest(this.nVehicles, this.maxCargo, this.nDestinies);
		this.simulatorApi.simulate(request).subscribe(resp => {
			this.apiResponse = resp;
			console.log("Solution fetched:", resp);
		});
		

		// depois redirecionar para página de resultados
	}

}
