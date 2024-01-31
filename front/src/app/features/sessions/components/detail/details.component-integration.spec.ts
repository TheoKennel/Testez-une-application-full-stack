import {ComponentFixture, fakeAsync, TestBed, tick} from "@angular/core/testing";
import {DetailComponent} from "./detail.component";
import { Router, Routes} from "@angular/router";
import {expect} from '@jest/globals';
import {HttpClientTestingModule, HttpTestingController} from "@angular/common/http/testing";
import {SessionService} from "../../../../services/session.service";
import {RouterTestingModule} from "@angular/router/testing";
import {HttpClientModule} from "@angular/common/http";
import {BrowserAnimationsModule} from "@angular/platform-browser/animations";
import {MatCardModule} from "@angular/material/card";
import {MatIconModule} from "@angular/material/icon";
import {ReactiveFormsModule} from "@angular/forms";
import {SessionInformation} from "../../../../interfaces/sessionInformation.interface";
import {By} from "@angular/platform-browser";
import {MatSnackBar, MatSnackBarModule} from "@angular/material/snack-bar";
import {of} from "rxjs";
import {Session} from "../../interfaces/session.interface";
import {Teacher} from "../../../../interfaces/teacher.interface";
import {ListComponent} from "../list/list.component";
import {SessionsRoutingModule} from "../../sessions-routing.module";

describe('DetailsComponent Integration', () => {
  let component : DetailComponent;
  let fixture : ComponentFixture<DetailComponent>;
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

  const mockSession: Session = {
    id: 1,
    name: "session test",
    description: "description de la session",
    date: new Date("2024-01-23"),
    teacher_id: 1,
    users: [1, 3],
  };

  const mockTeacher: Teacher = {
    id: 1,
    lastName: "Class",
    firstName: "Rooms",
    createdAt: new Date("2024-01-23"),
    updatedAt: new Date("2024-01-23")
  };

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [DetailComponent],
      imports: [
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
    }).compileComponents();

    httpTestingController = TestBed.inject(HttpTestingController);
    router = TestBed.inject(Router);
    matSnackBar = TestBed.inject(MatSnackBar);
    fixture = TestBed.createComponent(DetailComponent);
    component = fixture.componentInstance;
    component.sessionId = "1";
    component.ngOnInit()
    httpTestingController.expectOne(req => req.url === `api/session/${component.sessionId}`
                                                                    && req.method === 'GET').flush(mockSession);
    httpTestingController.expectOne(req => req.url === `api/teacher/${mockSession.teacher_id}`
                                                                    && req.method === 'GET').flush(mockTeacher);
    fixture.detectChanges()

  });


  it('should create', () => {
    expect(component).toBeDefined();
  });

  it('Should call delete from api, display a matSnackBar and navigates to session',() => {
    jest.spyOn(router, 'navigate');
    jest.spyOn(matSnackBar, "open");
    const deleteButton = fixture.debugElement.query(By.css('.details_delete_button'))
    expect(deleteButton).toBeTruthy()

    deleteButton.nativeElement.click()

    const req = httpTestingController.expectOne(req => req.url === `api/session/${component.sessionId}`
                                                                                              && req.method === "DELETE");
    req.flush(null)
    expect(req.request.method).toEqual('DELETE')
    expect(matSnackBar.open).toHaveBeenCalledWith('Session deleted !', 'Close', { duration: 3000 })
    expect(router.navigate).toHaveBeenCalledWith(['sessions'])
  });

  describe('Participation', () => {

    beforeEach(() => {
      component.isAdmin = false;
      component.userId = '1';
      fixture.detectChanges()
    })
    it('should add participation on click', () => {
      component.isParticipate = false;
      fixture.detectChanges()
      const addButton = fixture.debugElement.query(By.css('.details_add_participation-button'))
      expect(addButton).toBeTruthy();

      addButton.nativeElement.click();

      const req = httpTestingController.expectOne(`api/session/1/participate/${component.userId}`)
      req.flush(null)
      expect(req.request.method).toEqual("POST")
    })

    it('should remove participation on click', () => {
      component.isParticipate = true;

      const removeButton = fixture.debugElement.query(By.css('.details_remove_participation-button'))
      expect(removeButton).toBeTruthy();

      removeButton.nativeElement.click();

      const req = httpTestingController.expectOne(req => req.url === `api/session/1/participate/${component.userId}`
                                                                      && req.method === 'DELETE')
      req.flush(null)
      expect(req.request.method).toEqual("DELETE")
    });
  });
})
