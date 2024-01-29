import {ComponentFixture, TestBed} from "@angular/core/testing";
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
import {expect} from '@jest/globals';
import {ListComponent} from "./list.component";
import {SessionApiService} from "../../services/session-api.service";
import {SessionInformation} from "../../../../interfaces/sessionInformation.interface";
import {Session} from "../../interfaces/session.interface";
import {HttpClientTestingModule, HttpTestingController} from "@angular/common/http/testing";

describe("ListComponent Integration", () => {
  let component: ListComponent;
  let fixture: ComponentFixture<ListComponent>;
  let router: Router;
  let sessionService: SessionService;
  let sessionInformation : SessionInformation =  {
    token :"45",
    type : "class",
    id: 25,
    username : "Class",
    firstName : "Open",
    lastName : "rooms",
    admin : true
  };
  let httpTestingController: HttpTestingController;
  let sessionApiService: SessionApiService

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ListComponent],
      imports: [
        RouterTestingModule,
        HttpClientTestingModule,
        HttpClientModule,
        BrowserAnimationsModule,
        MatCardModule,
        MatIconModule,
        MatFormFieldModule,
        MatInputModule,
        ReactiveFormsModule],
      providers: [ SessionApiService]
    }).compileComponents();

    fixture = TestBed.createComponent(ListComponent);
    component = fixture.componentInstance;
    sessionService = TestBed.inject(SessionService);
    sessionService.sessionInformation = sessionInformation
    httpTestingController = TestBed.inject(HttpTestingController);
    fixture.detectChanges()

  });

  it('should initialize, retrieve all sessions and display them', () => {
    const mockSessions: Session[] = [
      { name: 'Session 1', date: new Date(), description: 'Description 1', teacher_id: 1, users: [25] },
      { name: 'Session 2', date: new Date(), description: 'Description 2', teacher_id: 2, users: [] }
    ];

    const req = httpTestingController.expectOne('api/session');
    expect(req.request.method).toEqual('GET');
    req.flush(mockSessions);

    fixture.detectChanges();

    const sessionTitleElements = fixture.debugElement.queryAll(By.css('.item mat-card-title'));
    expect(sessionTitleElements.length).toEqual(2);
    expect(sessionTitleElements[0].nativeElement.textContent).toContain('Session 1')
    expect(sessionTitleElements[1].nativeElement.textContent).toContain('Session 2')
    fixture.detectChanges();
    console.log(fixture.debugElement.nativeElement.innerHTML);
  });

  it('should not display edit button for non-admin users', () => {
    sessionService.sessionInformation = {
      token: "45",
      type: "class",
      id: 25,
      username: "Class",
      firstName: "Open",
      lastName: "rooms",
      admin: false
    };
    fixture.detectChanges()

    const editButton = fixture.debugElement.query(By.css('button[routerLink*="update"]'));
    expect(editButton).toBeFalsy();
  })
});
