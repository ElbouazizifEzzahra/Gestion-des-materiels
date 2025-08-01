import { Injectable } from '@angular/core';
import {HttpClient, HttpHeaders, HttpParams} from '@angular/common/http';
import { Observable} from 'rxjs'
import { User } from '../models/User';

@Injectable({providedIn:'root'})
export class UserService {
  private url='http://localhost:8080/user';
  constructor(private http:HttpClient) {
  }
  getUsers(): Observable<UserService[]> {
    return this.http.get<UserService[]>(`${this.url}/users`);
  }


  addUser(user: Omit<UserService, 'id'>): Observable<UserService> {
    return this.http.post<UserService>(`${this.url}/add`, user);
  }
  deleteUser(id: number): Observable<any> {
    return this.http.delete(`${this.url}/delete/${id}`);
  }

  updateUser(
    id: number,
    nom: string,
    prenom: string,
    email: string,
    role: string,
    site: string,
    societe: string,
    projet: string,
    fonctionalite: string,
    numeroTelephone: string,
    code: string
  ): Observable<any> {
    const body = {
      nom,
      prenom,
      email,
      role,
      site,
      societe,
      projet,
      fonctionalite,
      numeroTelephone,
      code
    };

    return this.http.put(`${this.url}/${id}`, {
      nom, prenom, email, role, site,
      societe, projet, fonctionalite,
      numeroTelephone, code
    });
  }
  filterUsers(prefix: string): Observable<UserService[]> {
    return this.http.get<UserService[]>(`${this.url}/filter`, {
      params: { nom: prefix }
    });
  }
}
