import {Component, inject, OnInit, signal} from '@angular/core';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import {DemandeService} from '../services/DemandeService';
import {LayoutComponent} from '../core/layout/layout/layout.component';
import {NavbarComponent} from '../core/layout/layout/navbar/navbar.component';


@Component({
  selector: 'app-demandes',
  imports: [
    ReactiveFormsModule,
    FormsModule,
    LayoutComponent,
    NavbarComponent
  ],
  templateUrl: './demandes.component.html',
  styleUrl: './demandes.component.css',
  standalone:true
})
export class DemandesComponent {
  private demandeService = inject(DemandeService);
  demandes = this.demandeService.demandes;


  expandedDemandeId = signal<number | null>(null);

  toggleExpand(demandeId: number): void {

    if (this.expandedDemandeId() === demandeId) {
      this.expandedDemandeId.set(null);
    } else {
      this.expandedDemandeId.set(demandeId);
    }
  }


}
