import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import {BehaviorSubject, Observable, tap, throwError} from 'rxjs';
import {catchError} from 'rxjs/operators';

@Injectable({
  providedIn: 'root'
})
export class LoginService {
  private url = 'http://localhost:8080/auth';
  private tokenSubject = new BehaviorSubject<string | null>(null);
  constructor(private http: HttpClient) {
    // Initialise avec le token du localStorage s'il existe
    const savedToken = localStorage.getItem('authToken');
    if (savedToken) {
      this.tokenSubject.next(savedToken);
    }}

  login(email: string, password: string) {
    return this.http.post<{token: string}>(`${this.url}/login`, { email, password }).pipe(
      tap(response => {
        const token = response.token;
        this.tokenSubject.next(token);
        localStorage.setItem('authToken', token); // Stocke le token
      }),
      catchError(error => {
        console.error('Erreur:', error);
        return throwError(() => new Error(error.error?.message || 'Erreur de connexion'));
      })
    );
  }
  getToken(): string | null {
    return this.tokenSubject.value;
  }

  logout() {
    this.tokenSubject.next(null);
    localStorage.removeItem('authToken');
  }

  isAuthenticated(): boolean {
    return !!this.tokenSubject.value;
  }
}
