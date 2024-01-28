import {HttpClient, HttpClientModule} from '@angular/common/http';
import {TestBed} from '@angular/core/testing';
import {expect} from '@jest/globals';

import {UserService} from './user.service';
import {of} from "rxjs";

describe('UserService', () => {
  let service: UserService;
  let httpClientSpy = {
    get: jest.fn(),
    delete: jest.fn()
  }

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [{
        provide: HttpClient, useValue: httpClientSpy
      }],
      imports: [
        HttpClientModule
      ]
    });
    service = TestBed.inject(UserService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should get user by id', () => {
    httpClientSpy.get.mockReturnValue(of(null))
    service.getById('1').subscribe(user => {
      expect(user).toEqual(null)
    });
    expect(httpClientSpy.get).toHaveBeenCalledWith("api/user/1")
  });

  it('should delete user by id', () => {
    httpClientSpy.delete.mockReturnValue(of("deleted"))
    service.delete("1").subscribe(response => {
      expect(response).toEqual("deleted")
    });
    expect(httpClientSpy.delete).toHaveBeenCalledWith("api/user/1")
  });
});
