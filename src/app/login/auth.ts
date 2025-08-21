
import { Injectable } from '@angular/core';
import {
  HttpRequest,
  HttpHandler,
  HttpEvent,
  HttpInterceptor
} from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { LoginService } from '../services/LoginService';
import { catchError, tap } from 'rxjs/operators';
import {jwtDecode} from 'jwt-decode';
import {Router} from '@angular/router';

@Injectable()
export class AuthInterceptor implements HttpInterceptor {
  constructor(private loginService: LoginService, private router: Router) {}

  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    const headers: Record<string, string> = {};
    const token = this.loginService.getToken();
    const url = req.url;


    if (token) {
      const decoded: any = jwtDecode(token);
      if (decoded.exp * 1000 < Date.now()) {
        console.log('Token expiré - logout');
        this.loginService.logout();
        this.router.navigate(['/login']);
        return throwError(() => new Error('Token expiré'));
      }
      headers['Authorization'] = `Bearer ${token}`;
    }

    const authReq = req.clone({ setHeaders: headers, withCredentials: true });
    return next.handle(authReq).pipe(
      catchError(err => {
        if (err.status === 401) this.loginService.logout();
        return throwError(() => err);
      })
    );

    console.log(`Intercepting request to: ${url}`);

    if (token) {
      headers['Authorization'] = `Bearer ${token}`;
      console.log('JWT token added to headers');
    } else {
      console.warn('No token available - unauthenticated request');
    }

    if (['POST', 'PUT', 'DELETE', 'PATCH'].includes(req.method)) {
      const csrfToken = this.getCsrfToken();
      if (csrfToken) {
        headers['X-XSRF-TOKEN'] = csrfToken;
        console.log(`CSRF token added: ${csrfToken}`);
      } else {
        console.warn('No CSRF token found for non-GET request');
      }
    }



    console.log('Final request headers:', authReq.headers);

    return next.handle(authReq).pipe(
      tap(event => console.log('Response event:', event)),
      catchError(err => {
        console.error('HTTP Error:', err);

        if (err.status === 401) {
          console.log('401 Unauthorized - triggering logout');
          this.loginService.logout();
        }

        if (err.status === 403) {
          console.error('403 Forbidden - possible causes:');
          console.error('- Insufficient permissions');
          console.error('- CSRF token mismatch');
          console.error('- CORS configuration issue');
        }

        return throwError(() => err);
      })
    );
  }

  private getCsrfToken(): string {
    const match = document.cookie.match(/XSRF-TOKEN=([^;]+)/);
    return match ? decodeURIComponent(match[1]) : '';
  }

}
