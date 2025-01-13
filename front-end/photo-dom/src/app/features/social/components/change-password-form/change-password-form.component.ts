// import { Component } from '@angular/core';
// import { FormBuilder, FormGroup, Validators } from '@angular/forms';
// import { MatSnackBar } from '@angular/material/snack-bar';
// import { UserControllerService } from '../../../../../services/user-service/services';

// @Component({
//   selector: 'app-change-password-form',
//   standalone: false,
  
//   templateUrl: './change-password-form.component.html',
//   styleUrl: './change-password-form.component.css'
// })
// export class ChangePasswordFormComponent {

//   passwordForm: FormGroup; // FormGroup per gestire il form
//   errorMessage: string | null = null; // Per i messaggi di errore dal backend

//   constructor(
//     private fb: FormBuilder,
//     private userService: UserControllerService,
//     private snackBar: MatSnackBar
//   ) {
//     // Inizializzazione del form con validazioni
//     this.passwordForm = this.fb.group({
//       oldPassword: ['', Validators.required], // Password attuale obbligatoria
//       newPassword: ['', [Validators.required, Validators.minLength(6)]], // Minimo 6 caratteri
//     });
//   }

//   // **Invio del form per aggiornare la password**
//   onSubmit(): void {
//     if (this.passwordForm.valid) {
//       const credentials = this.passwordForm.value; // Recupera i valori dal form

//       // Chiamata API per aggiornare la password
//       this.userService.changePassword({ body: credentials }).subscribe({
//         next: (response) => {
//           // Successo
//           if (response.success) {
//             this.snackBar.open('Password aggiornata con successo!', '', { duration: 3000 });
//             this.errorMessage = null; // Resetta eventuali errori
//             this.passwordForm.reset(); // Pulisce il form
//           } else {
//             // Messaggio di errore dal backend
//             this.errorMessage = response.message || 'Errore durante l’aggiornamento.';
//           }
//         },
//         error: (err) => {
//           // Errore HTTP generico
//           console.error('Errore durante l’aggiornamento:', err);
//           this.errorMessage = 'Errore durante l’aggiornamento.';
//         },
//       });
//     }
//   }
// }


import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MatSnackBar } from '@angular/material/snack-bar';
import { UserControllerService } from '../../../../../services/user-service/services';
import { Credentials } from '../../../../../services/user-service/models/credentials';

@Component({
  selector: 'app-change-password-form',
  templateUrl: './change-password-form.component.html',
  styleUrls: ['./change-password-form.component.css'],
  standalone: false
})
export class ChangePasswordFormComponent {
  passwordForm: FormGroup; // FormGroup per gestire il form
  errorMessage: string | null = null; // Per i messaggi di errore dal backend

  constructor(
    private fb: FormBuilder,
    private userService: UserControllerService,
    private snackBar: MatSnackBar
  ) {
    // Inizializza il form
    this.passwordForm = this.fb.group({
      oldPassword: ['', Validators.required], // Password attuale obbligatoria
      newPassword: ['', [Validators.required, Validators.minLength(6)]], // Minimo 6 caratteri
    });
  }

  // **Invio del form per aggiornare la password**
  onSubmit(): void {
    if (this.passwordForm.valid) {
      const credentials: Credentials = this.passwordForm.value; // Prepara i dati

      // Chiamata API per aggiornare la password
      this.userService.changePassword({ body: credentials }).subscribe({
        next: (response) => {
          if (response.success) {
            // Successo: Mostra snackbar verde
            this.snackBar.open('Password aggiornata con successo!', 'Chiudi', {
              duration: 3000,
              panelClass: ['success-snackbar'],
            });
            this.errorMessage = null; // Resetta eventuali errori
            this.passwordForm.reset(); // Pulisce il form
          } else {
            // Errore dal backend
            this.errorMessage = response.message || 'Errore durante l’aggiornamento.';
            this.resetErrorMessage(); // Resetta l'errore dopo 3 secondi
          }
        },
        error: (err) => {
          // Errore generico
          console.error('Errore durante l’aggiornamento:', err);
          this.errorMessage =
            err.error?.message || 'Errore durante l’aggiornamento. Riprova più tardi.';
          this.resetErrorMessage(); // Resetta l'errore dopo 3 secondi
        },
      });
    }
  }

  // **Reset automatico del messaggio di errore**
  private resetErrorMessage(): void {
    setTimeout(() => {
      this.errorMessage = null; // Rimuove l'errore dopo 3 secondi
    }, 3000);
  }
}
