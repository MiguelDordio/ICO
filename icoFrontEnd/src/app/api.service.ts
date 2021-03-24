import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';


const httpOptions = {
	headers: new HttpHeaders({
		'Content-Type': 'application/json',
	})
}


@Injectable({
 	providedIn: 'root'
})
export class ApiService {

  	constructor(private http: HttpClient) { }

	getStuff(): Observable<{}> {
		return this.http.post(environment.apiUrl + '/hello', httpOptions)
	}
}
