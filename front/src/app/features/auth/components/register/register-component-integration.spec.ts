import { ComponentFixture, TestBed } from '@angular/core/testing';
import { RegisterComponent } from './register.component';
import { AuthService } from '../../services/auth.service';
import { ReactiveFormsModule } from '@angular/forms';
import { By } from '@angular/platform-browser';
import { Router } from '@angular/router';
import { of, throwError } from 'rxjs';
import { expect } from '@jest/globals';
import {BrowserAnimationsModule} from "@angular/platform-browser/animations";
import {HttpClientModule} from "@angular/common/http";
import {MatCardModule} from "@angular/material/card";
import {MatFormFieldModule} from "@angular/material/form-field";
import {MatIconModule} from "@angular/material/icon";
import {MatInputModule} from "@angular/material/input";

describe('RegisterComponent Integration', () => {
  let component: RegisterComponent;
  let fixture: ComponentFixture<RegisterComponent>;
  let authService: AuthService;
  let router: Router;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ RegisterComponent ],
      imports: [
        BrowserAnimationsModule,
        HttpClientModule,
        ReactiveFormsModule,
        MatCardModule,
        MatFormFieldModule,
        MatIconModule,
        MatInputModule
      ],
      providers: [ AuthService ]
    }).compileComponents();

    fixture = TestBed.createComponent(RegisterComponent);
    component = fixture.componentInstance;
    authService = TestBed.inject(AuthService);
    router = TestBed.inject(Router);
    fixture.detectChanges();
  });

  it('should initialize the form', () => {
    const formElement = fixture.debugElement.query(By.css('form')).nativeElement;
    expect(formElement).toBeTruthy();
    expect(component.form.valid).toBeFalsy();
  });

  it('should enable the submit button when the form is valid', () => {
    fillValidFormData();
    fixture.detectChanges();
    const submitButton = fixture.debugElement.query(By.css('button[type="submit"]')).nativeElement;
    expect(submitButton.disabled).toBe(false);
  });

  it('should navigate to login on successful registration', () => {
    jest.spyOn(authService, 'register').mockReturnValue(of(undefined));
    jest.spyOn(router, 'navigate');

    fillValidFormData();
    submitForm();

    expect(authService.register).toHaveBeenCalled();
    expect(router.navigate).toHaveBeenCalledWith(['/login']);
  });

  it('should display an error message on registration failure', () => {
    jest.spyOn(authService, 'register').mockImplementation(() =>
      throwError('Failed registration'));

    fillValidFormData();
    submitForm();

    fixture.detectChanges();
    const errorMessage = fixture.debugElement.query(By.css('.error')).nativeElement;
    expect(errorMessage).toBeTruthy();
    expect(component.onError).toBe(true);
  })
  const fillValidFormData = () => {
    component.form.controls['firstName'].setValue('Class');
    component.form.controls['lastName'].setValue('Rooms');
    component.form.controls['email'].setValue('openclassrooms@gmail.com');
    component.form.controls['password'].setValue('123456');
  }

  const submitForm = ()  => {
    const form = fixture.debugElement.query(By.css('form')).nativeElement;
    form.dispatchEvent(new Event('submit'));
  }
});
