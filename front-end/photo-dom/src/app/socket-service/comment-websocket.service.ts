import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root',
})
export class CommentWebSocketService {
  private socket!: WebSocket;
  private url: string = 'ws://localhost:8080/ws/comments'; // URL del WebSocket
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


// import { Injectable } from '@angular/core';

// @Injectable({
//   providedIn: 'root',
// })
// export class CommentWebSocketService {
//   private socket!: WebSocket;
//   private url: string = 'ws://localhost:8080/ws/comments'; // URL del WebSocket
//   private listeners: ((message: { action: string; commentId: string }) => void)[] = [];

//   connect(): void {
//     if (!this.socket || this.socket.readyState === WebSocket.CLOSED) {
//       this.socket = new WebSocket(this.url);

//       this.socket.onopen = () => {
//         console.log('WebSocket Comment Service connesso');
//       };

//       this.socket.onmessage = (event) => {
//         console.log('Messaggio ricevuto dal WebSocket:', event.data);
//         try {
//           const parsedMessage = this.parseMessage(event.data);
//           if (parsedMessage) {
            
//           }
//         } catch (error) {
//           console.error('Errore nella gestione del messaggio WebSocket:', error);
//         }
//       };

//       this.socket.onclose = () => {
//         console.log('WebSocket Comment Service chiuso');
//       };

//       this.socket.onerror = (error) => {
//         console.error('Errore WebSocket Comment Service:', error);
//       };
//     }
//   }

//   disconnect(): void {
//     if (this.socket && this.socket.readyState !== WebSocket.CLOSED) {
//       this.socket.close();
//       console.log('WebSocket Comment Service disconnesso manualmente');
//     }
//   }

 

//   private parseMessage(message: string): { action: string; commentId: string } | null {
//     // Controlla il formato del messaggio e lo divide
//     const parts = message.split(':');
//     if (parts.length === 2) {
//       return {
//         action: parts[0].trim(), // Es. "Comment added"
//         commentId: parts[1].trim(), // Es. "67800a9c1b812d0e27c7bd7a"
//       };
//     }
//     console.error('Formato messaggio WebSocket non valido:', message);
//     return null;
//   }
// }
