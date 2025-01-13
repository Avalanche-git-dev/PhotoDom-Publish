import { Component, OnInit } from '@angular/core';
import { AuthService } from '../../../../../services/auth-service/auth.service';
import { Router } from '@angular/router';
import { MatDialog } from '@angular/material/dialog';
import { UploadPhotoModalComponent } from '../upload-photo-modal/upload-photo-modal.component';
import { MatSnackBar } from '@angular/material/snack-bar';

@Component({
  selector: 'app-navbar',
  standalone: false,
  
  templateUrl: './navbar.component.html',
  styleUrl: './navbar.component.css'
})
export class NavbarComponent implements OnInit {
  isAdmin: boolean = false;

constructor(
  private authService: AuthService,
  private router: Router,
  private dialog: MatDialog,
  private snackBar: MatSnackBar,
) {}


logout(): void {
  this.authService.logout();
  this.snackBar.open('Logout eseguito con successo!', 'Chiudi', {
    duration: 3000,
    panelClass: 'snackbar-success', // Classe CSS per il colore
  });
  this.router.navigate(['/login']);
}

ngOnInit(): void {
  const user = JSON.parse(localStorage.getItem('user') || '{}');
  this.isAdmin = user.role === 'ADMIN';
}


// **Apre il modale per il caricamento della foto**
openUploadModal(): void {
  this.dialog.open(UploadPhotoModalComponent, {
    width: '400px', // Larghezza del modale
    data: {}, // Dati opzionali da passare
  });
}
}