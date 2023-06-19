import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AddLoginInformationComponent } from './add-login-information.component';

describe('AddLoginInformationComponent', () => {
  let component: AddLoginInformationComponent;
  let fixture: ComponentFixture<AddLoginInformationComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ AddLoginInformationComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AddLoginInformationComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
