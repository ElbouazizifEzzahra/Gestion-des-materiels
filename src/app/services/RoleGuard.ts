
import { Injectable } from '@angular/core';
import { CanActivate, ActivatedRouteSnapshot, RouterStateSnapshot, UrlTree, Router } from '@angular/router';
import {Observable, take} from 'rxjs';
import { LoginService } from '../services/LoginService';
import {map} from 'rxjs/operators';

@Injectable({
  providedIn: 'root'
})
export class RoleGuard implements CanActivate {
  constructor(private loginService: LoginService, private router: Router) {
  }

  canActivate(
    next: ActivatedRouteSnapshot,
    state: RouterStateSnapshot): Observable<boolean | UrlTree> | Promise<boolean | UrlTree> | boolean | UrlTree {

    return this.loginService.tokenSubject.pipe(
      take(1),
      map(token => {
        if (!token) {
          this.router.navigate(['/login']);
          return false;
        }

        const requiredRoles = next.data['roles'];
        const userRole = this.loginService.getRole();

        if (!requiredRoles.includes(userRole)) {
          this.router.navigate(['/acces-interdit']);
          return false;
        }

        return true;
      })
    );
  }
}
