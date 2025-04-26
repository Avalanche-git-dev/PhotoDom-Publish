import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { BehaviorSubject, Observable, throwError } from 'rxjs';
import { catchError, tap } from 'rxjs/operators';
import {jwtDecode} from 'jwt-decode'; // Per decodificare il token JWT
import { User, TokenSet } from './models/auth.models';
import { Router } from '@angular/router';
import { environment } from '../../environments/environment';

// const KEYCLOAK_TOKEN_URL = 'http://localhost:8180/realms/PhotoDom/protocol/openid-connect/token';
// const KEYCLOAK_LOGOUT_URL = 'http://localhost:8180/realms/PhotoDom/protocol/openid-connect/logout';
const KEYCLOAK_TOKEN_URL = `${environment.keycloakUrl}/token`;
const KEYCLOAK_LOGOUT_URL = `${environment.keycloakUrl}/logout`;

@Injectable({
  providedIn: 'root',
})
export class AuthService {
  private userSubject = new BehaviorSubject<User | null>(null); // Stato utente
  private tokenSet: TokenSet | null = null; // Token salvati
  static sessionExpired: boolean = false;

  constructor(private http: HttpClient,private router: Router) {
    // Recupera i dati dal localStorage
   

    if(typeof window !== 'undefined' && window.localStorage){
      const storedTokenSet = localStorage.getItem('tokenSet');
      const storedUser = localStorage.getItem('user');

    if (storedTokenSet) {
      this.tokenSet = JSON.parse(storedTokenSet);
    }
    if (storedUser) {
      this.userSubject.next(JSON.parse(storedUser));
    }
   
  }
  
  }

  // **Login**
  login(username: string, password: string): Observable<TokenSet> {
    const body = new URLSearchParams();
    body.set('grant_type', 'password');
    body.set('client_id', 'angular-client'); // Configurazione del client su Keycloak
    body.set('username', username);
    body.set('password', password);
    body.set('scope', 'openid');

    const headers = new HttpHeaders({
      'Content-Type': 'application/x-www-form-urlencoded',
    });

    return this.http.post<any>(KEYCLOAK_TOKEN_URL, body.toString(), { headers }).pipe(
      tap((response) => {
        // Salva i token
        this.tokenSet = new TokenSet(
          response.access_token,
          response.refresh_token,
          response.id_token
        );
        localStorage.setItem('tokenSet', JSON.stringify(this.tokenSet));
       
        
        // Estrai i dati utente
        this.extractUserFromToken(response.access_token, response.id_token);
      }),
      catchError((error) => {
        // Restituisce direttamente l'errore di Keycloak al chiamante
        // console.log(error);
        const errorMessage = this.extractKeycloakError(error);
        return throwError(() => new Error(errorMessage));
      })
    );
  }

  // **Logout**
  logout(): void {
    if (!this.tokenSet) {
      console.warn('Logout già effettuato');
      return;
    }

    const body = new URLSearchParams();
    const refreshToken = this.tokenSet.refreshToken;

    body.set('client_id', 'angular-client');
    if (refreshToken) body.set('refresh_token', refreshToken);

    const headers = new HttpHeaders({
      'Content-Type': 'application/x-www-form-urlencoded',
    });

    this.http.post(KEYCLOAK_LOGOUT_URL, body.toString(), { headers }).subscribe({
      next: () => {
        this.clearSession();
      },
      error: () => {
        this.clearSession(); // Pulisce comunque i dati
      },
    });
  }

  // **Estrai i dati utente dai token**
  private extractUserFromToken(accessToken: string, idToken: string): void {
    const accessDecoded: any = jwtDecode(accessToken);
    const idDecoded: any = jwtDecode(idToken);

    const user = new User(
      accessDecoded.userId,          // Estratto dal claim `userId`
      idDecoded.Nickname,  // Estratto dal claim `preferred_username`
      accessDecoded.role || 'USER'   // Estratto dal claim `role`
    );

    // Salva l'utente nello stato e nel localStorage
    this.userSubject.next(user);
    localStorage.setItem('user', JSON.stringify(user));
  }

