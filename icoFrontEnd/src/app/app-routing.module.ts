import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { HomepageComponent } from './homepage/homepage.component';
import { MapComponent } from './map/map.component';
import { SimulatorComponent } from './simulator/simulator.component';
import { PagesRoute } from './models/routingPaths';

const routes: Routes = [
	{path: '', component: HomepageComponent},
	{path: PagesRoute.MAP, component: MapComponent},
	{path: PagesRoute.SIMULATOR, component: SimulatorComponent}
];

@NgModule({
	imports: [RouterModule.forRoot(routes)],
	exports: [RouterModule]
})
export class AppRoutingModule { }
