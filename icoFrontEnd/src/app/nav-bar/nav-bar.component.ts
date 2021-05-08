import { Component, EventEmitter, OnInit, Output } from '@angular/core';

@Component({
  selector: 'nav-bar',
  templateUrl: './nav-bar.component.html',
  styleUrls: ['./nav-bar.component.scss']
})
export class NavBarComponent implements OnInit {

	@Output('screenChanged') screenChangedEvent: EventEmitter<any> = new EventEmitter<any>();

	constructor() { }

	ngOnInit(): void {
	}

	nextScreen(screen: number) {
		console.log("screen:", screen);
		this.screenChangedEvent.emit(screen);
	}

}
