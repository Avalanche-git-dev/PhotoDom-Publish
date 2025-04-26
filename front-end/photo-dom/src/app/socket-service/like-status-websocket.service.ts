import { Injectable } from '@angular/core';
import { Subject, Observable } from 'rxjs';
import { environment } from '../../environments/environment';

@Injectable({
  providedIn: 'root',
})
export class LikeStatusWebSocketService {
  private webSocket: WebSocket | null = null;
  private likeStatusSubject = new Subject<{
    photoId: number;
    liked: boolean;
  }>();
  private readonly url = environment.apiRootUrl.replace('http', 'ws') + '/ws/like/status';
  constructor() {}

  connect(): void {
    // this.webSocket = new WebSocket('ws://localhost:8080/ws/like/status');
    this.webSocket = new WebSocket(this.url);

    

    this.webSocket.onopen = () => {
      console.log('WebSocket LikeStatus connesso');
    };

    this.webSocket.onmessage = (event) => {
      console.log('Messaggio WebSocket ricevuto:', event.data);
      try {
        // Parsing del messaggio personalizzato
        const data = event.data.match(/{(\d+)=(true|false)}/);
        if (data) {
          const photoId = parseInt(data[1], 10);
          const liked = data[2] === 'true';
          this.likeStatusSubject.next({ photoId, liked });
        } else {
          console.error(
            'Formato del messaggio WebSocket non valido:',
            event.data
          );
        }
      } catch (error) {
        console.error('Errore nel parsing del messaggio WebSocket:', error);
      }
    };

    this.webSocket.onerror = (event) => {
      console.error('Errore WebSocket LikeStatus:', event);
    };

    this.webSocket.onclose = () => {
      console.log('WebSocket LikeStatus disconnesso');
    };
  }

  getLikeStatusUpdates(): Observable<{ photoId: number; liked: boolean }> {
    return this.likeStatusSubject.asObservable();
  }

  disconnect(): void {
    if (this.webSocket) {
      this.webSocket.close();
      console.log('WebSocket LikeStatus chiuso');
    }
  }
}
