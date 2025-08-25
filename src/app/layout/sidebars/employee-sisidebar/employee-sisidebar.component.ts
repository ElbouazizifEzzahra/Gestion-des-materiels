import { Component } from '@angular/core';
import {Router, RouterLink, RouterOutlet} from '@angular/router';
import {NavbarComponent} from '../../navbar/navbar.component';
import {LoginService} from '../../../services/LoginService';

@Component({
  selector: 'app-employee-sisidebar',
  imports: [
    RouterLink,
    NavbarComponent,
    RouterOutlet
  ],
  templateUrl: './employee-sisidebar.component.html',
  styleUrl: './employee-sisidebar.component.css',
  standalone:true
})
export class EmployeeSISidebarComponent {
  currentUser = { name: '' };
  isSidebarOpen = true;
  constructor(
    private loginService: LoginService,
    private router: Router
  ) {}
  toggleSidebar() {
    this.isSidebarOpen = !this.isSidebarOpen;
  }
  logout() {
    this.loginService.logout();
    this.router.navigate(['/login']);
  }
}
