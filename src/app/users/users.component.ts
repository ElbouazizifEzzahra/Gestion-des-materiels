import {Component, inject, OnInit, signal} from '@angular/core';
import { UserService } from '../services/UserService'
import {FormBuilder, FormGroup, FormsModule, ReactiveFormsModule, Validators} from '@angular/forms';
import {EditForm, User} from '../models/User';
import {LayoutComponent} from '../core/layout/layout/layout.component';
import {LoginService} from '../services/LoginService';
import {Router} from '@angular/router';

@Component({
  selector: 'app-users',
  imports: [
    ReactiveFormsModule,
    FormsModule,
    LayoutComponent
  ],
  templateUrl: './users.component.html',
  styleUrl: './users.component.css',
  standalone:true
})
export class UsersComponent implements OnInit {
  users: any[] = [];
  isLoading = true;
  showForm = false;
  userForm!: FormGroup;

  private user = inject(UserService);

  selectedUser = signal<User | null>(null);
  showEditModal = signal<boolean>(false);
  editForm = signal<EditForm>({
    nom:'',
    prenom:'',
    email:'',
    role:'',
    numeroTelephone:'',
    code:'',
    fonctionalite:'',
    projet:'',
    site:'',
    societe:''
  });

  constructor(  private loginService: LoginService,
  private fb: FormBuilder
  ) {
    this.userForm = this.fb.group({
      nom: ['', Validators.required],
      prenom: ['', Validators.required],
      email: ['', [Validators.required, Validators.email]],
      role: ['', Validators.required],
      numeroTelephone: [''],
      code: [''],
      fonctionalite: [''],
      projet: [''],
      site: [''],
      societe: ['']
    });
    }


  ngOnInit(): void {
    this.user.getUsers().subscribe({
      next: (data) => {

        this.users = Array.isArray(data) ? data : [];
        this.isLoading = false;
      },
      error: (err) => {
        console.error({err: 'Erreur:'}, err);
        this.isLoading = false;
      }
    });
  }

  openAddForm(): void {
    this.showForm = true;
  }
  cancelForm(): void {
    this.userForm.reset();
    this.showForm = false;
  }
  onSubmit(): void {
    if (this.userForm.valid) {
      this.user.addUser(this.userForm.value).subscribe({
        next: (response) => {
          this.users.push(response);
          this.userForm.reset();
          this.showForm = false;
          this.isLoading = false;
        },
        error: (err) => {
          console.error('Erreur:', err);
          this.isLoading = false;
        }
      });
    }
  }

  searchTerm: string = '';
  filterMateriels() {
    this.user.filterUsers(this.searchTerm).subscribe({
      next: (users) => {
        this.users = users;
      },
      error: (error) => {
        console.error('Erreur lors du filtrage:', error);
      }
    });
  }

 /* updateUser(): void {
    const id = this.selectedUser()?.id;
    if (!id) return;

    const form = this.editForm();
    this.user.updateUser(id, form.nom ,form.prenom,form.email,form.role,form.site,form.societe,form.projet, form.fonctionalite,form.numeroTelephone,form.code).subscribe({
      next: (updatedUser) => {

        const index = this.users.findIndex(u => u.id === id);
        if (index !== -1) {
          this.users[index] = {
            ...this.users[index],
            nom: form.nom,
            prenom: form.prenom,
            email:form.email,
            role:form.role,
            site:form.site,
            societe:form.societe,
            projet:form.projet,
            fonctionalite:form.fonctionalite,
            numeroTelephone:form.numeroTelephone,
            code:form.code
          };
        }
        this.showEditModal.set(false);

      },

    });
  }
*/

  updateUser(): void {
    const token = this.loginService.getToken();
    console.log('Token actuel:', token);

    const id = this.selectedUser()?.id;
    if (!id) {
      console.error('ID utilisateur manquant');
      return;
    }


    const form = this.editForm();
    console.log('Envoi des données:', form);

    this.user.updateUser(
      id,
      form.nom || '',
      form.prenom || '',
      form.email || '',
      form.role || '',
      form.site || '',
      form.societe || '',
      form.projet || '',
      form.fonctionalite || '',
      form.numeroTelephone || '',
      form.code || ''
    ).subscribe({
      next: () => {

        const index = this.users.findIndex(u => u.id === id);
        if (index !== -1) {
          this.users[index] = {...this.users[index], ...form};
        }
        this.showEditModal.set(false);
        alert('Modification réussie !');
      },
      error: (err) => {
        console.error('Erreur:', err);
        alert(`Échec de la modification : ${err.message}`);
      }
    });
  }
  deleteUser(id: number) {
    if (confirm('Êtes-vous sûr de vouloir supprimer ce user ?')) {
      this.user.deleteUser(id).subscribe({
        next: () => {
          this.users = this.users.filter(m => m.id !== id);
          console.log('user supprimé avec succès');
        },
        error: (err) => {
          console.error('Erreur lors de la suppression:', err);
        }
      });
    }
  }
  openEditModal(user: User): void {
    this.selectedUser.set(user);
    this.editForm.set({
      nom: user.nom as string,
      prenom: user.prenom as string,
      email: user.email as string,
      role: user.role as string,
      site: user.site as string,
      societe: user.societe as string,
      projet: user.projet as string,
      fonctionalite: user.fonctionalite as string,
      numeroTelephone: user.numeroTelephone as string,
      code: user.code as string
    });
    this.showEditModal.set(true);
  }
}
