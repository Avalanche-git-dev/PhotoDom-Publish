import { Component, Input, OnChanges, OnInit, SimpleChanges } from '@angular/core';
import { PhotoControllerService } from '../../../../../services/photo-service/services';
import { ProfileView } from '../../../../../services/user-service/models';
import { UserControllerService } from '../../../../../services/user-service/services';

@Component({
  selector: 'app-profile-view-card',
  standalone: false,
  
  templateUrl: './profile-view-card.component.html',
  styleUrl: './profile-view-card.component.css'
})
export class ProfileViewCardComponent implements OnChanges,OnInit {
  @Input() userId!: number; // ID utente ricevuto come input
  profile: ProfileView | null = null; // Dati del profilo
  profileImage: string | null = null; // URL della foto profilo // Immagine di default

  constructor(
    private userService: UserControllerService,
    private photoService: PhotoControllerService,
  ) {}

  ngOnInit(): void {
    this.loadProfile();
  }
  
  ngOnChanges(changes: SimpleChanges): void {
    if (changes['userId'] && changes['userId'].currentValue) {
      this.loadProfile(); // Ricarica il profilo ogni volta che cambia l'userId
    }
  }

  private loadProfile(): void {
    if (!this.userId) {
      console.error('ID utente non fornito.');
      return;
    }

    this.userService.getProfileView({ id: this.userId }).subscribe({
      next: (response) => {
        if (response.success && response.data) {
          this.profile = response.data;

          // Carica l'immagine profilo se presente
          if (this.profile.photoProfileId) {
            this.loadProfilePhoto(this.profile.photoProfileId);
          }
        } else {
          console.error('Errore nel caricamento del profilo:', response.message);
        }
      },
      error: (err) => {
        console.error('Errore nel caricamento del profilo:', err);
      },
    });
  }

  private loadProfilePhoto(photoId: number): void {
    this.photoService.getPhotoById({ photoId }).subscribe({
      next: (response) => {
        if (response.success && response.data) {
          this.profileImage = `data:${response.data.contentType};base64,${response.data.imageBase64}`;
        } else {
          console.error('Errore nel caricamento della foto:', response.message);
        }
      },
      error: (err) => {
        console.error('Errore nel caricamento della foto:', err);
        this.profileImage = 'assets/default-profile.png';
      },
    });
  }
}

