import {HttpClient, HttpClientModule} from '@angular/common/http';
import { TestBed } from '@angular/core/testing';
import { expect } from '@jest/globals';

import { SessionApiService } from './session-api.service';
import {of} from "rxjs";
import {Session} from "../interfaces/session.interface";

describe('SessionsService', () => {
  let sessionApi: SessionApiService;
  let baseApiUrl = "http://localhost:8080/";
  let httpClientSpy = {
    get: jest.fn(),
    post: jest.fn(),
    put: jest.fn(),
    delete: jest.fn(),
  };

  const mockSession: Session = {
    id: 1,
    name: "session test",
    description: "description de la sessionApi",
    date: new Date("2024-01-23"),
    teacher_id: 1,
    users: [2, 3],
  };

  beforeEach(() => {

    TestBed.configureTestingModule({
      imports:[
        HttpClientModule
      ],
      providers: [{ provide: HttpClient, useValue: httpClientSpy }],
    });
    sessionApi = TestBed.inject(SessionApiService);
  });

  it('should be created', () => {
    expect(sessionApi).toBeTruthy();
  });

  it('should retrieve all sessions', () => {
    const expectedSessions: Session[] = [mockSession]
    httpClientSpy.get.mockReturnValue(of(expectedSessions));

    sessionApi.all().subscribe(sessions => {
      expect(sessions).toEqual(expectedSessions);
    });
    expect(httpClientSpy.get).toHaveBeenCalledWith(baseApiUrl + 'api/session');
  });

  it('should retrieve sessionApi detail', () => {
    const expectedSession: Session = mockSession
    httpClientSpy.get.mockReturnValue(of(expectedSession));

    sessionApi.detail('1').subscribe(session => {
      expect(session).toEqual(expectedSession);
    });
    expect(httpClientSpy.get).toHaveBeenCalledWith(baseApiUrl + 'api/session/1');
  });

  it('should delete a sessionApi', () => {
    httpClientSpy.delete.mockReturnValue(of({}));

    sessionApi.delete('1').subscribe(response => {
      expect(response).toEqual({});
    });
    expect(httpClientSpy.delete).toHaveBeenCalledWith(baseApiUrl + 'api/session/1');
  });

  it('should create a sessionApi', () => {
    const newSession: Session = mockSession
    httpClientSpy.post.mockReturnValue(of(newSession));

    sessionApi.create(newSession).subscribe(session => {
      expect(session).toEqual(newSession);
    });
    expect(httpClientSpy.post).toHaveBeenCalledWith(baseApiUrl + 'api/session', newSession);
  });

  it('should update a sessionApi', () => {
    const updatedSession: Session = mockSession
    httpClientSpy.put.mockReturnValue(of(updatedSession));

    sessionApi.update('1', updatedSession).subscribe(session => {
      expect(session).toEqual(updatedSession);
    });
    expect(httpClientSpy.put).toHaveBeenCalledWith(baseApiUrl + 'api/session/1', updatedSession);
  });

  it('should handle participation in a sessionApi', () => {
    httpClientSpy.post.mockReturnValue(of(null));

    sessionApi.participate('1', 'userId').subscribe(response => {
      expect(response).toBeNull();
    });
    expect(httpClientSpy.post).toHaveBeenCalledWith(baseApiUrl + 'api/session/1/participate/userId', null);
  });

  it('should handle unparticipation in a sessionApi', () => {
    httpClientSpy.delete.mockReturnValue(of(null));

    sessionApi.unParticipate('1', 'userId').subscribe(response => {
      expect(response).toBeNull();
    });
    expect(httpClientSpy.delete).toHaveBeenCalledWith(baseApiUrl + 'api/session/1/participate/userId');
  });
});
