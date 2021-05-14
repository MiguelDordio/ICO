import { Component, OnInit } from '@angular/core';
import { FormArray, FormBuilder, FormGroup } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { Loader } from '@googlemaps/js-api-loader';
import { AlgorithmRequest } from '../models/AlgorithmRequest';
import { Coordinate } from '../models/Coordinate';
import { PagesRoute } from '../models/routingPaths';
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
			this.showMap = !this.showMap;
			this.pageNumber++;
			this.pageTitle = "Veiculos";
			this.pageTip = "Caractize os seus veiculos";
		}
		// From step 2 -> 3 - Packages
		else if (this.pageNumber == 2) {
			this.pageNumber++;
			this.pageTitle = "Encomendas";
			this.pageTip = "Caractize as encomendas dos seus clientes";
		} 
		// From step 3 -> 4 - Simulation Resume
		else if (this.pageNumber == 3) {
			this.pageNumber++;
			this.pageTitle = "Resumo de simulação";
			this.pageTip = "Confirme os dados";
		}
	}

	addItem() {
		if (this.pageNumber == 2)
			this.addVehicle()
		else if (this.pageNumber == 3)
			this.addPackage();		
	}

	addVehicle() {
		const vhcs = this.vehicleForm.controls.vehicles as FormArray;
		vhcs.push(this.fb.group({
			maxCargo: '',
			fuelConsumption: '',
		}));
	}

	addPackage() {
		const pack = this.packagesForm.controls.packages as FormArray;
		pack.push(this.fb.group({
			weight: ''
		}));
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
