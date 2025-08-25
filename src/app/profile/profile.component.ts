import {Component, inject, signal} from '@angular/core';
import {FormsModule} from '@angular/forms';
import {UserService} from '../services/UserService';
import {User} from '../models/User';
import {LoginService} from '../services/LoginService';

interface EditForm {
  nom: string;
  prenom: string;
  email: string;
  numeroTelephone: string;
  code?: string;
  fonctionalite?: string;
  projet?: string;
  site?: string;
  societe?: string;

}

@Component({
  selector: 'app-profile',
  imports: [FormsModule],
  templateUrl: './profile.component.html',
  styleUrl: './profile.component.css',
  standalone: true
})
export class ProfileComponent {
  isEditing = false;
  user: User | null = null;
  loginService = inject(LoginService);
  userService = inject(UserService);

  passwordError = signal<string | null>(null);
  passwordSuccess = signal(false);
  isLoading = signal(false);
  passwordForm = signal({
    currentPassword: '',
    newPassword: '',
    confirmPassword: ''
  });
  editForm = signal<EditForm>({
    nom: '',
    prenom: '',
    email: '',
    numeroTelephone: '',
    code: '',
    fonctionalite: '',
    projet: '',
    site: '',
    societe: ''
  });


  constructor() {
    this.loadUserData();
  }
  private loadUserData(): void {
    const userId = this.loginService.userId;
    if (!userId) return;

    this.userService.findUser(userId).subscribe({
      next: (user) => {
        this.user = user;
        this.editForm.set({
          nom: user.nom || '',
          prenom: user.prenom || '',
          email: user.email || '',
          numeroTelephone: user.numeroTelephone || '',
          code: user.code || '',
          fonctionalite: user.fonctionalite || '',
          projet: user.projet || '',
          site: user.site || '',
          societe: user.societe || ''
        });
      },
      error: (err) => {
        console.error('Erreur lors du chargement des données utilisateur', err);
      }
    });
  }
  updateUser(): void {
    const userId = this.loginService.userId;
    if (!userId || !this.user) {
      console.error('ID utilisateur manquant');
      return;
    }


    const updatedUser = {
      ...this.user,
      ...this.editForm()
    };

    this.userService.updateUser2(userId, updatedUser).subscribe({
      next: (response) => {
        this.isEditing = false;
        this.showSuccess("Modification réussie !");
        this.user = updatedUser;
        this.loginService.updateCurrentUser(updatedUser);
      },
      error: (err) => {
        this.showError('Erreur lors de la mise à jour');
      }
    });
  }


  updatePassword(): void {
    const { currentPassword, newPassword, confirmPassword } = this.passwordForm();

    // Validation côté client
    if (!currentPassword || !newPassword || !confirmPassword) {
      this.passwordError.set('Tous les champs sont obligatoires');
      return;
    }

    if (newPassword !== confirmPassword) {
      this.passwordError.set('Les mots de passe ne correspondent pas');
      return;
    }

    if (newPassword.length < 8) {
      this.passwordError.set('Le mot de passe doit contenir au moins 8 caractères');
      return;
    }

    const userId = this.loginService.userId;
    if (!userId) {
      this.passwordError.set('Utilisateur non identifié');
      return;
    }

    this.isLoading.set(true);
    this.passwordError.set(null);

    this.userService.updatePassword(userId, currentPassword, newPassword).subscribe({
      next: () => {
        this.passwordSuccess.set(true);
        this.passwordForm.set({
          currentPassword: '',
          newPassword: '',
          confirmPassword: ''
        });
        setTimeout(() => this.passwordSuccess.set(false), 3000);
      },
      error: (err) => {
        this.passwordError.set(err.error?.message || 'Erreur lors du changement de mot de passe');
      },
      complete: () => this.isLoading.set(false)
    });
  }
  cancelEditing(): void {
    this.isEditing = false;

    this.loadUserData();
  }
  showPassword = signal({
    current: false,
    new: false,
    confirm: false
  });
  passwordStrength = signal(0);
  calculatePasswordStrength() {
    const password = this.passwordForm().newPassword;
    let strength = 0;

    if (password.length >= 8) strength += 1;
    if (/[A-Z]/.test(password)) strength += 1;
    if (/\d/.test(password)) strength += 1;
    if (/[^A-Za-z0-9]/.test(password)) strength += 1;
    this.passwordStrength.set(strength);
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
