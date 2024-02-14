  import { HttpClient } from '@angular/common/http';
  import { Injectable } from '@angular/core';
  import { Observable } from 'rxjs';
  import { Session } from '../interfaces/session.interface';

  @Injectable({
    providedIn: 'root'
  })
  export class SessionApiService {

    private pathService = 'api/session';

    constructor(private httpClient: HttpClient) {
    }

    public all(): Observable<Session[]> {
      return this.httpClient.get<Session[]>("http://localhost:8080/api/session");
    }

    public detail(id: string): Observable<Session> {
      return this.httpClient.get<Session>(`http://localhost:8080/api/session/${id}`);
    }

    public delete(id: string): Observable<any> {
      return this.httpClient.delete(`http://localhost:8080/api/session/${id}`);
    }

    public create(session: Session): Observable<Session> {
      return this.httpClient.post<Session>("http://localhost:8080/api/session", session);
    }

    public update(id: string, session: Session): Observable<Session> {
      return this.httpClient.put<Session>(`http://localhost:8080/api/session/${id}`, session);
    }

    public participate(id: string, userId: string): Observable<void> {
      return this.httpClient.post<void>(`http://localhost:8080/api/session/${id}/participate/${userId}`, null);
    }

    public unParticipate(id: string, userId: string): Observable<void> {
      return this.httpClient.delete<void>(`http://localhost:8080/api/session/${id}/participate/${userId}`);
    }

  }
