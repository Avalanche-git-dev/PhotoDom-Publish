import { Injectable } from '@angular/core';
import { Subject, Observable } from 'rxjs';
import { environment } from '../../environments/environment';

@Injectable({
  providedIn: 'root',
})
export class UserWebSocketService {
  private webSocket: WebSocket | null = null;
  private messageSubject = new Subject<string>();
  private readonly url = environment.apiRootUrl.replace('http', 'ws') + '/ws/users';
  constructor() {}

  connect(): void {
    // Apri connessione WebSocket tramite proxy
    // this.webSocket = new WebSocket('ws://localhost:8080/ws/users');
    this.webSocket = new WebSocket(this.url);


    this.webSocket.onopen = () => {
      console.log('WebSocket connesso');
    };

    this.webSocket.onmessage = (event) => {
      console.log('Messaggio WebSocket ricevuto:', event.data);
      this.messageSubject.next(event.data); // Pubblica il messaggio ricevuto
    };

    this.webSocket.onerror = (event) => {
      console.error('Errore WebSocket:', event);
    };

    this.webSocket.onclose = () => {
      console.log('WebSocket disconnesso');
    };
  }

  getMessages(): Observable<string> {
    return this.messageSubject.asObservable();
  }

  disconnect(): void {
    if (this.webSocket) {
      this.webSocket.close();
      console.log('WebSocket chiuso');
    }
  }
}
