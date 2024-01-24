import { HttpClientModule } from '@angular/common/http';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import {MatSnackBar, MatSnackBarModule} from '@angular/material/snack-bar';
import { SessionService } from 'src/app/services/session.service';
import { expect } from '@jest/globals';

import { MeComponent } from './me.component';
import {of} from "rxjs";
import {UserService} from "../../services/user.service";
import {Router} from "@angular/router";

describe('MeComponent', () => {
  let component: MeComponent;
  let fixture: ComponentFixture<MeComponent>;

  const mockSessionService = {
    sessionInformation: {
      admin: true,
      id: 1
    },
    logOut : jest.fn()
  }
  const mockUser = {
    id: 1,
    email: 'openclassrooms@gmail.com',
    lastName: 'Class',
    firstName: 'Open'
  };

  const mockUserService = {
    getById: jest.fn().mockReturnValue(of(mockUser)),
    delete : jest.fn().mockReturnValue(of({}))
  };

  const mockMatSnackBar = {
    open : jest.fn()
  }

  const mockRouter = {
    navigate : jest.fn()
  }

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [MeComponent],
      imports: [
        MatSnackBarModule,
        HttpClientModule,
        MatCardModule,
        MatFormFieldModule,
        MatIconModule,
        MatInputModule
      ],
      providers: [
        { provide: SessionService, useValue: mockSessionService },
        { provide: UserService, useValue: mockUserService},
        { provide: MatSnackBar, useValue: mockMatSnackBar },
        { provide: Router, useValue: mockRouter }
      ],
    })
      .compileComponents();

    fixture = TestBed.createComponent(MeComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    // @ts-ignore
    expect(component).toBeTruthy();
  });

describe('NgOnInit', () => {
  it('should call userService.getById with session ID and update user', () => {
    component.ngOnInit()

    expect(component.user).toEqual(mockUser)
    // @ts-ignore
    expect(mockUserService.getById).toHaveBeenCalledWith("1")
  });
});

describe('back', () => {
  it('should go back', () => {
    const historySpy = jest.spyOn(window.history, 'back')
    component.back()
    expect(historySpy).toHaveBeenCalled();
  });
});

describe('delete', () => {
  beforeEach(() => {
    component.delete();
  });

  it('should open a snackbar with deletion message', () => {
    expect(mockMatSnackBar.open).toHaveBeenCalledWith("Your account has been deleted !", 'Close', { duration: 3000 });
  });

  it('should call logOut from sessionService', () => {
    expect(mockSessionService.logOut).toHaveBeenCalled();
  });

  it('should navigate to home page', () => {
    expect(mockRouter.navigate).toHaveBeenCalledWith(['/']);
  });
});
});
