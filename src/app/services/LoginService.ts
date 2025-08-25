import {Injectable, signal} from '@angular/core';
import { HttpClient } from '@angular/common/http';
import {BehaviorSubject, Observable, tap, throwError} from 'rxjs';
import {catchError} from 'rxjs/operators';
import { jwtDecode } from 'jwt-decode';
import {User} from '../models/User';

@Injectable({
  providedIn: 'root'
})
export class LoginService {

  private url = 'http://localhost:8080/auth';

  private currentUserSubject = new BehaviorSubject<any>(null);
  tokenSubject = new BehaviorSubject<string | null>(null);
  private currentUser = signal<User | null>(null);
  role: string | null = null;
  userId: number | null = null;
  constructor(private http: HttpClient) {
    const savedToken = sessionStorage.getItem('authToken');
    this.initializeAuthState();
    if (savedToken) {
      this.tokenSubject.next(savedToken);
    }}


  initializeAuthState() {
    const token = sessionStorage.getItem('authToken');
    if (token && !this.isTokenExpired(token)) {
      this.tokenSubject.next(token);
      const decoded: any = jwtDecode(token);
      this.role = decoded.role;
      this.userId = decoded.id;
      this.currentUserSubject.next({
        id: this.userId,
        name: decoded.name || decoded.email?.split('@')[0],
        role: decoded.role
      });
    }
  }

  private isTokenExpired(token: string): boolean {
    const decoded = jwtDecode(token);
    return decoded.exp ? decoded.exp * 1000 < Date.now() : false;
  }

  login(email: string, password: string) {
    return this.http.post<{token: string}>(`${this.url}/login`, { email, password }).pipe(
      tap(response => {
        const token = response.token;
        this.tokenSubject.next(token);
        sessionStorage.setItem('authToken', token);
        const decoded: any = jwtDecode(token);
        this.role = decoded.role;
        this.userId = decoded.id
        this.currentUserSubject.next({
          id: this.userId,
          name: decoded.name || decoded.email.split('@')[0],
          role: decoded.role
        });
      }),
      catchError(error => {
        console.error('Erreur:', error);
        return throwError(() => new Error(error.error?.message || 'Erreur de connexion'));
      })
    );
  }

  getRole(): string | null {
    return this.role ;
  }
  getToken(): string | null {
    return this.tokenSubject.value;
  }

  logout() {
    this.tokenSubject.next(null);
    sessionStorage.removeItem('authToken');
  }

  isAuthenticated(): boolean {
    return !!this.tokenSubject.value;
  }

  setRole(role: string) {
    this.role = role;
  }
  getCurrentUser() {
    return this.currentUserSubject.value;
  }

  currentUser$(): Observable<any> {
    return this.currentUserSubject.asObservable();
  }

  getUserId(): number | null {
    return this.userId;
  }
  updateCurrentUser(user: User): void {
    this.currentUser.set(user);

  }
}
