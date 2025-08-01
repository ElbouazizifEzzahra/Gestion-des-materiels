
import { Injectable } from '@angular/core';
import {
  HttpRequest,
  HttpHandler,
  HttpEvent,
  HttpInterceptor
} from '@angular/common/http';
import {Observable, throwError} from 'rxjs';
import {LoginService} from '../services/LoginService';
import {catchError} from 'rxjs/operators';


@Injectable()
export class auth implements HttpInterceptor {

  constructor(private loginService: LoginService) {}
  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    const token = this.loginService.getToken();

    if (token) {
      req = req.clone({
        setHeaders: {
          Authorization: `Bearer ${token}`
        }
      });
    } else {
      console.warn('Aucun token disponible - requête non authentifiée');
    }

    return next.handle(req);
  }
}
