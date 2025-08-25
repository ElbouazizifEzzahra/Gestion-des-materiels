import {inject, Injectable} from '@angular/core';
import {HttpClient, HttpParams} from '@angular/common/http';
import {LigneDemande} from '../models/LigneDemande';
import {Observable} from 'rxjs';
import {Demande} from '../models/Demande';



@Injectable({
  providedIn:'root'
})
export class LigneDemandeService {

  private http = inject(HttpClient);
  private apiUrl = 'http://localhost:8080/lignes-demandes';


  ajouterLignesDemande(formData: FormData): Observable<any> {
    return this.http.post(`${this.apiUrl}/add`, formData);
  }
  genererDecharge(): Observable<any> {
    return this.http.get(`${this.apiUrl}/ligne/{idLigne}`);
  }

  getPDF(idLigne: number, ville: string, caracteristiques: string[]): Observable<Blob> {
    return this.http.get(`${this.apiUrl}/ligne/${idLigne}`, {
      params: {
        ville: ville,
        caracteristiques: caracteristiques
      },
      responseType: 'blob'
    }) ;
  }



  emailAchat(id:number): Observable<any> {
    return this.http.get(`${this.apiUrl}/email/${id}`);
  }
  uploadDecharge(idLigne: number, fichier: File): Observable<any> {
    const formData = new FormData();
    formData.append('decharge', fichier);

    return this.http.post(`${this.apiUrl}/decharge/${idLigne}`, formData);
  }



  demandesById(id: number): Observable<Demande[]> {
    return this.http.get<Demande[]>(`${'http://localhost:8080/demandes'}/${id}`);
  }
  deleteLigneDemande(id: number): Observable<any> {
    return this.http.delete(`${this.apiUrl}/delete/${id}`);
  }

  updateLigneDemande(id: number, formData: FormData): Observable<any> {
    return this.http.put(`${this.apiUrl}/update/${id}`, formData);
  }
  updateEtat(id: number, etat: string): Observable<any> {
    const params = new HttpParams().set('etat', etat);
    return this.http.patch(`${this.apiUrl}/Update-Etat/${id}`, {}, { params });
  }
  ajouterLivraison(id: number, type: string): Observable<any> {
    return this.http.post(`${this.apiUrl}/livraison/${id}`, {}, {
      params: { type }
    });
  }
}
