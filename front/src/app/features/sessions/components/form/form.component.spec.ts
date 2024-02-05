import { HttpClientModule } from '@angular/common/http';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ReactiveFormsModule } from '@angular/forms';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatSnackBarModule, MatSnackBar } from '@angular/material/snack-bar';
import { NoopAnimationsModule } from '@angular/platform-browser/animations';
import { RouterTestingModule } from '@angular/router/testing';
import { expect } from '@jest/globals';
import { SessionService } from 'src/app/services/session.service';
import { SessionApiService } from '../../services/session-api.service';
import { FormControl } from '@angular/forms';

import { FormComponent } from './form.component';
import { Router } from '@angular/router';
import { of } from 'rxjs';

describe('FormComponent', () => {
  let component: FormComponent;
  let fixture: ComponentFixture<FormComponent>;
  let router: Router;
  let matSnackBar: MatSnackBar;

  const mockSessionService = {
    sessionInformation: {
      admin: true,
    },
  };

  const mockedAPIService = {
    detail: jest.fn((id: string) => {
      return of({
        id: id,
        name: 'New session',
        description: 'Random description',
        date: new Date(),
        teacher_id: 1,
        users: [],
        createdAt: new Date(),
        updatedAt: new Date(),
      });
    }),
    delete: jest.fn((sessionId: string) => {
      return of({});
    }),
    create: jest.fn((session: any) => {
      return of({});
    }),
    update: jest.fn((id: string, session: any) => {
      return of({});
    }),
  };

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [
        RouterTestingModule.withRoutes([
          { path: 'sessions', component: FormComponent },
        ]),
        NoopAnimationsModule,
        HttpClientModule,
        MatCardModule,
        MatIconModule,
        MatFormFieldModule,
        MatInputModule,
        ReactiveFormsModule,
        MatSnackBarModule,
        MatSelectModule,
      ],
      providers: [
        { provide: SessionService, useValue: mockSessionService },
        { provide: SessionApiService, useValue: mockedAPIService },
        MatSnackBar,
      ],
      declarations: [FormComponent],
    }).compileComponents();

    fixture = TestBed.createComponent(FormComponent);
    component = fixture.componentInstance;
    router = TestBed.inject(Router);
    fixture.detectChanges();

    matSnackBar = TestBed.inject(MatSnackBar);
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  // Test that text is well displayed after form submission
  it('should call a MatSnackBar after creating session', () => {
    jest
      .spyOn(mockedAPIService, 'create')
      .mockReturnValue(of(mockSessionService));
    jest.spyOn(matSnackBar, 'open');

    component.submit();
    expect(matSnackBar.open).toBeCalledWith('Session created !', 'Close', {
      duration: 3000,
    });
  });

  // Test that the component navigates correctly when admin is false
  it('should navigate to sessions path when not admin session', () => {
    jest.spyOn(router, 'navigate');
    mockSessionService.sessionInformation.admin = false;
    component.ngOnInit();
    fixture.detectChanges();
    expect(router.navigate).toHaveBeenCalledWith(['/sessions']);
  });

  // Test that the form is submitted correctly when not in update mode
  it('should submit form when not in update mode', () => {
    component.onUpdate = false;
    component.submit();
    expect(mockedAPIService.create).toHaveBeenCalled();
  });

  // Test that the form is initialized correctly for update
  it('should initialize form for update', () => {
    jest.spyOn(router, 'url', 'get').mockReturnValue('/update/1');
    component.ngOnInit();
    expect(component.onUpdate).toBeTruthy();
    expect(mockedAPIService.detail).toHaveBeenCalled();
  });

  // Test that the form is submitted correctly when in update mode
  it('should submit form when in update mode', () => {
    component.onUpdate = true;
    component.submit();
    expect(mockedAPIService.update).toHaveBeenCalled();
  });

  // Test that the form button is disabled
  it('should have the save button disabled while the session form is not valid', () => {
    let inputText = new FormControl('');
    const saveButton = fixture.debugElement.nativeElement.querySelector(
      'button[type="submit"]'
    );
    inputText.setValue('');
    fixture.detectChanges();
    expect(saveButton.disabled).toBe(true);
  });
});
