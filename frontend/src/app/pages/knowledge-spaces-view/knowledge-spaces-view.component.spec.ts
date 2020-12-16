import { ComponentFixture, TestBed } from '@angular/core/testing';

import { KnowledgeSpacesViewComponent } from './knowledge-spaces-view.component';

describe('KnowledgeSpacesViewComponent', () => {
  let component: KnowledgeSpacesViewComponent;
  let fixture: ComponentFixture<KnowledgeSpacesViewComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ KnowledgeSpacesViewComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(KnowledgeSpacesViewComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
