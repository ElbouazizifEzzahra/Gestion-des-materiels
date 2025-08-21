export interface User {
  id: number;
  nom: string;
  prenom: string;
  email: string;
  role: string;
  numeroTelephone: string;
  code: string;
  fonctionalite: string;
  projet: string;
  site?: string;
  societe?: string;
  password?: string;

}
export interface EditForm {
  nom:string;
  prenom:string;
  email:string;
  role:string;
  numeroTelephone:string;
  code:string;
  fonctionalite:string;
  projet:string;
  site:string;
  societe:string;


}

export interface PasswordForm {
  currentPassword: string;
  newPassword: string;
  confirmPassword: string;
}
