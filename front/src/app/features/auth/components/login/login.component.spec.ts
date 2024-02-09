import { HttpClientModule } from '@angular/common/http';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ReactiveFormsModule } from '@angular/forms';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { RouterTestingModule } from '@angular/router/testing';
import { expect } from '@jest/globals';
import { SessionService } from 'src/app/services/session.service';
import { Router } from '@angular/router';
import { throwError, of } from 'rxjs';
import { AuthService } from '../../services/auth.service';
import { LoginRequest } from '../../interfaces/loginRequest.interface';

import { LoginComponent } from './login.component';
import { AuthGuard } from 'src/app/guards/auth.guard';

describe('LoginComponent', () => {
  let component: LoginComponent;
  let fixture: ComponentFixture<LoginComponent>;
  let mockSessionService: jest.Mocked<SessionService>;
  let mockRouter: Router;
  let guard: AuthGuard;
  let sessionService: SessionService;

  mockRouter = {
    navigate: jest.fn(),
  } as unknown as jest.Mocked<Router>;

  let mockAuthService = {
    login: jest.fn(),
  };

  const sessionServiceMock = { isLogged: false };

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [LoginComponent],
      providers: [
        AuthGuard,
        { provide: SessionService, useValue: mockSessionService },
        { provide: SessionService, useValue: sessionServiceMock },
        { provide: Router, useValue: mockRouter },
        { provide: AuthService, useValue: mockAuthService },
      ],
      imports: [
        RouterTestingModule,
        BrowserAnimationsModule,
        HttpClientModule,
        MatCardModule,
        MatIconModule,
        MatFormFieldModule,
        MatInputModule,
        ReactiveFormsModule,
      ],
    }).compileComponents();
    fixture = TestBed.createComponent(LoginComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
    guard = TestBed.inject(AuthGuard);
  });

  it('should successfully created', () => {
    expect(component).toBeTruthy();
  });

  it('component initial state', () => {
    expect(component.hide).toBeTruthy();
    expect(component.form).toBeDefined();
    expect(component.form.invalid).toBeTruthy();
    expect(component.onError).toBeFalsy();
  });

  it('should set on error during login', () => {
    const error = new Error();
    const errorSpy = jest
      .spyOn(mockAuthService, 'login')
      .mockReturnValueOnce(throwError(error));
    component.submit();
    expect(errorSpy).toHaveBeenCalled;
    expect(component.onError).toBeTruthy();
  });

  it('should redirect to login and return false when not logged in', () => {
    expect(guard.canActivate()).toBe(false);
    expect(mockRouter.navigate).toHaveBeenCalledWith(['login']);
  });

  it('should return true when logged in', () => {
    sessionServiceMock.isLogged = true;

    expect(guard.canActivate()).toBe(true);
  });

  describe('submit', () => {
    it('should log in and navigate on successful login', () => {
      const loginRequest: LoginRequest = {
        email: 'test@example.com',
        password: 'test123',
      };
      const sessionInformation = {
        admin: true,
        id: 1,
      };

      const spyLogin = jest
        .spyOn(mockAuthService, 'login')
        .mockReturnValue(of(sessionInformation));

      component.form.setValue(loginRequest);
      component.submit();

      expect(spyLogin).toHaveBeenCalledWith(loginRequest);
      expect(component.onError).toBeFalsy();
    });
  });
});
