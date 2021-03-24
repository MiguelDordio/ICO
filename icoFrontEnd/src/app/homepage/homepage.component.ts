import { Component, OnInit } from '@angular/core';
import { ApiService } from '../api.service';

@Component({
  selector: 'app-homepage',
  templateUrl: './homepage.component.html',
  styleUrls: ['./homepage.component.scss']
})
export class HomepageComponent implements OnInit {

    apiResponse: any;

    constructor(private algoApi: ApiService) { }

    ngOnInit(): void {
    }

	apiTest() {	
		this.algoApi.getStuff().subscribe(resp => {
			this.apiResponse = resp;
			console.log("Fetching for stuff:", resp);
			console.log("Fetched:", this.apiResponse.username);
		});
	}

}
