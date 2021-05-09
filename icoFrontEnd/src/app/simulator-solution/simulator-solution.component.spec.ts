import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SimulatorSolutionComponent } from './simulator-solution.component';

describe('SimulatorSolutionComponent', () => {
  let component: SimulatorSolutionComponent;
  let fixture: ComponentFixture<SimulatorSolutionComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ SimulatorSolutionComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(SimulatorSolutionComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