  // **Refresh Token**
  refreshAuthToken(): Observable<TokenSet> {
    const body = new URLSearchParams();
    const refreshToken = this.tokenSet?.refreshToken;

    if (!refreshToken) {
      return throwError(() => new Error('Refresh token non presente'));
    }

    body.set('grant_type', 'refresh_token');
    body.set('client_id', 'angular-client');
    body.set('refresh_token', refreshToken);

    const headers = new HttpHeaders({
      'Content-Type': 'application/x-www-form-urlencoded',
    });

    return this.http.post<any>(KEYCLOAK_TOKEN_URL, body.toString(), { headers }).pipe(
      tap((response) => {
        this.tokenSet = new TokenSet(
          response.access_token,
          response.refresh_token,
          response.id_token
        );
        localStorage.setItem('tokenSet', JSON.stringify(this.tokenSet));
      }),
      catchError((error) => {
        const errorMessage = this.extractKeycloakError(error);
        return throwError(() => new Error(errorMessage));
      })
    );
  }


  handleSessionExpired(): void {
    AuthService.sessionExpired = true; // Imposta la variabile statica
    this.clearSession(); // Cancella i dati della sessione
    this.router.navigate(['/login']); // Reindirizza al login
  }

  // **Controlla autenticazione**
  // isAuthenticated(): boolean {
  //   if (!this.tokenSet || !this.tokenSet.accessToken || !this.tokenSet.refreshToken) {
  //     return false;
  //   }

  //   const decoded: any = jwtDecode(this.tokenSet.refreshToken);
  //   const now = Math.floor(Date.now() / 1000);
  //   return decoded.exp > now; // Controlla la scadenza del token
  // }

  isAuthenticated(): boolean {
    // **1. Controllo locale sui token**
    if (!this.tokenSet || !this.tokenSet.accessToken || !this.tokenSet.refreshToken) {
      return false; // Nessun token disponibile
    }
  
    // Decodifica il refresh token per verificare la scadenza
    const decoded: any = jwtDecode(this.tokenSet.refreshToken);
    const now = Math.floor(Date.now() / 1000);
    if (decoded.exp <= now) {
      return false; // Refresh token scaduto
    }
  
    // **2. Controllo remoto asincrono**
    this.checkSessionValidity(); // Avvia il controllo in background
  
    // **3. Restituisce true temporaneamente basandosi sul controllo locale**
    return true;
  }


  private checkSessionValidity(): void {
    const headers = new HttpHeaders({
      Authorization: `Bearer ${this.tokenSet?.accessToken}`,
    });
  
    this.http
      .get('http://localhost:8180/realms/PhotoDom/protocol/openid-connect/userinfo', { headers })
      .subscribe({
        next: () => {
          console.log('Sessione valida.');
        },
        error: () => {
          console.log('Sessione invalidata lato server.');
          this.handleSessionExpired(); // Gestisce la sessione scaduta
        },
      });
  }
  

  // **Restituisce l'utente corrente**
  getUser(): Observable<User | null> {
    return this.userSubject.asObservable();
  }

  // **Cancella la sessione**
  private clearSession(): void {
    this.tokenSet = null;
    this.userSubject.next(null);
    localStorage.clear();
  }

  private extractKeycloakError(error: any): string {
    let errorMessage = 'Errore Sconosciuto'; // Messaggio generico predefinito
  
    // Controlla se l'errore contiene informazioni dettagliate
    if (error.error) {
      if (error.error.error === 'invalid_grant') {
        errorMessage = 'Username o password errati.'; // Caso specifico per credenziali errate
      } else if (error.error.error_description) {
        errorMessage = error.error.error_description; // Altri messaggi descrittivi
      } else if (error.message) {
        errorMessage = error.message; // Fallback per messaggi generici
      }
    }
    return errorMessage;
  }


  getAccessToken(): string | null {
    return this.tokenSet?.accessToken || null;
  }


  
}



