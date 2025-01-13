import { Component } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { PhotoSelectionModalComponent } from '../photo-selection-modal/photo-selection-modal.component';
import { UploadPhotoModalComponent } from '../upload-photo-modal/upload-photo-modal.component';

@Component({
  selector: 'app-profile-photo-card',
  standalone: false,
  
  templateUrl: './profile-photo-card.component.html',
  styleUrl: './profile-photo-card.component.css'
})
export class ProfilePhotoCardComponent {
  constructor(private dialog: MatDialog) {}

  // Apre il modal per caricare una nuova foto
  openUploadPhotoModal(): void {
    const dialogRef = this.dialog.open(UploadPhotoModalComponent, {
      width: '400px',
    });

    dialogRef.afterClosed().subscribe((result) => {
      if (result) {
        console.log('Foto caricata con successo:', result);
      } else {
        console.log('Caricamento foto annullato.');
      }
    });
  }

  // Apre il modal per selezionare una foto esistente
  openPhotoSelectionModal(): void {
    const dialogRef = this.dialog.open(PhotoSelectionModalComponent, {
      width: '800px',
    });

    dialogRef.afterClosed().subscribe((selectedPhotoId) => {
      if (selectedPhotoId) {
        console.log('Foto profilo aggiornata con ID:', selectedPhotoId);
      } else {
        console.log('Nessuna foto selezionata.');
      }
    });
  }

}
