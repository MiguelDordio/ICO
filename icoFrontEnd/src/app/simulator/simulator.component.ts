import { Component, OnInit } from '@angular/core';
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

	apiResponse: any;
	showMap: boolean = true;
	map: any;
	selectedCoordinates: Coordinate[] = new Array();

	// inputs
	nVehicles!: number;
	maxCargo!: number;
	vehicleConsumption!: number;
	nDestinies!: number;
	demand!: number;

	constructor(private simulatorApi: SimulatorApiService, private route: ActivatedRoute, private router: Router) { }

	ngOnInit(): void {
		console.log("Simulator Page");
		let loader = new Loader({
			apiKey: 'AIzaSyBs6jtKaF86w6XSBakQ8LBEgPMqx2dZVoE'
		});
		loader.load().then(() => {
			this.initMap();
		  });
	}

	initMap(): void {
		const myLatlng = { lat: -25.363, lng: 131.044 };
	  
		const map = new google.maps.Map(document.getElementById("map") as HTMLElement, {
				zoom: 4,
				center: myLatlng,
		  	}
		);
	  
		const marker = new google.maps.Marker({
			position: myLatlng,
			map,
			title: "Click para aumentar",
		});

		map.addListener("click", (e: any) => {
			this.placeMarkerAndPanTo(e.latLng, map);
		});
	}

	placeMarkerAndPanTo(latLng: google.maps.LatLng, map: google.maps.Map) {
		new google.maps.Marker({
		  position: latLng,
		  map: map,
		});
		map.panTo(latLng);
		let coordinate = new Coordinate(latLng.lat(), latLng.lng());
		this.selectedCoordinates.push(coordinate);
		console.log("Lat: " + latLng.lat() + "\nLon: " + latLng.lng());
	}

	changePage() {
		this.showMap = !this.showMap;
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
