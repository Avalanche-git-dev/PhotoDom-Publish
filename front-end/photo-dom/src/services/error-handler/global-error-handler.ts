import { ErrorHandler, Injectable, NgZone } from '@angular/core';
import { Router } from '@angular/router';

@Injectable()
export class GlobalErrorHandler implements ErrorHandler {

  constructor(private router: Router, private zone: NgZone) {}

  handleError(error: any): void {
    console.error('Errore globale intercettato:', error);

    if (error.status === 401 || error.status === 403) {
      this.zone.run(() => {
        this.router.navigate(['/login'], { queryParams: { sessionExpired: true } });
      });
    } else {
      alert('Si è verificato un errore. Riprova più tardi.');
    }
  }
}
