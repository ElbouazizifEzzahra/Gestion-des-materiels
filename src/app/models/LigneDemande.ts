import {Demande} from './Demande';
import {Destinataire} from './Destinataire';
import {Materiel} from './Materiel';

export interface LigneDemande {
  id : number;
  description ?: string;
  decharge ?: string;
  ficheTechnique?: string;
  quantite : number;
  demandeId : number;
  destinataireId : number;
  materielId : number;
  demande: Demande;
  destinataire: Destinataire;
  materiel :Materiel;
  etat: string;
  dureeSi:number;
  dureeLogistique:number;
  typeLivraison: string;
}
