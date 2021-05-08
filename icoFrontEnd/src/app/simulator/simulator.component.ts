import { Component, OnInit } from '@angular/core';
import { SimulatorApiService } from '../simulator-api.service';

@Component({
  selector: 'simulator',
  templateUrl: './simulator.component.html',
  styleUrls: ['./simulator.component.scss']
})
export class SimulatorComponent implements OnInit {

	apiResponse: any;

	constructor(private simulatorApi: SimulatorApiService) { }

	ngOnInit(): void {
		console.log("Simulator Page");
	}

	simulate() {

		this.simulatorApi.getStuff().subscribe(resp => {
			this.apiResponse = resp;
			console.log("Stuff fetched:", resp);
		});
		console.log("Simulação espetacular!")

		// depois redirecionar para página de resultados
	}

}
