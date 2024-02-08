import { HttpClientModule } from '@angular/common/http';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ReactiveFormsModule } from '@angular/forms';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { expect } from '@jest/globals';
import { throwError, of } from 'rxjs';
import { AuthService } from '../../services/auth.service';

import { RegisterComponent } from './register.component';
import { Router } from '@angular/router';

describe('RegisterComponent', () => {
  let component: RegisterComponent;
  let fixture: ComponentFixture<RegisterComponent>;
  let authService: jest.Mocked<AuthService>;
  let router: Router;

  const mockAuthService = {
    register: jest.fn(),
  };

  beforeEach(async () => {
    const authServiceMock = {
      register: jest.fn(),
    };

    const routerMock = {
      navigate: jest.fn(),
    };

    await TestBed.configureTestingModule({
      declarations: [RegisterComponent],
      providers: [
        { provide: AuthService, useValue: authServiceMock },
        { provide: Router, useValue: routerMock },
      ],
      imports: [
        BrowserAnimationsModule,
        HttpClientModule,
        ReactiveFormsModule,
        MatCardModule,
        MatFormFieldModule,
        MatIconModule,
        MatInputModule,
      ],
    }).compileComponents();

    fixture = TestBed.createComponent(RegisterComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
    authService = TestBed.inject(AuthService) as jest.Mocked<AuthService>;
    router = TestBed.inject(Router);
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('submit should call service', async () => {
    let authServiceSpy = jest
      .spyOn(authService, 'register')
      .mockReturnValue(of(void 0));
    await component.submit();
    expect(authServiceSpy).toHaveBeenCalled();
  });

  it('should set onError on registration error', () => {
    const spyRegister = jest
      .spyOn(authService, 'register')
      .mockReturnValue(throwError(() => new Error('Incorrect value(s)')));

    component.submit();

    expect(spyRegister).toHaveBeenCalled();
    expect(component.onError).toBeTruthy();
  });

  it('should register', () => {
    authService.register.mockReturnValue(of(undefined));
    component.form.setValue({
      email: 'test@test.com',
      firstName: 'Test',
      lastName: 'User',
      password: 'password123',
    });
    component.submit();
    expect(authService.register).toHaveBeenCalled();
  });
});
