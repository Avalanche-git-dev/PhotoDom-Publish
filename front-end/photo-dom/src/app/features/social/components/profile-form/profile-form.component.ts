
import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { UserControllerService } from '../../../../../services/user-service/services/user-controller.service';
import { UserDto } from '../../../../../services/user-service/models/user-dto';
import { MatSnackBar } from '@angular/material/snack-bar';

@Component({
  selector: 'app-profile-form',
  templateUrl: './profile-form.component.html',
  styleUrls: ['./profile-form.component.css'],
  standalone: false
})
export class ProfileFormComponent implements OnInit {
  profileForm: FormGroup; // Form
  errorMessage: string | null = null; // Messaggio di errore

  constructor(
    private fb: FormBuilder,
    private userService: UserControllerService,
    private snackBar: MatSnackBar
  ) {
    // Inizializza il form
    this.profileForm = this.fb.group({
      firstName: [''],
      lastName: [''],
      email: ['', [ Validators.email]],
      telephone: ['', [Validators.pattern(/^\d{10}$/)]],
      nickname: [''],
    });
  }
  ngOnInit(): void {
  }

  // **Invio aggiornamenti**
  onSubmit(): void {
    if (this.profileForm.valid) {
      const updatedProfile: UserDto = this.profileForm.value; // Prepara i dati modificati

      // Chiamata API per l'aggiornamento
      this.userService.updateUser({ body: updatedProfile }).subscribe({
        next: (response) => {
          if (response.success) {
            // Successo: Mostra snackbar verde
            this.snackBar.open('Profilo aggiornato con successo!', 'Chiudi', {
              duration: 3000,
              panelClass: ['success-snackbar'],
            });
            this.errorMessage = null; // Resetta eventuali errori
            
          } else {
            // Errore: Mostra il messaggio restituito dal backend
            this.errorMessage = response.message || 'Errore durante l’aggiornamento.';
            this.resetErrorMessage(); // Rimuove l'errore dopo 3 secondi
          }
        },
        error: (err) => {
          // Errore generico
          console.error('Errore durante l’aggiornamento:', err);
          this.errorMessage =
            err.error?.message || 'Errore durante l’aggiornamento. Riprova più tardi.';
          this.resetErrorMessage(); // Rimuove l'errore dopo 3 secondi
        },
      });
    }
  }

  // **Reset automatico del messaggio di errore**
  private resetErrorMessage(): void {
    setTimeout(() => {
      this.errorMessage = null; // Rimuove il messaggio di errore dopo 3 secondi
    }, 3000);
  }
}
