export interface Materiel {
  idMateriel:number;
  typeMateriel?:string;
  nomMateriel ?: string;
  displayText?: string;
  //typesDisponibles?: string[];
}
export interface EditForm {
  nom: string;
  type: string;
}
