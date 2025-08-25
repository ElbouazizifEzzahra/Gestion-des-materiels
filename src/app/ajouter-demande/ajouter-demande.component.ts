
import {Component, ElementRef, OnInit, ViewChild} from '@angular/core';
import { FormsModule } from '@angular/forms';

import { CommonModule } from '@angular/common';
import {MaterielService} from '../services/MaterielService';
import {LigneDemandeService} from '../services/ligneDemandeService';

@Component({
  selector: 'app-ajouter-demande',
  templateUrl: './ajouter-demande.component.html',
  styleUrls: ['./ajouter-demande.component.css'],
  imports: [FormsModule, CommonModule],
  standalone: true
})
export class AjouterDemandeComponent implements OnInit {
  materiels: any[] = [];
  selectedItems: any[] = [];

  errorMessage: string | null = null;

  onFileSelected(event: any): void {
    const file: File = event.target.files[0];
    if (file && file.type === 'application/pdf') {
      this.newItem.ficheTechniqueFile = file;
    } else {
      console.warn('Fichier non PDF ignoré :', file?.name);
    }
  }


  newItem: any = {
    materiel: null,
    quantite: 1,
    description: '',
    ficheTechnique: '',
    typeDestinataire: 'EMPLOYE',
    nomDestinataire: '',
    fonctionDestinataire: '',
    prenomDestinataire: '',
    emailDestinataire: '',
    projetDestinataire: ''
  };
  acceptedFileTypes = '.pdf';
  constructor(
    private materielService: MaterielService,
    private ajoutDemandeService: LigneDemandeService
  ) {}

  ngOnInit() {
    this.loadMateriels();
  }

  loadMateriels() {
    this.materielService.getMateriels().subscribe({
      next: (data) => {
        this.materiels = data;
      }
    });
  }

  addItem() {
    if (this.newItem.materiel) {
      this.selectedItems.push({
        materielId: this.newItem.materiel.idMateriel,
        nomMateriel: this.newItem.materiel.nomMateriel,
        typeMateriel: this.newItem.materiel.typeMateriel,
        quantite: this.newItem.quantite,
        description: this.newItem.description,
        ficheTechnique: this.newItem.ficheTechniqueFile,
        fonctionDestinataire: this.newItem.fonctionDestinataire,
        nomDestinataire: this.newItem.nomDestinataire,
        prenomDestinataire: this.newItem.prenomDestinataire,
        emailDestinataire: this.newItem.emailDestinataire,
        projetDestinataire: this.newItem.projetDestinataire,
        typeDestinataire: this.newItem.typeDestinataire
      });
      this.resetNewItem();
    }
  }

  removeItem(index: number) {
    this.selectedItems.splice(index, 1);
  }


  fichiers: File[] = [];

  submitDemande() {
    if (this.selectedItems.length === 0) {
      this.errorMessage = 'Aucune ligne de demande n\'a été ajoutée.';
      return;
    }

    const formData = new FormData();
    const lignesForServer: any[] = [];
    let fileCounter = 0;

    this.selectedItems.forEach((item, idx) => {
      const dto: any = {
        quantite: item.quantite,
        materielId: item.materielId,
        description: item.description,
        nomDestinataire: item.nomDestinataire,
        prenomDestinataire: item.prenomDestinataire,
        fonctionDestinataire: item.fonctionDestinataire,
        emailDestinataire: item.emailDestinataire,
        projetDestinataire: item.projetDestinataire,
        typeDestinataire: item.typeDestinataire
        // fileIndex
      };

      if (item.ficheTechnique) {
        dto.fileIndex = fileCounter;
        formData.append('fichesTechniques', item.ficheTechnique, item.ficheTechnique.name);
        fileCounter++;
      } else {
        dto.fileIndex = null;
      }

      lignesForServer.push(dto);
    });

    // append JSON string (plus fiable pour Spring)
    formData.append('lignes', JSON.stringify(lignesForServer));

    this.ajoutDemandeService.ajouterLignesDemande(formData).subscribe({
      next: () => {
        this.showSuccess('Bien envoyé');
        this.selectedItems = [];
        this.resetNewItem();
      },
      error: (err) => {
        console.error('Erreur envoi:', err);
        this.showError('Échec');
      }
    });

    // debug rapide (dans console) — vérifie le contenu qu'on envoie
    for (const pair of formData.entries()) {
      console.log(pair[0], pair[1]);
    }
  }

  showSuccess(message: string): void {
    this.showNotification(message, 'bg-green-500');
  }

  showError(message: string): void {
    this.showNotification(message, 'bg-red-500');
  }


  private showNotification(message: string, bgClass: string): void {
    const notification = document.createElement('div');
    notification.innerHTML = `
      <div class="fixed justify-center top-4 right-4 p-4 rounded-md text-white ${bgClass} shadow-lg">
        ${message}
      </div>
    `;
    document.body.appendChild(notification);

    setTimeout(() => notification.remove(), 3000);
  }
  @ViewChild('fileInput') fileInput!: ElementRef<HTMLInputElement>;

  private resetNewItem() {
    this.newItem = {
      materiel: null,
      quantite: 1,
      description: '',
      typeDestinataire: 'EMPLOYE',
      fonctionDestinataire:'',
      nomDestinataire: '',
      prenomDestinataire: '',
      emailDestinataire: '',
      projetDestinataire: ''
    };
    if (this.fileInput) {
      this.fileInput.nativeElement.value = '';
    }
  }
}
