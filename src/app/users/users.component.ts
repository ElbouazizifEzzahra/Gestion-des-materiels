import {Component, inject, OnInit, signal} from '@angular/core';
import { UserService } from '../services/UserService'
import {FormBuilder, FormGroup, FormsModule, ReactiveFormsModule, Validators} from '@angular/forms';
import {EditForm, User} from '../models/User';
import {LoginService} from '../services/LoginService';
import {Router} from '@angular/router';

@Component({
  selector: 'app-users',
  imports: [
    ReactiveFormsModule,
    FormsModule
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
  showDeleteModal = signal<boolean>(false);
  ToDelete = signal<number | null>(null);
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
          this.showSuccess('user ajouté avec succès');
        },
        error: (err) => {
          this.showError('Erreur');
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


  updateUser(): void {
    const token = this.loginService.getToken();
    const id = this.selectedUser()?.id;
    if (!id) {
      console.error('ID utilisateur manquant');
      return;
    }


    const form = this.editForm();
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
       this.showSuccess('Modification réussie !');
      },
      error: (err) => {
        console.error('Erreur:', err);
       this.showError('Échec de la modification ');
      }
    });
  }
  deleteUser(id: number) {
    if (confirm('Êtes-vous sûr de vouloir supprimer ce user ?')) {
      this.user.deleteUser(id).subscribe({
        next: () => {
          this.users = this.users.filter(m => m.id !== id);
          this.showSuccess('user supprimé avec succès');
        },
        error: (err) => {
        this.showError('Erreur lors de la suppression');
        }
      });
    }
  }  deleteMateriel(): void {
    const id = this.ToDelete();
    if (id === null) return;

    this.user.deleteUser(id).subscribe({
      next: () => {
        this.showSuccess('Supprimé avec succès');

        this.user.getUsers().subscribe(data => {
          this.users = Array.isArray(data) ? data : [];
        });
        this.closeDeleteModal();
      },
      error: () => {
        this.showError('Échec de la suppression');
        this.closeDeleteModal();
      }
    });
  }

  openDeleteModal(id: number): void {
    this.ToDelete.set(id);
    this.showDeleteModal.set(true);
  }

  closeDeleteModal(): void {
    this.showDeleteModal.set(false);
    this.ToDelete.set(null);
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
  filterUsersFront(): void {
    if (!this.searchTerm) {

      this.user.getUsers().subscribe({
        next: (data) => {
          this.users = Array.isArray(data) ? data : [];
        },
        error: (err) => {
          console.error({err: 'Erreur:'}, err);
        }
      });
      return;
    }

    const term = this.searchTerm.toLowerCase();
    this.users = this.users.filter(user =>
      (user.nom && user.nom.toLowerCase().includes(term)) ||
      (user.prenom && user.prenom.toLowerCase().includes(term)) ||
      (user.email && user.email.toLowerCase().includes(term)) ||
      (user.role && user.role.toLowerCase().includes(term)) ||
      (user.site && user.site.toLowerCase().includes(term)) ||
      (user.societe && user.societe.toLowerCase().includes(term)) ||
      (user.projet && user.projet.toLowerCase().includes(term)) ||
      (user.fonctionalite && user.fonctionalite.toLowerCase().includes(term)) ||
      (user.numeroTelephone && user.numeroTelephone.includes(term)) ||
      (user.code && user.code.includes(term))
    );
  }

  onSearchInput(): void {
    this.filterUsersFront();
  }
  showSuccess(message: string): void {
    this.showNotification(message, 'bg-green-500');
  }

  showError(message: string): void {
    this.showNotification(message, 'bg-red-500');
  }


  private showNotification(message: string, bgClass: string): void {
    const notification = document.createElement('div');
    notification.innerHTML = `
      <div class="fixed justify-center top-4 right-4 p-4 rounded-md text-white ${bgClass} shadow-lg">
        ${message}
      </div>
    `;
    document.body.appendChild(notification);

    setTimeout(() => notification.remove(), 3000);
  }
}
