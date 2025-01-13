import { Component, OnInit } from '@angular/core';
import { UserDto } from '../../../../../services/user-service/models';
import { AdminControllerService } from '../../../../../services/user-service/services';

@Component({
  selector: 'app-banned-users',
  standalone: false,
  
  templateUrl: './banned-users.component.html',
  styleUrl: './banned-users.component.css'
})
export class BannedUsersComponent implements OnInit {
  bannedUsers: UserDto[] = [];
  isLoading: boolean = false;

  constructor(private adminService: AdminControllerService) {}

  ngOnInit(): void {
    this.loadBannedUsers();
  }

  private loadBannedUsers(): void {
    this.isLoading = true;
    this.adminService.getBannedUsers().subscribe({
      next: (response) => {
        this.bannedUsers = response.data || [];
        this.isLoading = false;
      },
      error: (err) => {
        console.error('Errore nel caricamento degli utenti bannati:', err);
        this.isLoading = false;
      },
    });
  }

  handleUnban(userId: number): void {
    this.adminService.removeBanUser({ id: userId }).subscribe(() => this.loadBannedUsers());
  }
}
