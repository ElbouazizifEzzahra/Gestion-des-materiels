import { Injectable, inject, signal } from '@angular/core';
import {HttpClient, HttpParams} from '@angular/common/http';
import { toSignal } from '@angular/core/rxjs-interop';
import { Demande } from '../models/Demande';
import {Observable} from 'rxjs';


@Injectable({
  providedIn: 'root'
})
export class DemandeService {


  private http = inject(HttpClient);
  private apiUrl = 'http://localhost:8080/demandes';

  // Charge les demandes et les expose via un Signal
  demandes = toSignal(this.http.get<Demande[]>(`${this.apiUrl}/demandes`), {
    initialValue: [] as Demande[]
  });

  updateEtat(id: number, etat: string): Observable<any> {
    const params = new HttpParams().set('etat', etat);
    return this.http.patch(`${this.apiUrl}/Update-Etat/${id}`, {}, { params });
  }

  deleteDemande(id: number): Observable<any> {
    return this.http.delete(`${this.apiUrl}/${id}`);
  }
  loadDemandes(): Observable<Demande[]> {
    return this.http.get<Demande[]>(`${this.apiUrl}/demandes`).pipe(

    );
  }

}
