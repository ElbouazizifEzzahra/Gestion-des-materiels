import { Injectable, inject, signal } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { map, catchError } from 'rxjs/operators';
import { toSignal } from '@angular/core/rxjs-interop';
import { Demande } from '../models/Demande';
import { of } from 'rxjs';
import {Destinataire} from '../models/Destinataire';
import {LigneDemande} from '../models/LigneDemande';
import {User} from '../models/User';

@Injectable({
  providedIn: 'root'
})
export class DemandeService {


  private http = inject(HttpClient);
  private apiUrl = 'http://localhost:8080/demandes/demandes';

  // Charge les demandes et les expose via un Signal
  demandes = toSignal(this.http.get<Demande[]>(this.apiUrl), {
    initialValue: [] as Demande[]
  });


}
