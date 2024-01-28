import { HttpClientModule } from '@angular/common/http';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import {  ReactiveFormsModule } from '@angular/forms';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import {MatSnackBar, MatSnackBarModule} from '@angular/material/snack-bar';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { expect } from '@jest/globals';
import { SessionService } from 'src/app/services/session.service';
import { SessionApiService } from '../../services/session-api.service';

import { FormComponent } from './form.component';
import {Session} from "../../interfaces/session.interface";
import {ActivatedRoute, Router} from "@angular/router";
import {of} from "rxjs";

describe('FormComponent', () => {
  let component: FormComponent;
  let fixture: ComponentFixture<FormComponent>;

  const mockSession: Session = {
    id: 1,
    name: "session test",
    description: "description de la session",
    date: new Date(),
    teacher_id: 1,
    users: [2, 3],
  };

  const mockSessionService = {
    sessionInformation: {
      admin: true
    }
  }

  const mockRouter = {
    navigate : jest.fn(),
    url : "/update"
  }

  const mockSessionApiService = {
    detail : jest.fn().mockReturnValue(of(mockSession)),
    create : jest.fn().mockReturnValue(of(mockSession)),
    update : jest.fn().mockReturnValue(of(mockSession))
  }

  const mockActivatedRoute = {
    snapshot: {
      paramMap: {
        get : jest.fn().mockReturnValue("1")
      }
    }
  }

  const mockMatSnackBar = {
    open : jest.fn()
  }

  beforeEach(async () => {
    await TestBed.configureTestingModule({

      imports: [
        HttpClientModule,
        MatCardModule,
        MatIconModule,
        MatFormFieldModule,
        MatInputModule,
        ReactiveFormsModule,
        MatSnackBarModule,
        MatSelectModule,
        BrowserAnimationsModule
      ],
      providers: [
        { provide: SessionService, useValue: mockSessionService },
        { provide: SessionApiService, useValue: mockSessionApiService },
        { provide: Router, useValue: mockRouter },
        { provide: MatSnackBar, useValue: mockMatSnackBar },
        { provide: ActivatedRoute, useValue: mockActivatedRoute }
      ],
      declarations: [FormComponent]
    })
      .compileComponents();

    fixture = TestBed.createComponent(FormComponent);
    component = fixture.componentInstance;
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  describe("on init", () => {
    it('should navigate to sessions with admin false', () => {
      mockSessionService.sessionInformation.admin = false;
      component.ngOnInit()
      expect(mockRouter.navigate).toHaveBeenCalledWith(['/sessions'])
    })

    describe('when in update mode', () => {
      beforeEach(() => {
        mockRouter.url = '/update';
        component.ngOnInit();
        fixture.detectChanges();
        });

        it('should fetch session details and initialize the form', () => {

          component.ngOnInit()
          expect(mockSessionApiService.detail).toHaveBeenCalledWith('1');
          expect(component.sessionForm).toBeDefined();
      });
  });

    describe('when path do not contain update', () => {
      beforeEach(() => {
        mockRouter.url = '/invalid';
        component.ngOnInit();
        fixture.detectChanges();
      });

      it('should set onUpdate to false', () => {
        expect(component.onUpdate).toBe(false);
        expect(component.sessionForm).toBeDefined();
      });
    });
  });

  describe('submit', () => {
    beforeEach(() => {
      component.ngOnInit()
      fixture.detectChanges()
    })
    it('should call create method when onUpdate is false', () => {
      const session = component.sessionForm?.value
      component.onUpdate = false;
      component.submit();

      expect(mockSessionApiService.create).toHaveBeenCalledWith(session)
      expect(mockMatSnackBar.open).toHaveBeenCalledWith('Session created !', 'Close', { duration: 3000 });
      expect(mockRouter.navigate).toHaveBeenCalledWith(['sessions']);
    });

      beforeEach(() => {
      mockRouter.url = "/update"
      })
    it('should call update method and exitPage with "Session updated !" when onUpdate is true', () => {
      const session = component.sessionForm?.value
      console.log(session)
      component.submit();

      expect(mockSessionApiService.update).toHaveBeenCalledWith('1', session);
      expect(mockMatSnackBar.open).toHaveBeenCalledWith('Session updated !', 'Close', { duration: 3000 });
      expect(mockRouter.navigate).toHaveBeenCalledWith(['sessions']);
    });
  });
});
