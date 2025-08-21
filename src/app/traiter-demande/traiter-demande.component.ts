import {Component, effect, inject, signal} from '@angular/core';
import {DemandeService} from '../services/DemandeService';
import {FormsModule} from '@angular/forms';
import {LigneDemandeService} from '../services/ligneDemandeService';
import {HttpClient} from '@angular/common/http';

@Component({
  selector: 'app-traiter-demande',
  imports: [
    FormsModule
  ],
  templateUrl: './traiter-demande.component.html',
  styleUrl: './traiter-demande.component.css',
  standalone:true
})
export class TraiterDemandeComponent {
  private demandeService = inject(DemandeService);
  private ligneDemandeService = inject(LigneDemandeService);
  private ajouterdemandeService = inject(LigneDemandeService);
  demandes = this.demandeService.demandes;
  filteredDemandes = signal<any[]>([]);
  searchTerm = signal<string>('');
constructor( private http:HttpClient) {
  this.demandes;
  effect(() => {
    this.filteredDemandes.set(this.demandes());
  }, { allowSignalWrites: true });
}
  expandedDemandeId = signal<number | null>(null);

  toggleExpand(demandeId: number): void {

    if (this.expandedDemandeId() === demandeId) {
      this.expandedDemandeId.set(null);
    } else {
      this.expandedDemandeId.set(demandeId);
    }
  }

  selectedDemande: any = null;
  selectedLigne: any = null;
  selectedState: string = '';

  openEditState(demande: any) {
    this.selectedDemande = demande;
    this.selectedState = demande.etat;
  }
  openEditState2(ligne: any) {
    this.selectedLigne = ligne;
    this.selectedState = ligne.etat;
  }
  cancelEdit() {
    this.selectedDemande = null;
  }
  cancelEdit2() {
    this.selectedLigne = null;
  }

  saveState() {
    if (this.selectedDemande) {
      this.demandeService.updateEtat(this.selectedDemande.idDemande, this.selectedState)
        .subscribe({
          next: () => {

            const index = this.demandes().findIndex(d => d.idDemande === this.selectedDemande.idDemande);
            if (index !== -1) {
              this.demandes()[index].etat = this.selectedState;
            }
            this.selectedDemande = null;

          },
          error: (err) => {
            console.error('Erreur lors de la mise à jour', err);

          }
        });
    }
  }

 /* saveState2() {
    if (this.selectedLigne) {
      this.ligneDemandeService.updateEtat(this.selectedLigne.id, this.selectedState)
        .subscribe({
    })
      this.selectedLigne = null;
  }}*/
  saveState2() {
    if (this.selectedLigne) {
      this.ligneDemandeService.updateEtat(this.selectedLigne.id, this.selectedState)
        .subscribe({
          next: () => {

            const demandeIndex = this.demandes().findIndex(d =>
              d.ligneDemandes.some((l: any) => l.id === this.selectedLigne.id)
            );

            if (demandeIndex !== -1) {
              const ligneIndex = this.demandes()[demandeIndex].ligneDemandes
                .findIndex((l: any) => l.id === this.selectedLigne.id);

              if (ligneIndex !== -1) {
                this.demandes()[demandeIndex].ligneDemandes[ligneIndex].etat = this.selectedState;
              }
            }

            this.showSuccess('État mis à jour avec succès');
            this.selectedLigne = null;
          },
          error: (err) => {
            this.showError('Erreur lors de la mise à jour');
            console.error(err);
          }
        });
    }
  }

  demandeToGenerateDecharge: any | null = null;
  ville: string = '';
  caracteristiquesText: string = '';



  openGenerateForm(demande: any) {
    this.demandeToGenerateDecharge = demande;
    this.ville = '';
    this.caracteristiquesText = '';
  }

  closeGenerateForm() {
    this.demandeToGenerateDecharge = null;
  }

