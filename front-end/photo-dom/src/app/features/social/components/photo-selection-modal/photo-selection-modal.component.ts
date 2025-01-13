
import { Component, OnInit } from '@angular/core';
import { MatDialogRef } from '@angular/material/dialog';
import { MatSnackBar } from '@angular/material/snack-bar';
import { PhotoDto } from '../../../../../services/photo-service/models';
import { PhotoControllerService } from '../../../../../services/photo-service/services';
import { UserControllerService } from '../../../../../services/user-service/services';

@Component({
  selector: 'app-photo-selection-modal',
  templateUrl: './photo-selection-modal.component.html',
  styleUrls: ['./photo-selection-modal.component.css'],
  standalone: false
})
export class PhotoSelectionModalComponent implements OnInit {
  photos: { id: number; thumbnailUrl: string }[] = [];
  isLoading: boolean = true;

  constructor(
    private dialogRef: MatDialogRef<PhotoSelectionModalComponent>,
    private photoService: PhotoControllerService,
    private userService: UserControllerService,
    private snackBar: MatSnackBar
  ) {}

  ngOnInit(): void {
    // Carica le foto dell'utente
    this.photoService.getPhotosBatchByUser({ page: 0, size: 50 }).subscribe({
      next: (response: PhotoDto[]) => {
        this.photos = response.map((photo) => ({
          id: photo.id!,
          thumbnailUrl: `data:${photo.contentType};base64,${photo.imageBase64}`,
        }));
        this.isLoading = false;
      },
      error: (err) => {
        console.error('Errore nel caricamento delle foto:', err);
        this.isLoading = false;
      },
    });
  }

  // Metodo per selezionare una foto
  selectPhoto(photoId: number): void {
    console.log('Foto selezionata con ID:', photoId);

    // Aggiorna il profilo dell'utente con l'ID della foto
    this.userService.updateUser({ body: { photoProfileId: photoId } }).subscribe({
      next: (response) => {
        console.log('Risposta aggiorna profilo:', response);
        this.snackBar.open(response.message || 'Foto profilo aggiornata!', 'Chiudi', {
          duration: 3000,
          panelClass: ['success-snackbar'], // Classe CSS per personalizzare lo snackbar
        });
        this.dialogRef.close(photoId); // Chiudi il modale restituendo l'ID della foto selezionata
      },
      error: (err) => {
        console.error('Errore durante l\'aggiornamento del profilo:', err);
        this.snackBar.open(
          err.error?.message || 'Errore durante l’aggiornamento della foto profilo.',
          'Chiudi',
          {
            duration: 3000,
            panelClass: ['error-snackbar'], // Classe CSS per personalizzare lo snackbar in errore
          }
        );
      },
    });
  }

  // Metodo per chiudere il modale senza selezione
  closeModal(): void {
    this.dialogRef.close(null);
  }
}
