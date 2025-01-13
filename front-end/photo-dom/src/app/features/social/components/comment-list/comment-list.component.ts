import { Component, EventEmitter, Input, OnDestroy, OnInit, Output } from '@angular/core';
import { CommentControllerService } from '../../../../../services/comment-service/services';
import { CommentDto } from '../../../../../services/comment-service/models';
import { CommentWebSocketService } from '../../../../socket-service/comment-websocket.service';

@Component({
  selector: 'app-comment-list',
  templateUrl: './comment-list.component.html',
  styleUrls: ['./comment-list.component.css'],
  standalone: false,
})
export class CommentListComponent implements OnInit, OnDestroy {
  @Input() photoId!: string; // ID della foto
  @Input() parentId: string | null = null; // ID del commento principale (per le risposte)
  @Input() initialCommentCount: number = 0; // Conteggio iniziale dei commenti
  @Output() commentCountChange = new EventEmitter<number>(); // Evento per notificare il conteggio

  comments: CommentDto[] = []; // Lista dei commenti
  isLoading: boolean = false; // Stato del caricamento

  constructor(
    private commentService: CommentControllerService,
    private commentWebSocketService: CommentWebSocketService
  ) {}

  ngOnInit(): void {
    this.checkAndLoadComments(); // Verifica e carica i commenti

    this.commentWebSocketService.addListener(this.handleWebSocketMessage.bind(this));
  }

  ngOnDestroy(): void {
    this.commentWebSocketService.removeListener(this.handleWebSocketMessage.bind(this));
  }

  private checkAndLoadComments(): void {
    if (this.initialCommentCount === 0) {
      console.log('Nessun commento iniziale da caricare.');
      return; // Evita chiamate HTTP inutili
    }

    this.loadComments();
  }

  loadComments(): void {
    this.isLoading = true;

    if (this.parentId) {
      console.log('Caricamento risposte per parentId:', this.parentId);
      this.commentService.getReplies({ id: this.parentId }).subscribe({
        next: (response) => {
          this.comments = this.sortComments(response);
          this.commentCountChange.emit(this.comments.length);
          this.isLoading = false;
        },
        error: (err) => {
          console.error('Errore nel caricamento delle risposte:', err);
          this.isLoading = false;
        },
      });
    } else {
      console.log('Caricamento commenti principali per photoId:', this.photoId);
      this.commentService.getCommentsByPhoto({ photoId: this.photoId }).subscribe({
        next: (response) => {
          this.comments = this.sortComments(response.filter((comment) => !comment.parentCommentId));
          this.commentCountChange.emit(this.comments.length);
          this.isLoading = false;
        },
        error: (err) => {
          console.error('Errore nel caricamento dei commenti principali:', err);
          this.isLoading = false;
        },
      });
    }
  }

  private sortComments(comments: CommentDto[]): CommentDto[] {
    // Ordina i commenti per data di creazione (dal più recente al più vecchio)
    return comments.sort((a, b) => {
      const dateA = new Date(a.createdDate || '').getTime();
      const dateB = new Date(b.createdDate || '').getTime();
      return dateB - dateA; // Ordine decrescente
    });
  }
  

  handleCommentDeleted(): void {
    this.loadComments(); // Ricarica i commenti dopo l'eliminazione
  }

  private handleWebSocketMessage(message: string): void {
    const [action, commentId] = message.split(':').map((part) => part.trim());

    switch (action) {
      case 'Comment added':
      case 'Reply added':
        this.loadSingleComment(commentId);
        break;

      case 'Comment deleted':
        this.comments = this.comments.filter((comment) => comment.id !== commentId);
        this.commentCountChange.emit(this.comments.length);
        break;

      default:
        console.warn(`Azione WebSocket non riconosciuta: ${action}`);
    }
  }

  private loadSingleComment(commentId: string): void {
    if (this.parentId) {
      this.commentService.getReplies({ id: this.parentId }).subscribe({
        next: (comments) => {
          const newComment = comments.find((comment) => comment.id === commentId);
          if (newComment) {
            this.comments = [newComment, ...this.comments];
            this.commentCountChange.emit(this.comments.length);
          }
        },
        error: (err) => {
          console.error('Errore nel caricamento del commento aggiunto:', err);
        },
      });
    } else {
      this.commentService.getCommentsByPhoto({ photoId: this.photoId }).subscribe({
        next: (comments) => {
          const newComment = comments.find((comment) => comment.id === commentId);
          if (newComment) {
            this.comments = [newComment, ...this.comments];
            this.commentCountChange.emit(this.comments.length);
          }
        },
        error: (err) => {
          console.error(`Errore nel caricamento del commento con ID: ${commentId}`);
        },
      });
    }
  }
}