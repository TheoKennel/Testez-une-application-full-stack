import { TestBed } from '@angular/core/testing';
import { expect } from '@jest/globals';

import { SessionService } from './session.service';
import {SessionInformation} from "../interfaces/sessionInformation.interface";

describe('SessionService', () => {
  let service: SessionService;
  const sessionInformation = {
    token : "token",
    type : "type",
    id : 1,
    username: "class",
    firstName: "open",
    lastName: "rooms",
    admin: false
  }

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(SessionService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  describe('logIn', () => {
    beforeEach(() => {
      service.logIn(sessionInformation)
    })
    it('should set session information',() => {
      expect(service.sessionInformation).toEqual(sessionInformation)
    })
    it('should set isLogged to true', () => {
      expect(service.isLogged).toBe(true)
    })
  })

  describe('logOut', () => {
    beforeEach(() => {
      service.logOut()
    })
    it('should set sessionInformation to undefined', () => {
      expect(service.sessionInformation).toBe(undefined)
    })
    it('should set isLogged to false', () => {
      expect(service.isLogged).toBe(false)
    })
  })
});
