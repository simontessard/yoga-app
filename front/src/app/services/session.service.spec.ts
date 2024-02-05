import { TestBed } from '@angular/core/testing';
import { expect } from '@jest/globals';

import { SessionService } from './session.service';

describe('SessionService', () => {
  let service: SessionService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(SessionService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should log in', () => {
    const user = {
      token: 'token',
      id: 1,
      type: 'type',
      username: 'simondurand',
      firstName: 'simon',
      lastName: 'durand',
      admin: true,
    };
    service.logIn(user);
    expect(service.isLogged).toBe(true);
  });

  it('should log out', () => {
    service.logOut();
    expect(service.isLogged).toBe(false);
  });
});
