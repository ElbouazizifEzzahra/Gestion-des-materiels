import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DemandesByUserComponent } from './demandes-by-user.component';

describe('DemandesByUserComponent', () => {
  let component: DemandesByUserComponent;
  let fixture: ComponentFixture<DemandesByUserComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [DemandesByUserComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(DemandesByUserComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
