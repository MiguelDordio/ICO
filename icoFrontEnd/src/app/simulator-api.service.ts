import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';
import { AlgorithmRequest } from './models/AlgorithmRequest';


const httpOptions = {
	headers: new HttpHeaders({
		'Content-Type': 'application/json',
	})
}

@Injectable({
  providedIn: 'root'
})
export class SimulatorApiService {

	constructor(private http: HttpClient) { }

	simulate(data: AlgorithmRequest): Observable<{}> {
		console.log('Fetching solution for parameters:', data);
		return this.http.post(environment.apiUrl + '/', data, httpOptions)
	}
}
