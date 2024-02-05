import { HttpClientModule } from '@angular/common/http';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { MatSnackBarModule, MatSnackBar } from '@angular/material/snack-bar';
import { SessionService } from 'src/app/services/session.service';
import { expect } from '@jest/globals';
import { of } from 'rxjs';
import { UserService } from 'src/app/services/user.service';
import { Router } from '@angular/router';
import { NoopAnimationsModule } from '@angular/platform-browser/animations';
import { RouterTestingModule } from '@angular/router/testing';

import { MeComponent } from './me.component';

// Test suite for MeComponent
describe('MeComponent', () => {
  let component: MeComponent;
  let fixture: ComponentFixture<MeComponent>;
  let snackBar: MatSnackBar;
  let router: Router;

  // Mock user data
  const mockedUser = {
    id: 1,
    email: 'simon@durand.com',
    lastName: 'Durand',
    firstName: 'Simon',
    admin: false,
    password: 'test!1234',
    createdAt: new Date(),
  };

  // Set up the testing environment for each test
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [MeComponent],
      imports: [
        HttpClientModule,
        NoopAnimationsModule,
        RouterTestingModule,
        MatSnackBarModule,
        MatCardModule,
        MatFormFieldModule,
        MatIconModule,
        MatInputModule,
      ],
      providers: [
        {
          provide: SessionService,
          useValue: {
            sessionInformation: {
              id: 1,
            },
            logOut: () => {},
          },
        },
        {
          provide: UserService,
          useValue: {
            getById: () => of(mockedUser),
            delete: () => of({}),
          },
        },
      ],
    }).compileComponents();

    // Get the injected instances
    snackBar = TestBed.inject(MatSnackBar);
    router = TestBed.inject(Router);

    // Create the component
    fixture = TestBed.createComponent(MeComponent);
    component = fixture.componentInstance;

    // Trigger change detection for the component
    fixture.detectChanges();
  });

  // Test that the component is created correctly
  it('should create', () => {
    expect(component).toBeTruthy();
  });

  // Test that the back function works correctly
  it('should go back to home page', () => {
    jest.spyOn(window.history, 'back').mockImplementation(() => {});
    component.back();
    expect(window.history.back).toHaveBeenCalled();
  });

  // Test that the user information is retrieved correctly
  it('should get user data', () => {
    expect(component.user).toBeTruthy();
    expect(component.user).toEqual(mockedUser);
    fixture.detectChanges();
  });

  // Test that the delete function works correctly
  it('should delete the user', () => {
    const routerSpy = jest
      .spyOn(router, 'navigate')
      .mockImplementation(async () => true);
    const snackBarSpy = jest.spyOn(snackBar, 'open');

    component.delete();
    // Check that the snackbar was opened and the router navigated to the home page
    expect(snackBarSpy).toHaveBeenCalled();
    expect(routerSpy).toHaveBeenCalledWith(['/']);
  });
});
