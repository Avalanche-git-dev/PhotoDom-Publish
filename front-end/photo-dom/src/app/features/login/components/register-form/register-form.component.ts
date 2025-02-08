import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { AuthService } from '../../../../../services/auth-service/auth.service';
import { UserControllerService } from '../../../../../services/user-service/services';
import { Router } from '@angular/router';

@Component({
  selector: 'app-register-form',
  standalone: false,
  
  templateUrl: './register-form.component.html',
  styleUrl: './register-form.component.css'
})
export class RegisterFormComponent {
  registerForm: FormGroup;
  errorMessage: string | null = null;
  successMessage: string | null = null;

  constructor(
    private fb: FormBuilder,
    private userService: UserControllerService,
    private authService: AuthService,
    private router: Router
  ) {
    this.registerForm = this.fb.group({
      username: ['', Validators.required],
      password: ['', Validators.required],
      firstName: ['', Validators.required],
      lastName: ['', Validators.required],
      birthday: ['', Validators.required],
      email: ['', [Validators.required, Validators.email]],
      telephone: ['', Validators.required],
      nickname: ['', Validators.required]
    });
  }

  onRegister(): void {
    if (this.registerForm.valid) {
      const userData = this.registerForm.value;

      this.userService.createUser({ body: userData }).subscribe({
        next: (response) => {
          if (response.success) {
            this.successMessage = response.message || 'Registrazione completata con successo!';
            this.errorMessage = null;
            this.loginAfterRegistration(userData.username, userData.password);
          }
        },
        error: (err) => {
          // Gestiamo l'errore proveniente dal backend
          if (err.error && err.error.message) {
            this.errorMessage = err.error.message;
            this.successMessage = null;
          } else {
            // Errore generico (es. server non raggiungibile)
            this.errorMessage = 'Errore di connessione al server. Riprova più tardi.';
            this.successMessage = null;
          }
          this.hideMessagesAfterDelay();
        }
      });
    } else {
      this.successMessage = null;
      this.errorMessage = 'Compila tutti i campi obbligatori.';
      this.hideMessagesAfterDelay();
    }
  }

  private loginAfterRegistration(username: string, password: string): void {
    this.authService.login(username, password).subscribe({
      next: () => {
        this.errorMessage = null;
        this.successMessage = null;
        localStorage.setItem('welcomeShown', 'false');
        this.router.navigate(['/social']);
      },
      error: () => {
        this.successMessage = null;
        this.errorMessage = 'Errore durante l’autenticazione. Riprova più tardi.';
        this.hideMessagesAfterDelay();
      }
    });
  }

  private hideMessagesAfterDelay(): void {
    setTimeout(() => {
      this.errorMessage = null;
      this.successMessage = null;
    }, 2000);
  }
}
