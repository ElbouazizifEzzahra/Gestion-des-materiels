import {Component, inject} from '@angular/core';
import {Materiel} from '../models/Materiel';
import {MaterielService} from '../services/MaterielService';
import {Observable} from 'rxjs';

@Component({
  selector: 'app-ajouter-demande',
  imports: [],
  templateUrl: './ajouter-demande.component.html',
  styleUrl: './ajouter-demande.component.css',
  standalone:true
})
export class AjouterDemandeComponent {

  private materielService = inject(MaterielService);
  materiels: Materiel[] = [];
  constructor() {
    this.loadMateriels();
  }

  loadMateriels(): void {
    this.materielService.getMateriels().subscribe({
      next: (mats) => {
        this.materiels = mats;
      },
      error: (err) => console.error('Erreur chargement matériels:', err)
    });
  }


  onMaterielSelect(materielId: number, itemIndex: number): void {
    const selected = this.materiels.find(m => m.idMateriel === materielId);
    if (selected) {
      //this.demandItems[itemIndex].types = selected.typesDisponibles || [];
      this.demandItems[itemIndex].selectedType = '';
    }
  }

  demandItems: any[] = [{}];


  addRow(): void {
        this.demandItems.push({});
      console.log('Nouvelle ligne ajoutée:', this.demandItems);
  }

  removeRow(index: number): void {
    if (this.demandItems.length > 1) {
      this.demandItems.splice(index, 1);
    }
  }


}
