import {ComponentFixture, fakeAsync, flush, TestBed, tick} from "@angular/core/testing";
import {HttpClientTestingModule, HttpTestingController} from "@angular/common/http/testing";
import {ActivatedRoute, Router} from "@angular/router";
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
import {Session} from "../../interfaces/session.interface";
import {Teacher} from "../../../../interfaces/teacher.interface";
import {ListComponent} from "../list/list.component";

describe('Form Component integration', () => {
  let component : FormComponent;
  let fixture : ComponentFixture<FormComponent>;
  let httpTestingController: HttpTestingController;
  let router : Router;
  let matSnackBar : MatSnackBar;

  const mockSession: Session = {
    id: 1,
    name: "session test",
    description: "description de la session",
    date: new Date(),
    teacher_id: 1,
    users: [2, 3],
  };

  const mockTeacher: Teacher = {
    id: 1,
    lastName: "Class",
    firstName: "Rooms",
    createdAt: new Date("2024-01-23"),
    updatedAt: new Date("2024-01-23")
  };

  const sessionInformation : SessionInformation =  {
    token :"1",
    type : "class",
    id: 1,
    username : "Class",
    firstName : "Open",
    lastName : "rooms",
    admin : true
  };

  const mockActivatedRoute = {
    snapshot: {
      paramMap: new Map([['id', '1']])
    }
  };

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations : [FormComponent],
      imports : [
        RouterTestingModule.withRoutes([
            { path: 'sessions', component: ListComponent }]),
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
    } },
        { provide: ActivatedRoute, useValue: mockActivatedRoute },],
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

  describe('Submit', () => {
    beforeEach(() => {
      jest.spyOn(router, 'navigate');
      jest.spyOn(matSnackBar, "open");
      component.teachers$ = of([mockTeacher])
    })
    it('should update session when you click submit button', async () => {
      await router.navigate(['update', '1']);
      fixture.detectChanges();
      expect(router.url).toBe('/update/1');

      const reqDetail = httpTestingController.expectOne('api/session/1');
      reqDetail.flush(mockSession);

      fixture.detectChanges();

      expect(component.sessionForm).toBeDefined();

      const buttonSubmit = fixture.debugElement.query(By.css('button[type="submit"]')).nativeElement
      expect(buttonSubmit).toBeTruthy()

     await buttonSubmit.click()

      const req = httpTestingController.expectOne('api/session/1')
      req.flush(null)
      expect(req.request.method).toEqual("PUT");
      expect(router.navigate).toHaveBeenCalledWith(['sessions'])
      expect(matSnackBar.open).toHaveBeenCalledWith('Session updated !', 'Close', { duration: 3000 })
    });

    it('should create session when you click on submit button', fakeAsync(() => {
      router.navigate(['create']);
      tick();
      fixture.detectChanges();

      component.sessionForm!.patchValue({
        name: 'New Session',
        date: '2023-01-01',
        teacher_id: '1',
        description: 'Description of the new session'
      });

      fixture.detectChanges();

      const buttonSubmit = fixture.debugElement.query(By.css('button[type="submit"]')).nativeElement;
      buttonSubmit.click();
      tick();

      fixture.detectChanges();

      const req = httpTestingController.expectOne(req =>
        req.url ==='api/session' && req.method === "POST");
      expect(req.request.body).toEqual({
        name: 'New Session',
        date: '2023-01-01',
        teacher_id: '1',
        description: 'Description of the new session'
      });
      req.flush(null);
      flush();
      expect(router.navigate).toHaveBeenCalledWith(['sessions']);
      expect(matSnackBar.open).toHaveBeenCalledWith('Session created !', 'Close', { duration: 3000 });
    }));
  });
});
