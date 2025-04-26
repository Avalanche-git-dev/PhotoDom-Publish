

import { Injectable } from "@angular/core";
import { Subject, Observable } from "rxjs";
import { environment } from "../../environments/environment";




@Injectable({
  providedIn: 'root',
})
export class PhotoWebSocketService {
  private webSocket: WebSocket | null = null;
  private notificationSubject = new Subject<string>();
  private readonly url = environment.apiRootUrl.replace('http', 'ws') + '/ws/photos';

  connect(): void {
    // this.webSocket = new WebSocket('ws://localhost:8080/ws/photos');
    this.webSocket = new WebSocket(this.url);


    this.webSocket.onmessage = (event) => {
      const message = event.data.trim(); // Rimuove spazi o caratteri strani
      if (message.startsWith('New photo uploaded:')) {
        const photoId = message.replace('New photo uploaded:', '').trim();
        this.notificationSubject.next(photoId);
      }
    };
  }

  getNotifications(): Observable<string> {
    return this.notificationSubject.asObservable();
  }


    disconnect(): void {
    if (this.webSocket) {
       this.webSocket.close();
      console.log('WebSocket Photo chiuso');
    }
  }
}
