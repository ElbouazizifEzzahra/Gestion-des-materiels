import {Component, OnInit, signal, ViewChild} from '@angular/core';

import {LigneDemandeService} from '../services/ligneDemandeService';
import {LoginService} from '../services/LoginService';
import {Demande} from '../models/Demande';
import {HttpClient} from '@angular/common/http';
import {FormsModule} from '@angular/forms';
import {MaterielService} from '../services/MaterielService';
import {lastValueFrom} from 'rxjs';
import {DemandeService} from '../services/DemandeService';

@Component({
  selector: 'app-demandes-by-user',
  imports: [  FormsModule],
  templateUrl: './demandes-by-user.component.html',
  styleUrl: './demandes-by-user.component.css',
  standalone:true
})
export class DemandesByUserComponent implements OnInit {
  materiels: any[] = [];
  demandes: Demande[] =[];
  isLoading = true;
  error: string | null = null;
  isEditing = false;
  selectedLigne: any = null;
  selectedFile: File | null = null;
  showDeleteModal = signal<boolean>(false);
  itemToDelete = signal<{id: number, type: 'demande' | 'ligne'} | null>(null);
  constructor(private http:HttpClient,private materielService :MaterielService,
    private ligneDemandeService: LigneDemandeService,
    private loginService: LoginService,private demandeService:DemandeService
  ) {}

  onFileSelected(event: Event) {
    const input = event.target as HTMLInputElement;
    if (input.files && input.files.length > 0) {
      this.selectedFile = input.files[0];
    }
  }




  expandedDemandeId = signal<number | null>(null);

  toggleExpand(demandeId: number): void {

    if (this.expandedDemandeId() === demandeId) {
      this.expandedDemandeId.set(null);
    } else {
      this.expandedDemandeId.set(demandeId);
    }
  }
  newItem: any = {
    materiel: null,
    ficheTechnique: '',
  };
  ngOnInit() {
    this.loadMateriels();
    const Id = this.loginService.getUserId();
    if (Id) {
      this.ligneDemandeService.demandesById(Id).subscribe({
        next: (data) => this.demandes = data

      });
    }
  }
  openPdf2(filename: string) {
    this.http.get(`http://localhost:8080/uploads/${filename}`, { responseType: 'blob' })
      .subscribe(blob => {
        const url = window.URL.createObjectURL(blob);
        window.open(url);
      }, err => {
        console.error('Erreur lors du chargement du PDF', err);
      });
  }
/*  deleteLigne(id: number) {
    if (confirm('Êtes-vous sûr de vouloir supprimer cette ligne ?')) {
      this.ligneDemandeService.deleteLigneDemande(id).subscribe({
        next: () => {

          this.demandes = this.demandes.map(demande => ({
            ...demande,
            ligneDemandes: demande.ligneDemandes.filter(l => l.id !== id)
          }));
        },
        error: (err) => console.error('Erreur suppression', err)
      });
    }
  }
  deleteDemande(id: number) {
    if (confirm('Êtes-vous sûr de vouloir supprimer cette demande ?')) {
      this.demandeService.deleteDemande(id).subscribe({

      });
    }
  }*/

  deleteLigne(id: number) {
    this.ligneDemandeService.deleteLigneDemande(id).subscribe({
      next: () => {
        this.demandes = this.demandes.map(demande => ({
          ...demande,
          ligneDemandes: demande.ligneDemandes.filter(l => l.id !== id)
        }));
        this.showSuccess('Ligne supprimée avec succès ✅');
      },
      error: (err) => {
        console.error('Erreur suppression', err);
        this.showError('Erreur lors de la suppression');
      }
    });
  }

  deleteDemande(id: number) {
    this.demandeService.deleteDemande(id).subscribe({
      next: () => {
        this.demandes = this.demandes.filter(d => d.idDemande !== id);
        this.showSuccess('Demande supprimée avec succès ✅');
      },
      error: (err) => {
        console.error('Erreur suppression', err);
        this.showError('Erreur lors de la suppression');
      }
    });
  }
  openDeleteModal(id: number, type: 'demande' | 'ligne'): void {
    this.itemToDelete.set({id, type});
    this.showDeleteModal.set(true);
  }

  closeDeleteModal(): void {
    this.showDeleteModal.set(false);
    this.itemToDelete.set(null);
  }

  confirmDelete(): void {
    if (!this.itemToDelete()) return;

    const {id, type} = this.itemToDelete()!;

    if (type === 'demande') {
      this.deleteDemande(id);
    } else {
      this.deleteLigne(id);
    }

    this.closeDeleteModal();
  }
  editLigne(ligne: any) {
    this.isEditing = true;

    this.selectedLigne = {
      ...ligne,
      materiel: { ...ligne.materiel },
      destinataire: { ...ligne.destinataire }
    };

  }

  async saveChanges() {
    try {
      const updateData = {
        quantite: this.selectedLigne.quantite,
        materielId: this.selectedLigne.materiel.idMateriel,
        description: this.selectedLigne.description || null,
        nomDestinataire: this.selectedLigne.destinataire?.nom || '',
        prenomDestinataire: this.selectedLigne.destinataire?.prenom || '',
        emailDestinataire: this.selectedLigne.destinataire?.email || '',
        typeDestinataire: this.selectedLigne.destinataire?.type || 'EMPLOYE',
        fonctionDestinataire: this.selectedLigne.destinataire?.fonction || null,
        projetDestinataire: this.selectedLigne.destinataire?.projet || null,
        decharge: null,
        fileIndex: null
      };

      const formData = new FormData();
      formData.append('dto', new Blob([JSON.stringify(updateData)], { type: 'application/json' }));


      if (this.selectedFile) {
        formData.append('ficheTechnique', this.selectedFile);
      }

      const response = await lastValueFrom(
        this.ligneDemandeService.updateLigneDemande(this.selectedLigne.id, formData)
      );
      this.isEditing = false;
      this.selectedLigne = null;
      this.showSuccess('Bien modifié ✅');

    } catch (error) {
      console.error("Erreur lors de la mise à jour :", error);
    }

  }
  private updateLocalData(updatedLigne: any) {
    this.demandes = this.demandes.map(demande => ({
      ...demande,
      ligneDemandes: demande.ligneDemandes.map(l =>
        l.id === this.selectedLigne.id ? {
          ...updatedLigne,
          materiel: this.materiels.find(m => m.idMateriel === updatedLigne.materiel.idMateriel),
          destinataire: updatedLigne.destinataire
        } : l
      )
    }));

  }




  cancelEdit() {
    this.isEditing = false;
    this.selectedLigne = null;
  }
  loadMateriels() {
    this.materielService.getMateriels().subscribe({
      next: (data) => {
        this.materiels = data;
      }
    });
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
}
