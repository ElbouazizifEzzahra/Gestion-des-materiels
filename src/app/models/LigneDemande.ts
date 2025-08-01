import {Demande} from './Demande';
import {Destinataire} from './Destinataire';
import {Materiel} from './Materiel';

export interface LigneDemande {
  id : number;
  description ?: string;
  fichTechnique?: string;
  quantite : number;
  demandeId : number;
  destinataireId : number;
  materielId : number;
  demande: Demande;
  destinataire: Destinataire;
  materiel :Materiel;

}
