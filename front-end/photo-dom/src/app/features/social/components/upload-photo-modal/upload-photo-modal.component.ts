// import { Component } from '@angular/core';
// import { MatDialogRef } from '@angular/material/dialog';
// import { PhotoControllerService } from '../../../../../services/photo-service/services';

// @Component({
//   selector: 'app-upload-photo-modal',
//   standalone: false,
  
//   templateUrl: './upload-photo-modal.component.html',
//   styleUrl: './upload-photo-modal.component.css'
// })
// export class UploadPhotoModalComponent {
//   selectedFile: File | null = null;
//   isUploading: boolean = false;
//   uploadSuccess: boolean | null = null;

//   constructor(
//     private dialogRef: MatDialogRef<UploadPhotoModalComponent>,
//     private photoService: PhotoControllerService
//   ) {}

//   // Metodo per gestire la selezione del file
//   onFileSelected(event: Event): void {
//     const input = event.target as HTMLInputElement;
//     if (input.files && input.files.length > 0) {
//       this.selectedFile = input.files[0];
//       console.log('File selezionato:', this.selectedFile);
//     }
//   }

//   uploadPhoto(): void {
//     if (!this.selectedFile) {
//       console.error('Nessun file selezionato.');
//       return;
//     }

//      // Crea un oggetto Blob dal file selezionato
//   const blob = new Blob([this.selectedFile], { type: this.selectedFile.type });

//     this.photoService.uploadPhoto({ body: { file: blob } }).subscribe({
//       next: (response) => {
//         console.log('Upload completato con successo:', response);
//       },
//       error: (err) => {
//         console.error('Errore durante l\'upload:', err);
//       },
//     });
//   }
  
  

//   // Metodo per chiudere il modal
//   closeModal(): void {
//     this.dialogRef.close();
//   }
// }




import { Component } from '@angular/core';
import { MatDialogRef } from '@angular/material/dialog';
import { PhotoControllerService } from '../../../../../services/photo-service/services';

@Component({
  selector: 'app-upload-photo-modal',
  templateUrl: './upload-photo-modal.component.html',
  styleUrls: ['./upload-photo-modal.component.css'],
  standalone:false
})
export class UploadPhotoModalComponent {
  selectedFile: File | null = null;
  isUploading: boolean = false;
  responseMessage: string = ''; // Messaggio di risposta da visualizzare
  isError: boolean = false; // Stato per distinguere successo da errore

  constructor(
    private dialogRef: MatDialogRef<UploadPhotoModalComponent>,
    private photoService: PhotoControllerService
  ) {}

  // Metodo per gestire la selezione del file
  onFileSelected(event: Event): void {
    const input = event.target as HTMLInputElement;
    if (input.files && input.files.length > 0) {
      this.selectedFile = input.files[0];
      console.log('File selezionato:', this.selectedFile);
    }
  }

  // Metodo per caricare la foto
  uploadPhoto(): void {
    if (!this.selectedFile) {
      this.responseMessage = 'Nessun file selezionato.';
      this.isError = true;
      console.error(this.responseMessage);
      return;
    }

    this.isUploading = true; // Mostra lo stato di caricamento

    // Crea un oggetto Blob dal file selezionato
    const blob = new Blob([this.selectedFile], { type: this.selectedFile.type });

    this.photoService.uploadPhoto({ body: { file: blob } }).subscribe({
      next: (response) => {
        console.log('Risposta del server:', response);
        this.responseMessage = response.message || 'Caricamento completato.';
        this.isError = false; // Stato di successo
      },
      error: (err) => {
        console.error('Errore durante il caricamento:', err);
        this.responseMessage = err.error?.message || 'Errore durante il caricamento.';
        this.isError = true; // Stato di errore
      },
      complete: () => {
        this.isUploading = false; // Nasconde lo stato di caricamento
      },
    });
  }

  // Metodo per chiudere il modal
  closeModal(): void {
    this.dialogRef.close();
  }
}
