import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'simulator',
  templateUrl: './simulator.component.html',
  styleUrls: ['./simulator.component.scss']
})
export class SimulatorComponent implements OnInit {

	constructor() { }

	ngOnInit(): void {
		console.log("Simulator Page");
	}

}
