import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Teacher } from '../interfaces/teacher.interface';

@Injectable({
  providedIn: 'root'
})
export class TeacherService {

  private pathService = 'api/teacher';

  constructor(private httpClient: HttpClient) { }

  public all(): Observable<Teacher[]> {
    return this.httpClient.get<Teacher[]>("http://localhost:8080/api/teacher");
  }

  public detail(id: string): Observable<Teacher> {
    return this.httpClient.get<Teacher>(`http://localhost:8080/api/teacher/${id}`);
  }
}
