import {Component, inject, signal} from '@angular/core';
import {DemandeService} from '../services/DemandeService';

@Component({
  selector: 'app-traiter-demande',
  imports: [],
  templateUrl: './traiter-demande.component.html',
  styleUrl: './traiter-demande.component.css',
  standalone:true
})
export class TraiterDemandeComponent {
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
