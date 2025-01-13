import { Component, OnInit } from '@angular/core';
import { UserDto } from '../../../../../services/user-service/models';
import { AdminControllerService } from '../../../../../services/user-service/services';

@Component({
  selector: 'app-inactive-users',
  standalone: false,
  
  templateUrl: './inactive-users.component.html',
  styleUrl: './inactive-users.component.css'
})
export class InactiveUsersComponent implements OnInit {
  inactiveUsers: UserDto[] = [];
  isLoading: boolean = false;

  constructor(private adminService: AdminControllerService) {}

  ngOnInit(): void {
    this.loadInactiveUsers();
  }

  private loadInactiveUsers(): void {
    this.isLoading = true;
    this.adminService.getInactiveUsers().subscribe({
      next: (response) => {
        this.inactiveUsers = response.data || [];
        this.isLoading = false;
      },
      error: (err) => {
        console.error('Errore nel caricamento degli utenti inattivi:', err);
        this.isLoading = false;
      },
    });
  }

  handlePromote(userId: number): void {
    this.adminService.addAdmin({ id: userId }).subscribe(() => this.loadInactiveUsers());
  }
}
