export interface Destinataire{
  id : number;
  email?: string;
  nom ?: string;
  prenom ?: string;
  fonction ?: string;
  projet ?: string;
  type: 'MO' | 'EMPLOYE';
}
