import { Injectable } from '@angular/core';
import {
  HttpEvent,
  HttpHandler,
  HttpInterceptor,
  HttpRequest,
  HttpErrorResponse,
} from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { AuthService } from '../auth-service/auth.service';

@Injectable({
  providedIn: 'root',
})
export class AuthInterceptor implements HttpInterceptor {
  // **Lista di URL esclusi dall'interceptor**
  private excludedUrls = [
    'http://localhost:8180/realms/PhotoDom/protocol/openid-connect/token', // Login
    'http://localhost:8180/realms/PhotoDom/protocol/openid-connect/logout', // Logout
    'http://localhost:8080/api/users/register' // Registrazione utente
  ];

  constructor(private authService: AuthService) {}

  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    // **Se l'URL è nella lista esclusa, bypassa l'interceptor**
    if (this.isExcludedUrl(req.url)) {
      return next.handle(req);
    }

    // Recupera il token dal servizio AuthService
    const accessToken = this.authService.getAccessToken();

    // Aggiunge il token se disponibile
    let clonedReq = req;
    if (accessToken) {
      clonedReq = this.addToken(req, accessToken);
    }

    return next.handle(clonedReq).pipe(
      catchError((error) => {
        return throwError(() => error); // Propaga l'errore
      })
    );
  }

  // **Controlla se l'URL è escluso**
  private isExcludedUrl(url: string): boolean {
    return this.excludedUrls.some(excludedUrl => url.includes(excludedUrl));
  }

  // **Aggiunge il token all'header Authorization**
  private addToken(request: HttpRequest<any>, token: string) {
    return request.clone({
      setHeaders: {
        Authorization: `Bearer ${token}`,
      },
    });
  }
}
