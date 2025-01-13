import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { CommentControllerService } from '../../../../../services/comment-service/services';
import { CommentWebSocketService } from '../../../../socket-service/comment-websocket.service';
import { PhotoControllerService } from '../../../../../services/photo-service/services';
import { Router } from '@angular/router';
import { MatSnackBar } from '@angular/material/snack-bar';

@Component({
  selector: 'app-post-card',
  standalone: false,
  
  templateUrl: './post-card.component.html',
  styleUrl: './post-card.component.css'
})
export class PostCardComponent implements OnInit {
 
  @Input() photoUrl!: string; // URL della foto
  @Input() photoId!: string; // ID della foto (necessario per i commenti)
  @Input() ownerId!: string; // ID del proprietario
  @Input() likeCount: number = 0; // Numero di like
  @Input() commentCount: number = 0; // Numero di commenti
  @Input() liked: boolean = false; // Stato del like
  
  @Output() likeToggle = new EventEmitter<void>();
  @Output() postDeleted = new EventEmitter<number>(); // Evento per toggle like

  showComments: boolean = false; // Stato della visualizzazione dei commenti
  canDelete: boolean = false;
  

  constructor(
    private commentService: CommentControllerService,
    private commentWebSocketService: CommentWebSocketService,
    private photoService: PhotoControllerService,
    private router: Router,
    private snackBar: MatSnackBar, // Importiamo MatSnackBar
  ) {}

  ngOnInit(): void {
    this.loadMainCommentCount();
    this.checkDeletePermission(); // Carica il conteggio iniziale dei commenti
    this.commentWebSocketService.connect(); // Connessione al WebSocket
    this.commentWebSocketService.addListener(this.handleWebSocketMessage.bind(this));
  }

  ngOnDestroy(): void {
    this.commentWebSocketService.removeListener(this.handleWebSocketMessage.bind(this));
    this.commentWebSocketService.disconnect(); // Disconnessione dal WebSocket
  }

  toggleLike(): void {
    this.likeToggle.emit(); // Informa il genitore che l'utente ha cliccato su like
  }

  toggleComments(): void {
    this.showComments = !this.showComments; // Mostra o nasconde i commenti
  }

  private loadMainCommentCount(): void {
    // Richiesta al backend per il conteggio dei commenti principali
    this.commentService.countMainComments({ photoId: this.photoId }).subscribe({
      next: (response) => {
        if (response.success) {
          this.commentCount = response.data || 0; // Imposta il numero di commenti
        } else {
          console.error('Errore nel conteggio dei commenti:', response.message);
          this.commentCount = 0; // Imposta a 0 in caso di errore
        }
      },
      error: (err) => {
        console.error('Errore nel caricamento del conteggio dei commenti:', err);
        this.commentCount = 0; // Imposta a 0 in caso di errore
      },
    });
  }

  private handleWebSocketMessage(message: string): void {
    const [action] = message.split(':').map((part) => part.trim());

    switch (action) {
      case 'Comment added':
        // Incrementa il conteggio dei commenti principali
        this.commentCount++;
        break;

      case 'Comment deleted':
        // Decrementa il conteggio dei commenti principali
        this.commentCount = Math.max(0, this.commentCount - 1);
        break;

      default:
        console.warn(`Azione WebSocket non riconosciuta: ${action}`);
    }
  }


  updateCommentCount(newCount: number): void {
    this.commentCount = newCount; // Aggiorna il numero di commenti
  }
  

  reloadComments(): void {
    this.loadMainCommentCount(); // Ricarica il conteggio dei commenti
  }

  private checkDeletePermission(): void {
    const storedUser = localStorage.getItem('user');
    if (storedUser) {
      const currentUser = JSON.parse(storedUser);
      this.canDelete =
        currentUser.role === 'ADMIN' || currentUser.id === this.ownerId;
    }
  }

  handleDeletePost(): void {
    this.photoService.deletePhoto({ photoId: +this.photoId }).subscribe({
      next: (response) => {
        if (response.success) {
          console.log(`Post con ID ${this.photoId} eliminato con successo.`);
          this.snackBar.open(response.message || 'Post eliminato con successo!', 'Chiudi', {
            duration: 3000,
          });
          this.postDeleted.emit(+this.photoId); // Notifica al genitore l'eliminazione
          // this.router.navigate(['/social']); // Reindirizza o aggiorna la lista dei post
        } else {
          this.snackBar.open(response.message || 'Errore durante l\'eliminazione.', 'Chiudi', {
            duration: 3000,
          });
        }
      },
      error: (err) => {
        console.error('Errore durante l\'eliminazione del post:', err);
        this.snackBar.open('Errore durante l\'eliminazione del post.', 'Chiudi', {
          duration: 3000,
        });
      },
    });
  }
  
  
  
}

