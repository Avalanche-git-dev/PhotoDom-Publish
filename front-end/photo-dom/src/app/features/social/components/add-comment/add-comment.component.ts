import { Component, EventEmitter, Input, Output } from '@angular/core';
import { CommentControllerService } from '../../../../../services/comment-service/services';
import { CommentResponseString } from '../../../../../services/comment-service/models';

@Component({
  selector: 'app-add-comment',
  standalone: false,
  
  templateUrl: './add-comment.component.html',
  styleUrl: './add-comment.component.css'
})
export class AddCommentComponent {
  // @Input() photoId!: string; // ID della foto a cui si riferisce il commento
  // @Input() parentId?: string; // ID del commento padre (opzionale per risposte)
  // @Output() commentAdded = new EventEmitter<void>(); // Evento per notificare il genitore

  // newComment: string = ''; // Contenuto del nuovo commento

  // constructor(private commentService: CommentControllerService) {}

  // addComment(): void {
  //   if (!this.newComment.trim()) {
  //     return; // Evita l'invio di commenti vuoti
  //   }

  //   const commentBody = {
  //     content: this.newComment,
  //     photoId: this.photoId,
  //     parentCommentId: this.parentId || undefined, // Gestione corretta di undefined
  //   };

  //   this.commentService.addComment({ body: commentBody }).subscribe({
  //     next: () => {
  //       this.newComment = ''; // Resetta il campo del commento
  //       this.commentAdded.emit(); // Notifica il componente genitore
  //       console.log('Commento aggiunto con successo');
  //     },
  //     error: (err) => {
  //       console.error('Errore nell\'aggiunta del commento:', err);
  //     },
  //   });
  // }

  @Input() photoId!: string; // ID della foto a cui si riferisce il commento
  @Input() parentId?: string; // ID del commento padre (opzionale per risposte)
  @Output() commentAdded = new EventEmitter<void>(); // Evento per notificare il genitore

  newComment: string = ''; // Contenuto del nuovo commento
  message: string | null = null; // Messaggio di stato
  isSuccess: boolean = false; // Stato del successo

  private messageTimeout!: ReturnType<typeof setTimeout>; // Per gestire il timeout del messaggio

  constructor(private commentService: CommentControllerService) {}

  addComment(): void {
    if (!this.newComment.trim()) {
      return; // Evita l'invio di commenti vuoti
    }

    const commentBody = {
      content: this.newComment,
      photoId: this.photoId,
      parentCommentId: this.parentId || undefined, // Gestione corretta di undefined
    };

    this.commentService.addComment({ body: commentBody }).subscribe({
      next: (response: CommentResponseString) => {
        this.newComment = ''; // Resetta il campo del commento
        this.isSuccess = !!response.success;
        this.message = response.message || 'Operazione completata.';
        this.startMessageTimer();

        if (response.success) {
          this.commentAdded.emit(); // Notifica il componente genitore solo in caso di successo
        }
      },
      error: (err) => {
        console.error('Errore nell\'aggiunta del commento:', err);
        this.isSuccess = false;
        this.message = 'Errore imprevisto durante l\'aggiunta del commento.';
        this.startMessageTimer();
      },
    });
  }

  private startMessageTimer(): void {
    clearTimeout(this.messageTimeout); // Resetta il timer precedente
    this.messageTimeout = setTimeout(() => {
      this.message = null; // Nascondi il messaggio dopo 2 secondi
    }, 2000);
  }
}