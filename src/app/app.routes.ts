import { Routes } from '@angular/router';

import {DemandesComponent} from './demandes/demandes.component';
import {UsersComponent} from './users/users.component';
import {MaterielsComponent} from './materiels/materiels.component';
import {AjouterDemandeComponent} from './ajouter-demande/ajouter-demande.component';
import {LayoutComponent} from './core/layout/layout/layout.component';
import {LoginComponent} from './login/login.component';
import {TraiterDemandeComponent} from './traiter-demande/traiter-demande.component';

export const routes: Routes = [
 {path: "ajout",component:AjouterDemandeComponent},
  {path: "login",component:LoginComponent},
  {path: "traiter-demandes",component:TraiterDemandeComponent},/*
  {path: "ajout",component:AjouterDemandeComponent},
  {path: "users",component:UsersComponent},*/
  {
    path: '',
    component: LayoutComponent,
    children: [
      { path: 'demandes', component: DemandesComponent },
      { path: 'materiels', component: MaterielsComponent },
      { path: 'users', component: UsersComponent },

      { path: '', redirectTo: 'demandes', pathMatch: 'full' },

    ]
  }
];
