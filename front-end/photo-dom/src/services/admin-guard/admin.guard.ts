import { Injectable } from '@angular/core';
import { CanActivate, Router } from '@angular/router';

@Injectable({
  providedIn: 'root',
})
export class AdminGuard implements CanActivate {
  constructor(private router: Router) {}

  canActivate(): boolean {
    const user = JSON.parse(localStorage.getItem('user') || '{}');
    if (user.role === 'ADMIN') {
      return true;
    }
    this.router.navigate(['/social']); // Reindirizza alla home se non è admin
    return false;
  }
}
