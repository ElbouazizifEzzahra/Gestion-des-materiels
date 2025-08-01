import { Component } from '@angular/core';
import {RouterLink, RouterOutlet} from '@angular/router';
import {NavbarComponent} from './navbar/navbar.component';

@Component({
  selector: 'app-layout',
  imports: [
    RouterLink,
    RouterOutlet,
    NavbarComponent
  ],
  templateUrl: './layout.component.html',
  styleUrl: './layout.component.css',
  standalone:true
})
export class LayoutComponent {
  currentUser = { name: '' };
  isSidebarOpen = true;


  toggleSidebar() {
    this.isSidebarOpen = !this.isSidebarOpen;
  }

}
