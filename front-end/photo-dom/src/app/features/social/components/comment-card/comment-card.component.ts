

import { Component, Input } from '@angular/core';
import { MatSnackBar } from '@angular/material/snack-bar';
import { CommentDto } from '../../../../../services/comment-service/models';
import { CommentControllerService } from '../../../../../services/comment-service/services';

@Component({
  selector: 'app-comment-card',
  templateUrl: './comment-card.component.html',
  styleUrls: ['./comment-card.component.css'],
  standalone: false,
})
export class CommentCardComponent {
  @Input() comment!: CommentDto; // Dati del commento
  @Input() photoId!: string; // ID della foto
  showReplies: boolean = false; // Controlla la visibilità delle risposte
  showReplyForm: boolean = false; // Controlla la visibilità del form per rispondere
  canDelete: boolean = false; // Permessi per eliminare
  replyCount: number = 0; // Conteggio delle risposte

  constructor(
    private snackBar: MatSnackBar,
    private commentService: CommentControllerService
  ) {}

  ngOnInit(): void {
    this.checkDeletePermission();
    this.loadReplyCount(); // Carica il conteggio delle risposte
  }

  checkDeletePermission(): void {
    const user = JSON.parse(localStorage.getItem('user') || '{}');
    this.canDelete = user.id === this.comment.userId;
  }

  toggleReplies(): void {
    this.showReplies = !this.showReplies;

    if (this.showReplies && this.replyCount > 0) {
      console.log(`Caricamento risposte per commento ID: ${this.comment.id}`);
    }
  }

  toggleReplyForm(): void {
    this.showReplyForm = !this.showReplyForm;
  }

  deleteComment(): void {
    this.commentService.deleteComment({ id: this.comment.id! }).subscribe({
      next: () => {
        this.snackBar.open('Commento eliminato con successo!', 'Chiudi', {
          duration: 3000,
          panelClass: ['success-snackbar'],
        });
      },
      error: (err) => {
        console.error('Errore nell\'eliminazione del commento:', err);
      },
    });
  }

  private loadReplyCount(): void {
    this.commentService.countReplies({ parentCommentId: this.comment.id! }).subscribe({
      next: (response) => {
        this.replyCount = response.data || 0;
      },
      error: (err) => {
        console.error('Errore nel conteggio delle risposte:', err);
        this.replyCount = 0;
      },
    });
  }
}
