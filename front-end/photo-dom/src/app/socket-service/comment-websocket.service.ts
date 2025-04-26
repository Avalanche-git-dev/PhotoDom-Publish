import { Injectable } from '@angular/core';
import { environment } from '../../environments/environment';

@Injectable({
  providedIn: 'root',
})
export class CommentWebSocketService {
  private socket!: WebSocket;
  // private url: string = 'ws://localhost:8080/ws/comments';
  private url: string = environment.apiRootUrl.replace('http', 'ws') + '/ws/comments';  
  private listeners: ((message: string) => void)[] = [];

  connect(): void {
    if (!this.socket || this.socket.readyState === WebSocket.CLOSED) {
      this.socket = new WebSocket(this.url);

      this.socket.onopen = () => {
        console.log('WebSocket Comment Service connesso');
      };

      this.socket.onmessage = (event) => {
        console.log('Messaggio ricevuto dal WebSocket:', event.data);
        this.notifyListeners(event.data);
      };

      this.socket.onclose = () => {
        console.log('WebSocket Comment Service chiuso');
      };

      this.socket.onerror = (error) => {
        console.error('Errore WebSocket Comment Service:', error);
      };
    }
  }

  disconnect(): void {
   
  }

  addListener(callback: (message: string) => void): void {
    this.listeners.push(callback);
  }

  removeListener(callback: (message: string) => void): void {
    this.listeners = this.listeners.filter((listener) => listener !== callback);
  }

  private notifyListeners(message: string): void {
    this.listeners.forEach((listener) => listener(message));
  }
}



