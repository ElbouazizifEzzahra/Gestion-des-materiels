import {Component, OnInit} from '@angular/core';
import {RouterOutlet} from '@angular/router';
import {LoginService} from '../../services/LoginService';
import {User} from '../../models/User';
import {Observable, of} from 'rxjs';

@Component({
  selector: 'app-navbar',
  imports: [
    RouterOutlet
  ],
  templateUrl: './navbar.component.html',
  styleUrl: './navbar.component.css',
  standalone:true
})
export class NavbarComponent implements OnInit {
  currentUser$: Observable<any>= of(null);
  isSidebarOpen = true;
  userInitial: string = 'U';

  ngOnInit() {
    this.currentUser$ = this.loginService.currentUser$();

    // Pour l'initiale
    this.currentUser$.subscribe(user => {
      if (user?.name) {
        this.userInitial = user.name.charAt(0).toUpperCase();
      }
    });
  }
  constructor(private loginService: LoginService) {}
  toggleSidebar() {
    this.isSidebarOpen = !this.isSidebarOpen;
  }



}
