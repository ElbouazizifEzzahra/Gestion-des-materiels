import { Component } from '@angular/core';
import {Router, RouterLink, RouterOutlet} from '@angular/router';
import {NavbarComponent} from '../../navbar/navbar.component';
import {LoginService} from '../../../services/LoginService';

@Component({
  selector: 'app-admin-sidebar',
  imports: [
    RouterLink,
    NavbarComponent,
    RouterOutlet
  ],
  templateUrl: './admin-sidebar.component.html',
  styleUrl: './admin-sidebar.component.css',
  standalone:true
})
export class AdminSidebarComponent {
  currentUser = { name: '' };
  isSidebarOpen = true;
  constructor(
    private loginService: LoginService,
    private router: Router
  ) {}

  logout() {
    this.loginService.logout();
    this.router.navigate(['/login']);
  }
  toggleSidebar() {
    this.isSidebarOpen = !this.isSidebarOpen;
  }
}
