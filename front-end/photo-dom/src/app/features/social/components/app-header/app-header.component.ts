import { Component, Input, OnInit } from '@angular/core';
import { PhotoControllerService } from '../../../../../services/photo-service/services';
import { UserControllerService } from '../../../../../services/user-service/services';

@Component({
  selector: 'app-app-header',
  standalone: false,
  
  templateUrl: './app-header.component.html',
  styleUrl: './app-header.component.css'
})
export class AppHeaderComponent implements OnInit {
  private _userId!: string;

  @Input()
  set userId(value: string | undefined) {
    if (value) {
      this._userId = value;
    } else {
      console.error('userId è obbligatorio');
      this._userId = 'defaultUserId'; // Fallback o valore di default
    }
  }

  get userId(): string {
    return this._userId;
  }

  profileImage: string | null = null; // URL della foto profilo
  nickname: string = 'Anonymous'; // Nickname dell'utente

  constructor(
    private userService: UserControllerService,
    private photoService: PhotoControllerService // Servizio per ottenere la foto
  ) {}

  ngOnInit(): void {
    this.loadUserProfile();
  }

  private loadUserProfile(): void {
    this.userService.getProfileView({ id: +this.userId }).subscribe({
      next: (response) => {
        if (response.success && response.data) {
          this.nickname = response.data.nickname || 'Anonymous';
          if (response.data.photoProfileId) {
            this.loadProfilePhoto(response.data.photoProfileId);
          }
        } else {
          console.error(
            'Errore nel caricamento del profilo utente:',
            response.message
          );
        }
      },
      error: (err) => {
        console.error('Errore nel caricamento del profilo utente:', err);
      },
    });
  }

  private loadProfilePhoto(photoId: number): void {
    this.photoService.getPhotoById({ photoId }).subscribe({
      next: (response) => {
        if (response.success && response.data) {
          this.profileImage = `data:${response.data.contentType};base64,${response.data.imageBase64}`;
        } else {
          console.error(
            'Errore nel caricamento della foto profilo:',
            response.message
          );
        }
      },
      error: (err) => {
        console.error('Errore nel caricamento della foto profilo:', err);
      },
    });
  }
}