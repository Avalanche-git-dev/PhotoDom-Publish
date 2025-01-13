import { Injectable } from '@angular/core';
import { CanActivate, CanActivateChild, Router } from '@angular/router';
import { AuthService } from '../auth-service/auth.service';










@Injectable({
  providedIn: 'root'
})
export class AuthGuard implements CanActivate, CanActivateChild {

  constructor(private authService: AuthService, private router: Router) {}

  // Protegge la rotta padre
  canActivate(): boolean {
    return this.checkAuthentication();
  }

  // Protegge tutte le rotte figlie
  canActivateChild(): boolean {
    return this.checkAuthentication();
  }

  // Controllo dell'autenticazione
  private checkAuthentication(): boolean {
    if (this.authService.isAuthenticated()) {
      return true; // Accesso consentito
    } else {
      AuthService.sessionExpired = true;
      this.router.navigate(['/login']); // Reindirizza al login
      return false;
    }
  }
}
