import { Component, OnInit } from '@angular/core';
import { enableDebugTools } from '@angular/platform-browser';
import { ActivatedRoute } from '@angular/router';
import { Edge, Node } from '@swimlane/ngx-graph';
import { AlgorithmResponse } from '../models/AlgorithmResponse';
import { Coordinate } from '../models/Coordinate';

@Component({
  selector: 'simulator-solution',
  templateUrl: './simulator-solution.component.html',
  styleUrls: ['./simulator-solution.component.scss']
})
export class SimulatorSolutionComponent implements OnInit {

	simulatorSolution!: AlgorithmResponse;
	links: Edge[] = new Array();
	nodes: Node[] = new Array();

	constructor(private route: ActivatedRoute) { }

	ngOnInit(): void {
		this.route.params.subscribe(params => {
			console.log("params: ", params);
			this.simulatorSolution = params.destiniesOrders;
		});

		this.simulatorSolution = {
			routes: new Array(),
			solutionCost: 0
		};

		this.simulatorSolution.routes.push(new Coordinate(20, 30));
		this.simulatorSolution.routes.push(new Coordinate(20, 40));
		this.simulatorSolution.routes.push(new Coordinate(50, 50));
		this.simulatorSolution.routes.push(new Coordinate(30, 30));
		this.simulatorSolution.routes.push(new Coordinate(20, 30));

		this.createGraph();
	}

	createGraph() {

		// creating the nodes
		for (let index = 0; index < this.simulatorSolution.routes.length; index++) {
			const element = this.simulatorSolution.routes[index];
			this.nodes.push({
				id: "" + index,
				label: "" + element.lat + element.lng
			});
		}

		for (let index = 1; index < this.nodes.length; index++) {
			const element = this.nodes[index];
			this.links.push({
				id: ""+index,
				source: this.nodes[index - 1].id,
				target: element.id,
			});
		}

		this.links.push({
			id: ""+this.nodes.length,
			source: this.nodes[this.nodes.length - 1].id,
			target: this.nodes[0].id,
		});
	}

}
