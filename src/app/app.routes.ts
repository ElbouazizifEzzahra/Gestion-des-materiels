import { Routes } from '@angular/router';

import {DemandesComponent} from './demandes/demandes.component';
import {UsersComponent} from './users/users.component';
import {MaterielsComponent} from './materiels/materiels.component';
import {AjouterDemandeComponent} from './ajouter-demande/ajouter-demande.component';
import {LoginComponent} from './login/login.component';
import {TraiterDemandeComponent} from './traiter-demande/traiter-demande.component';
import {AdminSidebarComponent} from './layout/sidebars/admin-sidebar/admin-sidebar.component';
import {EmployeeSidebarComponent} from './layout/sidebars/employee-sidebar/employee-sidebar.component';
import {EmployeeSISidebarComponent} from './layout/sidebars/employee-sisidebar/employee-sisidebar.component';
import {RoleGuard} from './services/RoleGuard';
import {ProfileComponent} from './profile/profile.component';
import {DemandesByUserComponent} from './demandes-by-user/demandes-by-user.component';


export const routes: Routes = [
  { path: '', redirectTo: 'login', pathMatch: 'full' },
  { path: 'login', component: LoginComponent },


  {
    path: 'admin',
    component: AdminSidebarComponent,
    canActivate: [RoleGuard],
    data: { roles: ['ADMIN'] },
    children: [
      { path: '', redirectTo: 'materiels', pathMatch: 'full' },
      { path: 'demandes', component: DemandesComponent },
      { path: 'materiels', component: MaterielsComponent },
      { path: 'users', component: UsersComponent },
      {path: 'profil',component: ProfileComponent}



    ]
  },
  {
    path: 'employe',
    component: EmployeeSidebarComponent,
    canActivate: [RoleGuard],
    data: { roles: ['E'] },
    children: [
      { path: '', redirectTo: 'mes-demandes', pathMatch: 'full' },
      { path: 'ajout', component: AjouterDemandeComponent },
      { path: 'mes-demandes', component: DemandesByUserComponent },
      {path: 'profil',component: ProfileComponent}



    ]
  },
  {
    path: 'service-informatique',
    component: EmployeeSISidebarComponent,
    canActivate: [RoleGuard],
    data: { roles: ['ESI'] },
    children: [
      { path: '', redirectTo: 'traiter-demandes', pathMatch: 'full' },
      {path: "traiter-demandes",component:TraiterDemandeComponent},
      {path: 'profil',component: ProfileComponent}



    ]
  },
  {
    path: '**',
    redirectTo: 'login'
  },
];
