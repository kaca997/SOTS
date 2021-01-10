import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DrivenTestingComponent } from './driven-testing.component';

describe('DrivenTestingComponent', () => {
  let component: DrivenTestingComponent;
  let fixture: ComponentFixture<DrivenTestingComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ DrivenTestingComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(DrivenTestingComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