  generateDecharge() {
    if (!this.ville.trim()) {
      alert('Veuillez saisir la ville');
      return;
    }
    const caracteristiques = this.caracteristiquesText
      .split('\n')
      .map(c => c.trim());
    this.ajouterdemandeService.getPDF(
      this.demandeToGenerateDecharge.id,
      this.ville,
      caracteristiques
    ).subscribe((pdfBlob) => {
      const url = window.URL.createObjectURL(pdfBlob);
      const a = document.createElement('a');
      a.href = url;
      const fileName = `decharge_${this.demandeToGenerateDecharge.id}_${Date.now()}.pdf`;
      a.download = fileName;
      document.body.appendChild(a);
      a.click();
      document.body.removeChild(a);
      window.URL.revokeObjectURL(url);
      this.closeGenerateForm();
    }, (error) => {
      alert('Erreur lors de la génération du PDF');
    });
  }
  emailAchat(id :number) {
    this.ajouterdemandeService.emailAchat(id).subscribe({
      next: () => this.showSuccess('Envoyé avec succès'),
      error: () => this.showError('Échec')
    });
  }
  onFileSelected(event: Event, ligne: any) {
    const input = event.target as HTMLInputElement;
    if (input.files && input.files.length > 0) {
      const file = input.files[0];
      if (file.type !== 'application/pdf') {
        alert('Veuillez sélectionner un fichier PDF uniquement.');
        return;
      }

      this.ajouterdemandeService.uploadDecharge(ligne.id, file).subscribe({
        next: () => this.showSuccess('Fichier uploadé avec succès'),
        error: () => this.showError('Erreur lors de l’upload du fichier')
      });
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
  openPdf1(filename: string) {
    this.http.get(`http://localhost:8080/decharges/${filename}`, { responseType: 'blob' })
      .subscribe(blob => {
        const url = window.URL.createObjectURL(blob);
        window.open(url);
      }, err => {
        console.error('Erreur lors du chargement du PDF', err);
      });
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

selectedLigneForLivraison: any = null;
selectedLivraisonType:  null = null;


openLivraisonDialog(ligne: any) {
  this.selectedLigneForLivraison = ligne;
  this.selectedLivraisonType = ligne.typeLivraison || null;
}
  typeLivraison: string='';

/*  confirmLivraison() {
    if (this.selectedLigneForLivraison && this.typeLivraison) {
      this.ligneDemandeService.ajouterLivraison(
        this.selectedLigneForLivraison.id,
        this.typeLivraison
      ).subscribe({
        next: (response) => {

          this.selectedLigneForLivraison.livraisonType = this.typeLivraison;
          this.cancelLivraison();
          this.showSuccess('Livraison mise à jour');
          this.selectedLigneForLivraison = null;
          this.typeLivraison = '';
        },
        error: (err) => {
          this.showError('Erreur lors de la mise à jour');

        }

      });
    }
  }*/
  confirmLivraison() {
    if (this.selectedLigneForLivraison && this.typeLivraison) {
      this.ligneDemandeService.ajouterLivraison(
        this.selectedLigneForLivraison.id,
        this.typeLivraison
      ).subscribe({
        next: (response) => {

          const demandeIndex = this.demandes().findIndex(d =>
            d.ligneDemandes.some((l: any) => l.id === this.selectedLigneForLivraison.id)
          );

          if (demandeIndex !== -1) {
            const ligneIndex = this.demandes()[demandeIndex].ligneDemandes
              .findIndex((l: any) => l.id === this.selectedLigneForLivraison.id);

            if (ligneIndex !== -1) {
              this.demandes()[demandeIndex].ligneDemandes[ligneIndex].typeLivraison = this.typeLivraison;
            }
          }


          this.selectedLigneForLivraison.typeLivraison = this.typeLivraison;

          this.showSuccess('Livraison mise à jour');
          this.cancelLivraison();
        },
        error: (err) => {
          this.showError('Erreur lors de la mise à jour');
          console.error(err);
        }
      });
    }
  }

  cancelLivraison() {
    this.selectedLigneForLivraison = null;
    this.typeLivraison = '';
  }
  onSearchInput(): void {
    if (this.searchTerm().trim() === '') {
      this.filteredDemandes.set(this.demandes());
    } else {
      this.filterDemandes();
    }
  }




  filterDemandes(): void {
    const term = this.searchTerm().toLowerCase();

    this.filteredDemandes.set(
      this.demandes().filter(demande => {
        const mainFieldsMatch =
          demande.idDemande.toString().includes(term) ||
          (demande.createur?.nom && demande.createur.nom.toLowerCase().includes(term)) ||
          (demande.etat && demande.etat.toLowerCase().includes(term)) ||
          (demande.createur?.email && demande.createur.email.toLowerCase().includes(term));
        const lignesMatch = demande.ligneDemandes?.some(ligne => {
          return (
            (ligne.typeLivraison && ligne.typeLivraison.toLowerCase().includes(term)) ||
            (ligne.etat && ligne.etat.toLowerCase().includes(term)) ||
            (ligne.dureeSi && ligne.dureeSi.toString().includes(term)) ||
            (ligne.materiel?.nomMateriel && ligne.materiel.nomMateriel.toLowerCase().includes(term)) ||
            (ligne.materiel?.typeMateriel && ligne.materiel.typeMateriel.toLowerCase().includes(term))
          );
        });

        return mainFieldsMatch || lignesMatch;
      })
    );
  }

}
