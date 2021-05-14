import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { HomepageComponent } from './homepage/homepage.component';
import { HttpClientModule } from '@angular/common/http'
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { NavBarComponent } from './nav-bar/nav-bar.component';
import { SimulatorComponent } from './simulator/simulator.component';
import { MapComponent } from './map/map.component';
import { SimulatorSolutionComponent } from './simulator-solution/simulator-solution.component';
import { ComponentNameComponent } from './component-name/component-name.component'
@NgModule({
  declarations: [
    AppComponent,
    HomepageComponent,
    NavBarComponent,
    SimulatorComponent,
    MapComponent,
    SimulatorSolutionComponent,
    ComponentNameComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    HttpClientModule,
    FormsModule,
    ReactiveFormsModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
