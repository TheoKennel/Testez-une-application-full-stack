import { HttpClientModule } from '@angular/common/http';
import {ComponentFixture, TestBed} from '@angular/core/testing';
import { MatToolbarModule } from '@angular/material/toolbar';
import { RouterTestingModule } from '@angular/router/testing';
import { expect } from '@jest/globals';

import { AppComponent } from './app.component';
import {SessionService} from "./services/session.service";
import {FormComponent} from "./features/sessions/components/form/form.component";
import {A} from "@angular/cdk/keycodes";
import {Router} from "@angular/router";


describe('AppComponent', () => {
  let component : AppComponent
  let fixture : ComponentFixture<AppComponent>
  let mockSessionService = {
    $isLogged : jest.fn(),
    logOut : jest.fn()
  };

  let mockRouter = {
    navigate : jest.fn()
  };

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [
        RouterTestingModule,
        HttpClientModule,
        MatToolbarModule
      ],
      providers: [
        {provide: SessionService, useValue: mockSessionService},
        {provide: Router, useValue: mockRouter}
      ],
      declarations: [
        AppComponent
      ],
    }).compileComponents();

    fixture = TestBed.createComponent(AppComponent);
    component = fixture.componentInstance;
  });

  it('should create the app', () => {
    expect(component).toBeTruthy();
  });

  describe('$isLogged', () => {
      it('should call sessionService isLogged', () => {
        component.$isLogged()
        expect(mockSessionService.$isLogged).toHaveBeenCalled()
      });
  });

  describe('logout', () => {
    beforeEach(() => {
      component.logout()
    });

      it('should call sessionService is logOut', () => {
        expect(mockSessionService.logOut).toHaveBeenCalled()
      });

      it("should navigate to :  [''] ", () => {
        expect(mockRouter.navigate).toHaveBeenCalledWith([''])
      });
  });
});
