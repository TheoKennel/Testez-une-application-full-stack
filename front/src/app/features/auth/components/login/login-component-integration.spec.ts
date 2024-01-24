import {LoginComponent} from "./login.component";
import {ComponentFixture, TestBed} from "@angular/core/testing";
import {AuthService} from "../../services/auth.service";
import {Router} from "@angular/router";
import {ReactiveFormsModule} from "@angular/forms";
import {RouterTestingModule} from "@angular/router/testing";
import {BrowserAnimationsModule} from "@angular/platform-browser/animations";
import {HttpClientModule} from "@angular/common/http";
import {MatCardModule} from "@angular/material/card";
import {MatFormFieldModule} from "@angular/material/form-field";
import {MatIconModule} from "@angular/material/icon";
import {MatInputModule} from "@angular/material/input";
import {SessionService} from "../../../../services/session.service";
import {By} from "@angular/platform-browser";
import { expect } from '@jest/globals';
import {of, throwError} from "rxjs";

describe("LoginComponent Integration", () => {
  let component : LoginComponent;
  let fixture: ComponentFixture<LoginComponent>;
  let authService : AuthService;
  let router: Router;
  let sessionService : SessionService;

  beforeEach(async() => {
    await TestBed.configureTestingModule({
      declarations: [ LoginComponent ],
      imports: [
        RouterTestingModule,
        BrowserAnimationsModule,
        HttpClientModule,
        MatCardModule,
        MatIconModule,
        MatFormFieldModule,
        MatInputModule,
        ReactiveFormsModule],
      providers: [ AuthService ]
    }).compileComponents();

    fixture = TestBed.createComponent(LoginComponent);
    component = fixture.componentInstance;
    authService = TestBed.inject(AuthService)
    router = TestBed.inject(Router);
    sessionService = TestBed.inject(SessionService)
    fixture.detectChanges()
  });

  it('should initialize the form', () => {
    const formElement = fixture.debugElement.query(By.css('form')).nativeElement;
    expect(formElement).toBeTruthy()
    expect(component.form.valid).toBeFalsy()
  })

  it('should enable the submit button when the form is valid', () => {
    fillValidFormData();
    fixture.detectChanges();
    const submitButton = fixture.debugElement.query(By.css('button[type="submit"]')).nativeElement;
    expect(submitButton.disabled).toBe(false);
  });

  it('should navigate to home on successful sign in', () => {
     const sessionInformation = {
       token : "token",
       type : "type",
       id : 1,
       username: "class",
       firstName: "open",
       lastName: "rooms",
       admin: false
     }
    jest.spyOn(authService, 'login').mockReturnValue(of(sessionInformation));
    jest.spyOn(sessionService, 'logIn')
    jest.spyOn(router, 'navigate');

    fillValidFormData();
    submitForm();

    expect(authService.login).toHaveBeenCalled();
    expect(sessionService.logIn).toHaveBeenCalled();
    expect(router.navigate).toHaveBeenCalledWith(['/sessions']);
  });

  it('should display an error message when sign in failure', () => {
    jest.spyOn(authService, 'login').mockImplementation(() =>
      throwError('Failed registration'));

    fillValidFormData();
    submitForm();

    fixture.detectChanges();
    const errorMessage = fixture.debugElement.query(By.css('.error')).nativeElement;
    expect(errorMessage).toBeTruthy();
    expect(component.onError).toBe(true);
  })

  const fillValidFormData = () => {
    component.form.controls['email'].setValue('openclassrooms@gmail.com');
    component.form.controls['password'].setValue('123456');
  }

  const submitForm = ()  => {
    const form = fixture.debugElement.query(By.css('form')).nativeElement;
    form.dispatchEvent(new Event('submit'));
  }
})
