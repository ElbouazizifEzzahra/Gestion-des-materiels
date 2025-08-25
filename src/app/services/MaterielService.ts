import {Injectable} from '@angular/core';
import {HttpClient, HttpParams} from '@angular/common/http';
import { Observable} from 'rxjs';
import {Materiel} from '../models/Materiel';

@Injectable({providedIn:'root'})
export class MaterielService {
  private url='http://localhost:8080/materiel';

  constructor(private http:HttpClient) {
  }
  getMateriels(): Observable<Materiel[]> {
    return this.http.get<Materiel[]>(`${this.url}/materiels`);
  }

  addMateriel(materiel: Omit<Materiel, 'idMateriel'>): Observable<Materiel> {
    return this.http.post<Materiel>(`${this.url}/add`, materiel);
  }
  deleteMateriel(id: number): Observable<any> {
    return this.http.delete(`${this.url}/delete/${id}`);
  }

  updateMateriel(id: number, nom?: string, type?: string): Observable<any> {
    const params = new HttpParams()
      .set('nomMateriel', nom || '')
      .set('typeMateriel', type || '');

    return this.http.patch(`${this.url}/update/${id}`, null, { params });
  }
  filterMateriels(prefix: string): Observable<Materiel[]> {
    return this.http.get<Materiel[]>(`${this.url}/filter`, {
      params: { nom: prefix }
    });
  }

}
