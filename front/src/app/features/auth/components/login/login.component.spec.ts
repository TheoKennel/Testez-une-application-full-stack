import { HttpClientModule } from '@angular/common/http';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ReactiveFormsModule } from '@angular/forms';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { RouterTestingModule } from '@angular/router/testing';
import { expect } from '@jest/globals';
import { SessionService } from 'src/app/services/session.service';

import { LoginComponent } from './login.component';
import {AuthService} from "../../services/auth.service";
import {of, throwError} from "rxjs";
import {Router} from "@angular/router";

describe('LoginComponent', () => {
  let component: LoginComponent;
  let fixture: ComponentFixture<LoginComponent>;
  let mockSessionService = {
    logIn : jest.fn()
  }
  let mockAuthService =  {
    login: jest.fn()
  }
  let mockRoute = {
    navigate : jest.fn()
  }

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [LoginComponent],
      providers: [
      { provide: SessionService, useValue : mockSessionService },
      { provide: AuthService, useValue : mockAuthService },
      { provide: Router, useValue : mockRoute}
    ],
      imports: [
        RouterTestingModule,
        BrowserAnimationsModule,
        HttpClientModule,
        MatCardModule,
        MatIconModule,
        MatFormFieldModule,
        MatInputModule,
        ReactiveFormsModule]
    })
      .compileComponents();
    fixture = TestBed.createComponent(LoginComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  describe('submit', () => {
    it('should handle successful login', () => {
      mockAuthService.login.mockReturnValue(of(fixture));

      component.submit();

      expect(mockSessionService.logIn).toHaveBeenCalledWith(fixture);
      expect(mockRoute.navigate).toHaveBeenCalledWith(['/sessions']);
      expect(component.onError).toBe(false);
    });
  })

  it('should set onError to true on error', () => {
    mockAuthService.login.mockImplementation(() => {
      return throwError("error message");
    });

    component.submit();

    expect(component.onError).toBe(true);
  });
});
