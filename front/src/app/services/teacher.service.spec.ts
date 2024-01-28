import {HttpClient, HttpClientModule} from '@angular/common/http';
import { TestBed } from '@angular/core/testing';
import { expect } from '@jest/globals';

import { TeacherService } from './teacher.service';
import {of} from "rxjs";

describe('TeacherService', () => {
  let service: TeacherService;
  let httpClientSpy = {
    get : jest.fn()
  }

  let pathService = 'api/teacher/1'

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers : [{provide : HttpClient, useValue : httpClientSpy}],
      imports:[
        HttpClientModule
      ]
    });
    service = TestBed.inject(TeacherService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should get teacher details by id', () => {
    httpClientSpy.get.mockReturnValue(of(null))
    service.detail('1').subscribe(teacher => {
      expect(teacher).toEqual(null)
    })
    expect(httpClientSpy.get).toHaveBeenCalledWith(pathService)
  })
});
