import { Component, OnInit } from '@angular/core';
import { FormArray, FormBuilder, FormGroup } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { Loader } from '@googlemaps/js-api-loader';
import { Edge, Node } from '@swimlane/ngx-graph';
import { AlgorithmRequest } from '../models/AlgorithmRequest';
import { Coordinate } from '../models/Coordinate';
import { Order } from '../models/Order';
import { Vehicle } from '../models/Vehicle';
import { SimulatorApiService } from '../simulator-api.service';

@Component({
  selector: 'simulator',
  templateUrl: './simulator.component.html',
  styleUrls: ['./simulator.component.scss']
})
export class SimulatorComponent implements OnInit {

	pageTitle: string = "";
	pageTip: string = "";
	pageNumber: number = 0;

	vehicleForm: FormGroup;
	packagesForm: FormGroup;

	apiResponse: any;
	showMap: boolean = true;
	map: any;
	selectedCoordinates: Coordinate[] = new Array();
	mapFirstClick: boolean = true;

	// inputs
	nVehicles!: number;
	maxCargo!: number;
	vehicleConsumption!: number;
	nDestinies!: number;
	demand!: number;

	// request
	depot!: Coordinate;
	clients: Coordinate[] = new Array();
	chosenClients: Coordinate[] = new Array();
	request!: AlgorithmRequest;

	// graph
	links!: Edge[];
	nodes!: Node[];

	constructor(
		private simulatorApi: SimulatorApiService, 
		private route: ActivatedRoute, 
		private router: Router,
		private fb: FormBuilder) {

			this.vehicleForm = this.fb.group({
				vehicles: this.fb.array([]),
			});

			this.packagesForm = this.fb.group({
				packages: this.fb.array([]),
			});
		}

	ngOnInit(): void {
		console.log("Simulator Page");
		this.changePage(false);
	}

	initMap(): void {
		const myLatlng = { lat: -25.363, lng: 131.044 };
	  
		const map = new google.maps.Map(document.getElementById("map") as HTMLElement, {
				zoom: 4,
				center: myLatlng,
		  	}
		);

		map.addListener("click", (e: any) => {
			this.placeMarkerAndPanTo(e.latLng, map);
		});
	}

	placeMarkerAndPanTo(latLng: google.maps.LatLng, map: google.maps.Map) {
		new google.maps.Marker({
		  position: latLng,
		  map: map,
		  icon: this.mapFirstClick ? '../../assets/depot_icon.png' : '../../assets/client_icon.png'
		});
		if (this.mapFirstClick) {
			this.mapFirstClick = false;
			this.pageTip = "Indique os locais das entregas";
		}
		map.panTo(latLng);
		let coordinate = new Coordinate(latLng.lat(), latLng.lng());
		this.selectedCoordinates.push(coordinate);
		console.log("Lat: " + latLng.lat() + "\nLon: " + latLng.lng());
	}

	changePage(goBack: boolean) {
		if (goBack)
			this.pageNumber -= 2;

		// From step 1 -> 2 - Map
		if (this.pageNumber == 0) {
			this.pageNumber++;
			this.pageTitle = "Localizações";
			this.pageTip = "Indique o local de partida";

			if (!goBack) {
				let loader = new Loader({
					apiKey: 'AIzaSyBs6jtKaF86w6XSBakQ8LBEgPMqx2dZVoE'
				});
				loader.load().then(() => {
					this.initMap();
				});
			}
		}
		// From step 1 -> 2 - Vehicles
		else if (this.pageNumber == 1){

			if (!goBack)
				this.prepareLocationsInputs();

			this.showMap = !this.showMap;
			this.pageNumber++;
			this.pageTitle = "Veiculos";
			this.pageTip = "Caracterize os seus veiculos";
		}
		// From step 2 -> 3 - Packages
		else if (this.pageNumber == 2) {

			if(this.packagesForm.controls.packages?.value.length < this.selectedCoordinates.length - 1) {
				for (let index = this.packagesForm.controls.packages?.value.length; index < this.selectedCoordinates.length - 1; index++) {
					this.addPackage();
				}
			}

			this.pageNumber++;
			this.pageTitle = "Encomendas";
			this.pageTip = "Caractize as encomendas dos seus clientes";
		} 
		// From step 3 -> 4 - Simulation Resume
		else if (this.pageNumber == 3) {
			this.request = this.setupRequest();
			this.pageNumber++;
			this.pageTitle = "Resumo de simulação";
			this.pageTip = "Confirme os dados";
		}
		// From step 4 -> 5 - Simulation Result
		else if (this.pageNumber == 4) {
			this.createGraph();
			this.pageNumber++;
			this.pageTitle = "Resultado da simulação";
			this.pageTip = "Verifique as rotas";
		}
	}


	/************************
	 * Forms
	 ************************/

	addItem() {
		if (this.pageNumber == 2)
			this.addVehicle()
		else if (this.pageNumber == 3)
			this.addPackage();		
	}

	addVehicle() {
		const vhcs = this.vehicleForm.controls.vehicles as FormArray;
		vhcs.push(this.fb.group(new Vehicle(0, 0)));
	}

	addPackage() {
		const pack = this.packagesForm.controls.packages as FormArray;
		pack.push(this.fb.group(new Order(0, new Coordinate(0, 0))));
	}

	prepareLocationsInputs() {
		let index = 0;
		this.selectedCoordinates.forEach( (location) => {
			location.lat
			index > 0 ? this.clients.push(location) : this.depot = location;
			index++;
		});
	}

	trackByFn(index: any, item: any) {
		return index;  
	}


	/************************
	 * Simulation
	 ************************/

	setupRequest() {
		let algoRequest = new AlgorithmRequest();

		this.vehicleForm.controls.vehicles?.value.forEach( (element: Vehicle) => {
			algoRequest.vehicles.push(element);
		});

		this.packagesForm.controls.packages?.value.forEach( (element: Order) => {
			algoRequest.orders.push(element);
		});

		algoRequest.depot = this.depot;

		return algoRequest;
	}

	simulate() {
		console.log("request:", this.request);
		this.simulatorApi.simulate(this.request).subscribe(resp => {
			this.apiResponse = resp;
			console.log("Solution fetched:", resp);
			this.changePage(false);
			//this.goToSolutionDisplay()
		});
	}

	/*
	goToSolutionDisplay() {
		this.router.navigate(["/" + PagesRoute.SIMULATOR_SOLUTION, { state: { data: {
			simulatorSolution: this.apiResponse,
			simulationRequest: this.request
		}}}]);
	}
	*/

	createGraph() {

		this.links = new Array();
		this.nodes = new Array();

		// creating the nodes
		for (let index = 0; index < this.apiResponse.routes.length; index++) {
			const element = this.apiResponse.routes[index];

			// if its the depot
			if (element.lat == Math.round(this.request.depot.lat) && element.lng == Math.round(this.request.depot.lng) && index == 0) {
				this.nodes.push({
					id: "" + index,
					label: "Armazem"
				});
			} else {
				// check wich client the order
				for (let i = 0; i < this.request.orders.length; i++) {
					const client = this.request.orders[i];
					if ((element.lat == Math.round(client.destiny.lat) && element.lng == Math.round(client.destiny.lng))) {
						this.nodes.push({
							id: "" + index,
							label: "Cliente " + (i + 1)
						});
						break;
					}
				}
			}
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


	/*
		this.currentRoute.queryParams.subscribe((queryParams) => {
            let params = this.router.getCurrentNavigation().extras.state;
			
            if (this.params) {
                this.simulatorSolution = params[simulationResponse];
				this.simulatorRequest = params[simulationRequest];
            }
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

		*/
}
