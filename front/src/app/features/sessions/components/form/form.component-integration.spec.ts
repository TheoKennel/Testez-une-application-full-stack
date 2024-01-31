import {ComponentFixture, fakeAsync, TestBed, tick} from "@angular/core/testing";
import {HttpClientTestingModule, HttpTestingController} from "@angular/common/http/testing";
import {Router} from "@angular/router";
import {MatSnackBar, MatSnackBarModule} from "@angular/material/snack-bar";
import {SessionInformation} from "../../../../interfaces/sessionInformation.interface";
import {expect} from '@jest/globals';
import {FormComponent} from "./form.component";
import {RouterTestingModule} from "@angular/router/testing";
import {HttpClientModule} from "@angular/common/http";
import {ReactiveFormsModule} from "@angular/forms";
import {MatCardModule} from "@angular/material/card";
import {BrowserAnimationsModule} from "@angular/platform-browser/animations";
import {MatIconModule} from "@angular/material/icon";
import {SessionService} from "../../../../services/session.service";
import {of} from "rxjs";
import {SessionsRoutingModule} from "../../sessions-routing.module";
import {By} from "@angular/platform-browser";

describe('Form Component integration', () => {
  let component : FormComponent;
  let fixture : ComponentFixture<FormComponent>;
  let httpTestingController: HttpTestingController;
  let router : Router;
  let matSnackBar : MatSnackBar;

  const sessionInformation : SessionInformation =  {
    token :"1",
    type : "class",
    id: 1,
    username : "Class",
    firstName : "Open",
    lastName : "rooms",
    admin : true
  };

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations : [FormComponent],
      imports : [
        RouterTestingModule,
        SessionsRoutingModule,
        HttpClientTestingModule,
        HttpClientModule,
        MatSnackBarModule,
        ReactiveFormsModule,
        MatCardModule,
        BrowserAnimationsModule,
        MatIconModule,
      ],
      providers: [
    { provide: SessionService, useValue: {
      sessionInformation: sessionInformation,
        isLogged: true,
        $isLogged: () => of(true)
    } }],
    }).compileComponents()

    httpTestingController = TestBed.inject(HttpTestingController);
    router = TestBed.inject(Router);
    matSnackBar = TestBed.inject(MatSnackBar);
    fixture = TestBed.createComponent(FormComponent);
    component = fixture.componentInstance;
  });

  it('should create', () => {
    expect(component).toBeDefined()
  });

  it('should display update session and form', fakeAsync(() => {
    router.navigate(['update/1'])
    tick()
    fixture.detectChanges()
    expect(component.onUpdate).toBe(true);
    const updateSessionTitle = fixture.debugElement.query(By.css('.update-session-title')).nativeElement
    expect(updateSessionTitle).toBeTruthy();
    expect(updateSessionTitle.textContent).toContain("Update session")
  }));
});
