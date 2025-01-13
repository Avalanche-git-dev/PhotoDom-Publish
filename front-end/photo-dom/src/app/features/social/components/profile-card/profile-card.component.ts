
import { Component, OnInit, OnDestroy } from '@angular/core';
import { of, Subscription } from 'rxjs';
import { UserControllerService } from '../../../../../services/user-service/services';
import { UserResponseUserDto } from '../../../../../services/user-service/models/user-response-user-dto';
import { UserDto } from '../../../../../services/user-service/models/user-dto';
import { PhotoControllerService } from '../../../../../services/photo-service/services';
import { PhotoResponsePhotoDto } from '../../../../../services/photo-service/models/photo-response-photo-dto';
import { UserWebSocketService } from '../../../../socket-service/user-websocket.service';

@Component({
  selector: 'app-profile-card',
  templateUrl: './profile-card.component.html',
  styleUrls: ['./profile-card.component.css'],
  standalone: false
})
export class ProfileCardComponent implements OnInit, OnDestroy {
  user: UserDto | null = null; // Dati utente
  // profileImage: string = 'assets/default-profile.png'; // Placeholder immagine profilo
  profileImage: string | null = null; 
  private socketSubscription!: Subscription; // WebSocket Subscription

  constructor(
    private userService: UserControllerService,
    private photoService: PhotoControllerService,
    private webSocketService: UserWebSocketService
  ) {}

  ngOnInit(): void {
    // Recupera l'ID utente dal localStorage
    const storedUser = localStorage.getItem('user');
    const userId = storedUser ? JSON.parse(storedUser).id : null;

    // Verifica se l'ID è presente
    if (!userId) {
      console.error('ID utente non trovato nel localStorage.');
      return;
    }

    // **Carica inizialmente il profilo utente**
    this.loadUserProfile(userId);

    // **Connessione al WebSocket**
    this.webSocketService.connect();

    this.socketSubscription = this.webSocketService.getMessages().subscribe((message) => {
      console.log('Messaggio ricevuto dal WebSocket (raw):', message);
    
      // Crea il messaggio atteso
      const expectedMessage = `User profile updated: ${userId}`;
      console.log('Messaggio atteso:', expectedMessage);
    
      // Confronta direttamente il messaggio ricevuto
      if (message.trim() === expectedMessage) {
        console.log('Aggiornamento rilevato. Ricarico il profilo...');
        this.loadUserProfile(userId);
      } else {
        console.log('Nessun aggiornamento rilevato per l\'utente corrente.');
      }
    });
    
    
    
  }

  // **Carica il profilo utente**
  private loadUserProfile(userId: number): void {
    this.userService.getUserById({ id: userId }).subscribe({
      next: (response: UserResponseUserDto) => {
        if (response && response.success && response.data) {
          this.user = response.data; // Assegna i dati dell'utente
          console.log('Profilo utente caricato:', this.user);

          // Carica la foto profilo se disponibile
          if (this.user.photoProfileId) {
            this.loadProfilePhoto(this.user.photoProfileId);
          } else {
            this.profileImage = 'assets/default-profile.png'; // Foto predefinita
          }
        } else {
          console.error('Profilo utente non disponibile:', response.message);
        }
      },
      error: (err) => {
        console.error('Errore nel caricamento del profilo:', err);
      },
    });
  }

  // **Carica la foto profilo**
  private loadProfilePhoto(photoId: number): void {
    this.photoService.getPhotoById({ photoId }).subscribe({
      next: (response: PhotoResponsePhotoDto) => {
        if (response && response.success && response.data) {
          // Decodifica l'immagine base64
          const imageBase64 = `data:${response.data.contentType};base64,${response.data.imageBase64}`;
          this.profileImage = imageBase64; // Imposta l'immagine nella card
        } else {
          console.error('Foto profilo non disponibile:', response.message);
        }
      },
      error: (err) => {
        console.error('Errore nel caricamento della foto profilo:', err);
      },
    });
  }

  ngOnDestroy(): void {
    this.socketSubscription.unsubscribe(); // Disconnette il WebSocket
    this.webSocketService.disconnect(); // Disconnette il servizio
  }
}
