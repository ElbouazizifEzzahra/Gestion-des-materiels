import { Component } from '@angular/core';
import {Router, RouterLink, RouterOutlet} from '@angular/router';
import {NavbarComponent} from '../../navbar/navbar.component';
import {LoginService} from '../../../services/LoginService';

@Component({
  selector: 'app-employee-sidebar',
  imports: [
    RouterOutlet,
    NavbarComponent,
    RouterLink
  ],
  templateUrl: './employee-sidebar.component.html',
  standalone:true,
  styleUrl: './employee-sidebar.component.css'
})
export class EmployeeSidebarComponent {
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

}}
