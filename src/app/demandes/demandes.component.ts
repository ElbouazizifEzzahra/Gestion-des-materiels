import {Component, effect, inject, OnInit, signal} from '@angular/core';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import {DemandeService} from '../services/DemandeService';
import {NavbarComponent} from '../layout/navbar/navbar.component';
import {HttpClient} from '@angular/common/http';


@Component({
  selector: 'app-demandes',
  imports: [
    ReactiveFormsModule,
    FormsModule,
    NavbarComponent
  ],
  templateUrl: './demandes.component.html',
  styleUrl: './demandes.component.css',
  standalone:true
})
export class DemandesComponent {
  private demandeService = inject(DemandeService);
  demandes = this.demandeService.demandes;

  filteredDemandes = signal<any[]>([]);
  searchTerm = signal<string>('');
  constructor(private http:HttpClient) {
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





  onSearchInput(): void {
    if (this.searchTerm().trim() === '') {
      this.filteredDemandes.set(this.demandes());
    } else {
      this.filterDemandes();
    }
  }


 /* filterDemandes(): void {
    const term = this.searchTerm().toLowerCase();
    this.filteredDemandes.set(
      this.demandes().filter(demande =>
        demande.idDemande.toString().includes(term) ||
        demande.createur.nom.toLowerCase().includes(term) ||
        demande.etat.toLowerCase().includes(term) ||
        (demande.createur.email && demande.createur.email.toLowerCase().includes(term))
      )
    );
  }*/
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

}
