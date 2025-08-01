import {User} from './User';
import {LigneDemande} from './LigneDemande';

export interface Demande {
  idDemande : number;
  etat : string;
  time  ?: string;
  date  ?: string ;
  createurId : number;
  createur : User;
  ligneDemandes : LigneDemande[]
}
