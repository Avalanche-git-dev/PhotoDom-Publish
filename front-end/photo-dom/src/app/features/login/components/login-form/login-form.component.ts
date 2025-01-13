



import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { AuthService } from '../../../../../services/auth-service/auth.service';
import { Router, ActivatedRoute } from '@angular/router';

@Component({
  selector: 'app-login-form',
  templateUrl: './login-form.component.html',
  styleUrls: ['./login-form.component.css'],
  standalone: false
})
export class LoginFormComponent implements OnInit {
  loginForm: FormGroup; // FormGroup per gestire il form
  loginError: string | null = null; // Messaggio di errore durante il login

  constructor(
    private fb: FormBuilder,
    private authService: AuthService, // Servizio di autenticazione
    private router: Router, // Router per navigazione
    private route: ActivatedRoute // Controllo parametri URL
  ) {
    // Inizializzazione del form
    this.loginForm = this.fb.group({
      username: ['', Validators.required], // Campo username obbligatorio
      password: ['', Validators.required], // Campo password obbligatorio
    });
  }

  ngOnInit(): void {
    // Controlla la variabile statica di AuthService
    if (AuthService.sessionExpired) {
      this.loginError = 'Sessione scaduta. Effettua nuovamente il login.';
      AuthService.sessionExpired = false; // Resetta la variabile dopo aver mostrato il messaggio
      

      // Nasconde il messaggio dopo 5 secondi
      setTimeout(() => {
        this.loginError = null;
      }, 5000);
    }
  }

  // **Gestione invio form**
  onSubmit(): void {
    if (this.loginForm.valid) {
      const { username, password } = this.loginForm.value;

      this.authService.login(username, password).subscribe({
        next: () => {
          this.loginError = null;
          localStorage.setItem('welcomeShown', 'false');
          this.router.navigate(['/social']); // Reindirizza all'area protetta
        },
        error: (err: any) => {
          this.loginError = err.message || 'Errore durante il login.';
          setTimeout(() => {
            this.loginError = null; // Nasconde il messaggio dopo 5 secondi
          }, 5000);
        },
      });
    } else {
      this.loginError = 'Compila tutti i campi richiesti.';
      setTimeout(() => {
        this.loginError = null; // Nasconde il messaggio dopo 5 secondi
      }, 5000);
    }
  }
}
