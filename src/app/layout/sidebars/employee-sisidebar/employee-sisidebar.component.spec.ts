import { ComponentFixture, TestBed } from '@angular/core/testing';

import { EmployeeSISidebarComponent } from './employee-sisidebar.component';

describe('EmployeeSISidebarComponent', () => {
  let component: EmployeeSISidebarComponent;
  let fixture: ComponentFixture<EmployeeSISidebarComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [EmployeeSISidebarComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(EmployeeSISidebarComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
