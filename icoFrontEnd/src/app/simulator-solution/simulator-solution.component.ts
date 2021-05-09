import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { AlgorithmResponse } from '../models/AlgorithmResponse';

@Component({
  selector: 'simulator-solution',
  templateUrl: './simulator-solution.component.html',
  styleUrls: ['./simulator-solution.component.scss']
})
export class SimulatorSolutionComponent implements OnInit {

	simulatorSolution!: AlgorithmResponse;

	constructor(private route: ActivatedRoute) { }

	ngOnInit(): void {
		this.route.params.subscribe(params => {
			console.log("params: ", params);
			this.simulatorSolution = params.destiniesOrders;
		});
	}

}
