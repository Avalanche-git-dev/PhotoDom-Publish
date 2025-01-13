import { Component, OnInit } from '@angular/core';
import { UserDto } from '../../../../../services/user-service/models';
import { AdminControllerService, UserControllerService } from '../../../../../services/user-service/services';
import { MatSnackBar } from '@angular/material/snack-bar';

@Component({
  selector: 'app-manage-users',
  standalone: false,
  
  templateUrl: './manage-users.component.html',
  styleUrl: './manage-users.component.css'
})
export class ManageUsersComponent implements OnInit {
 
  allUsers: UserDto[] = [];
  filteredUsers: UserDto[] = [];
  isLoading: boolean = false;

  constructor(private userService: UserControllerService ,private adminService: AdminControllerService, private snackBar: MatSnackBar) {}

  ngOnInit(): void {
    this.loadUsers();
  }

  private loadUsers(): void {
    this.isLoading = true;
    this.userService.getAllUsers().subscribe({
      next: (response) => {
        this.allUsers = response.data || [];
        this.filteredUsers = [...this.allUsers];
        this.isLoading = false;
      },
      error: (err) => {
        console.error('Errore nel caricamento degli utenti:', err);
        this.isLoading = false;
      },
    });
  }

  handleSearchResults(users: UserDto[]): void {
    this.filteredUsers = users.length ? users : [...this.allUsers];
  }

  // handleBan(userId: number): void {
  //   this.adminService.banUser({ id: userId }).subscribe(() => this.loadUsers());
  // }

  // handleUnban(userId: number): void {
  //   this.adminService.removeBanUser({ id: userId }).subscribe(() => this.loadUsers());
  // }

  // handlePromote(userId: number): void {
  //   this.adminService.addAdmin({ id: userId }).subscribe(() => this.loadUsers());
  // }


  handleBan(userId: number): void {
    this.adminService.banUser({ id: userId }).subscribe({
      next: (response) => {
        this.loadUsers();
        this.showSnackbar(response.message || 'Utente bannato con successo!', response.success);
      },
      error: (err) => {
        console.error('Errore nel ban dell\'utente:', err);
        this.showSnackbar('Errore durante il ban dell\'utente.', false);
      },
    });
  }

  handleUnban(userId: number): void {
    this.adminService.removeBanUser({ id: userId }).subscribe({
      next: (response) => {
        this.loadUsers();
        this.showSnackbar(response.message || 'Ban rimosso con successo!', response.success);
      },
      error: (err) => {
        console.error('Errore nel rimuovere il ban dell\'utente:', err);
        this.showSnackbar('Errore durante la rimozione del ban.', false);
      },
    });
  }

  handlePromote(userId: number): void {
    this.adminService.addAdmin({ id: userId }).subscribe({
      next: (response) => {
        this.loadUsers();
        this.showSnackbar(response.message || 'Utente promosso ad admin con successo!', response.success);
      },
      error: (err) => {
        console.error('Errore nella promozione dell\'utente:', err);
        this.showSnackbar('Errore durante la promozione dell\'utente.', false);
      },
    });
  }

  private showSnackbar(message: string, success: boolean | undefined): void {
    this.snackBar.open(message, 'Chiudi', {
      duration: 3000,
      panelClass: success ? 'snackbar-success' : 'snackbar-error',
    });
  }

}
