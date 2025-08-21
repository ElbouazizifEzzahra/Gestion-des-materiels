import { Component } from '@angular/core';
import {FormBuilder, FormGroup, ReactiveFormsModule, Validators} from '@angular/forms';
import { Router } from '@angular/router';
import { LoginService } from '../services/LoginService';
import { CommonModule } from '@angular/common';
import {jwtDecode} from 'jwt-decode';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent {
  loginForm: FormGroup;
  welcomeMessage: string | null = null;
  errorMessage: string = '';
  isLoading: boolean = false;

  constructor(
    private fb: FormBuilder,
    private loginService: LoginService,
    private router: Router
  ) {
    this.loginForm = this.fb.group({
      email: ['', [Validators.required, Validators.email]],
      password: ['', Validators.required],
      rememberMe: [false]
    });
  }

  get email() { return this.loginForm.get('email'); }
  get password() { return this.loginForm.get('password'); }

  onLogin() {
    this.loginForm.markAllAsTouched();
    if (this.loginForm.invalid) {
      this.errorMessage = 'Veuillez corriger les erreurs dans le formulaire';
      return;
    }

    this.isLoading = true;
    this.errorMessage = '';

    const { email, password, rememberMe } = this.loginForm.value;

    this.loginService.login(email, password).subscribe({
      next: (response) => {

        this.isLoading = false;

        if (rememberMe) {
          sessionStorage.setItem('rememberedEmail', email);
        }


        const token = sessionStorage.getItem('authToken');
        if (token) {
          try {
            const decodedToken: any = jwtDecode(token);
            const role = decodedToken.role;
            this.loginService.role = role;
            switch (role) {
              case 'ADMIN':
                this.router.navigate(['/admin']);
                break;
              case 'ESI':
                this.router.navigate(['/service-informatique']);
                break;
              case 'E':
                this.router.navigate(['/employe']);
                break;
              default:
                this.router.navigate(['/acces-interdit']);
            }
          } catch (err) {
            console.error('Erreur de décodage du token :', err);
            this.router.navigate(['/acces-interdit']);
          }
        }
      },
      error: (err) => {
        this.isLoading = false;
        this.errorMessage = err.error?.message || 'Échec de la connexion. Veuillez réessayer.';
        console.error('Erreur de connexion:', err);
      }
    });
  }

  ngOnInit() {
    const rememberedEmail = sessionStorage.getItem('rememberedEmail');
    if (rememberedEmail) {
      this.loginForm.patchValue({
        email: rememberedEmail,
        rememberMe: true
      });
    }

  }



}
