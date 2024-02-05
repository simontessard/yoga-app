import { HttpClientModule } from '@angular/common/http';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import {
  HttpClientTestingModule,
  HttpTestingController,
} from '@angular/common/http/testing';
import { RouterTestingModule } from '@angular/router/testing';
import { expect } from '@jest/globals';
import { NgZone } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { SessionApiService } from '../../services/session-api.service';
import { SessionService } from '../../../../services/session.service';
import { ReactiveFormsModule } from '@angular/forms';
import { MatSnackBarModule } from '@angular/material/snack-bar';

import { DetailComponent } from './detail.component';

describe('DetailComponent', () => {
  let component: DetailComponent;
  let httpTestingController: HttpTestingController;
  let fixture: ComponentFixture<DetailComponent>;
  let service: SessionService;
  let serviceApi: SessionApiService;
  let route: ActivatedRoute;
  let router: Router;
  let ngZone: NgZone;

  // Mock data for SessionService
  const mockSessionService = {
    sessionInformation: {
      admin: true,
      id: 1,
    },
  };

  // Set up the testing environment for each test
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [
        RouterTestingModule.withRoutes([
          { path: 'sessions', component: DetailComponent },
        ]),
        HttpClientModule,
        MatSnackBarModule,
        ReactiveFormsModule,
        HttpClientTestingModule,
      ],
      declarations: [DetailComponent],
      providers: [
        { provide: SessionService, useValue: mockSessionService },
        SessionApiService,
      ],
    }).compileComponents();
    service = TestBed.inject(SessionService);
    serviceApi = TestBed.inject(SessionApiService);
    fixture = TestBed.createComponent(DetailComponent);
    router = TestBed.inject(Router);
    route = TestBed.inject(ActivatedRoute);
    ngZone = TestBed.inject(NgZone);
    httpTestingController = TestBed.inject(HttpTestingController);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  // Test if the component is created successfully
  it('should create', () => {
    expect(component).toBeTruthy();
  });

  // Test if ngOnInit calls fetchSession
  it('should call ngOnInit', () => {
    const mockPrivateFetchSession = jest.spyOn(
      component as any,
      'fetchSession'
    );
    component.ngOnInit();
    expect(mockPrivateFetchSession).toBeCalled();
  });

  // Test if the back function is called
  it('should call back', () => {
    jest.spyOn(window.history, 'back');
    component.back();
    expect(window.history.back).toBeCalled();
  });

  // Test if the delete action API is called
  it('should call delete action API', () => {
    component.sessionId = '1';
    expect(component.delete()).toBe(void 0);
    const req = httpTestingController.expectOne('api/session/1');
    expect(req.request.method).toEqual('DELETE');
    req.flush(true);
  });

  // Test if the participate action API is called
  it('should call create participate action API', () => {
    component.sessionId = '1';
    component.userId = '1';

    expect(component.participate()).toBe(void 0);
    const req = httpTestingController.expectOne('api/session/1/participate/1');
    expect(req.request.method).toEqual('POST');
    req.flush(true);
  });

  // Test if the delete of user participating action API is called
  it('should call delete participate action API', () => {
    const myPrivateFuncExitPage = jest.spyOn(component as any, 'fetchSession');
    component.sessionId = '1';
    component.userId = '1';

    expect(component.unParticipate()).toBe(void 0);
    const req = httpTestingController.expectOne('api/session/1/participate/1');
    expect(req.request.method).toEqual('DELETE');
    req.flush(true);
    expect(myPrivateFuncExitPage).toBeCalled();
  });
});
