import { HttpClientModule } from '@angular/common/http';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ReactiveFormsModule } from '@angular/forms';
import {MatSnackBar, MatSnackBarModule} from '@angular/material/snack-bar';
import { RouterTestingModule, } from '@angular/router/testing';
import { expect } from '@jest/globals';
import { SessionService } from '../../../../services/session.service';

import { DetailComponent } from './detail.component';
import {SessionApiService} from "../../services/session-api.service";
import any = jasmine.any;
import {ActivatedRoute, Router} from "@angular/router";
import {of} from "rxjs";
import {Session} from "../../interfaces/session.interface";
import {Teacher} from "../../../../interfaces/teacher.interface";
import {TeacherService} from "../../../../services/teacher.service";
import {MatCardModule} from "@angular/material/card";
import {MatIconModule} from "@angular/material/icon";


describe('DetailComponent', () => {
  let component: DetailComponent;
  let fixture: ComponentFixture<DetailComponent>;
  let service: SessionService;

  const mockSession: Session = {
    id: 1,
    name: "session test",
    description: "description de la session",
    date: new Date("2024-01-23"),
    teacher_id: 1,
    users: [2, 3],
  };

  const mockTeacher: Teacher = {
    id: 10,
    lastName: "Class",
    firstName: "Rooms",
    createdAt: new Date("2024-01-23"),
    updatedAt: new Date("2024-01-23")
  };

  const mockSessionService = {
    sessionInformation: {
      admin: true,
      id: 1
    }
  }

  const mockActivatedRoute = {
    snapshot: {
      paramMap: {
        get: () => '1'
      }
    }
  }

  const mockTeacherService = {
    detail: jest.fn().mockReturnValue(of(mockTeacher))
  };

  const mockRouter = {
    navigate: jest.fn()
  }

  const mockMatSnackBar = {
    open: jest.fn()
  }
  const mockSessionApiService = {
    detail: jest.fn().mockReturnValue(of(mockSession)),
    delete: jest.fn().mockReturnValue(of(null)),
    participate: jest.fn().mockReturnValue(of(null)),
    unParticipate: jest.fn().mockReturnValue(of(null))
  }

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [
        RouterTestingModule,
        HttpClientModule,
        MatSnackBarModule,
        ReactiveFormsModule,
        MatCardModule,
        MatIconModule
      ],
      declarations: [DetailComponent],
      providers: [{provide: SessionService, useValue: mockSessionService},
        {provide: SessionApiService, useValue: mockSessionApiService},
        {provide: ActivatedRoute, useValue: mockActivatedRoute},
        {provide: MatSnackBar, useValue: mockMatSnackBar},
        {provide: TeacherService, useValue: mockTeacherService},
        {provide: Router, useValue: mockRouter}
      ],
    })
      .compileComponents();
    service = TestBed.inject(SessionService);
    fixture = TestBed.createComponent(DetailComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  describe('delete', () => {
    beforeEach(() => {
      component.delete();
    });
    it('should delete a session ', () => {
      expect(mockSessionApiService.delete).toHaveBeenCalled()
    })
    it('should open a snackbar with message', () => {
      expect(mockMatSnackBar.open).toHaveBeenCalledWith('Session deleted !', 'Close', {duration: 3000});
    });
    it('should navigate to sessions page', () => {
      expect(mockRouter.navigate).toHaveBeenCalledWith(['sessions']);
    });
  });

  describe('participate', () => {
    it('should call participate and then fetchSession', () => {
      component.participate();
      expect(mockSessionApiService.participate).toHaveBeenCalledWith(component.sessionId, component.userId);
    });
  });

  describe('unParticipate', () => {
    it('should call unParticipate and then fetchSession', () => {
      component.unParticipate();
      expect(mockSessionApiService.unParticipate).toHaveBeenCalledWith(component.sessionId, component.userId);
    });
  });

  describe('back', () => {
    it('should go back', () => {
      const historySpy = jest.spyOn(window.history, 'back')
      component.back()
      expect(historySpy).toHaveBeenCalled();
    });
  });
});

