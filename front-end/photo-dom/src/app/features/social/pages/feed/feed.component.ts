import { Component } from '@angular/core';
import { MatSnackBar } from '@angular/material/snack-bar';

@Component({
  selector: 'app-feed',
  standalone: false,
  
  templateUrl: './feed.component.html',
  styleUrl: './feed.component.css'
})
export class FeedComponent {
  nickname: string = '';
  constructor(private snackBar: MatSnackBar) {}

  ngOnInit(): void {
    this.loadUserNickname();
    this.showWelcomeMessageIfNeeded();
  }

  private loadUserNickname(): void {
    const user = JSON.parse(localStorage.getItem('user') || '{}');
    this.nickname = user.username || 'Utente'; // Preleva il nickname o usa "Utente" come predefinito
  }

  private showWelcomeMessageIfNeeded(): void {
    const welcomeShown = localStorage.getItem('welcomeShown');

    if (welcomeShown === 'false') {
      this.snackBar.open(`Benvenuto, ${this.nickname}!`, '', {
        duration: 3000,
        horizontalPosition: 'center',
        verticalPosition: 'top',
        panelClass: ['welcome-snack-bar'],
      });

      // Imposta il flag per non mostrare di nuovo il messaggio
      localStorage.setItem('welcomeShown', 'true');
    }
  }
}
