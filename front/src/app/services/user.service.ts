import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { User } from '../interfaces/user.interface';

@Injectable({
  providedIn: 'root'
})
export class UserService {

  private pathService = 'api/user';

  constructor(private httpClient: HttpClient) { }

  public getById(id: string): Observable<User> {
    return this.httpClient.get<User>(`http://localhost:8080/api/user/${id}`);
  }

  public delete(id: string): Observable<any> {
    return this.httpClient.delete(`http://localhost:8080/api/user/${id}`);
  }
}
