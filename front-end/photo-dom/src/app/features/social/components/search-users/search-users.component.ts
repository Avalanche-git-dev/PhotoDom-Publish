import { Component, EventEmitter, Output } from '@angular/core';
import { UserDto } from '../../../../../services/user-service/models';
import { UserControllerService } from '../../../../../services/user-service/services';

@Component({
  selector: 'app-search-users',
  standalone: false,
  
  templateUrl: './search-users.component.html',
  styleUrl: './search-users.component.css'
})
export class SearchUsersComponent {
  @Output() onSearchResults = new EventEmitter<UserDto[]>();
  isSearching: boolean = false;
  query: string = '';

  constructor(private userService: UserControllerService) {}

  searchUsers(): void {
    if (!this.query.trim()) {
      this.onSearchResults.emit([]); // Se vuoto, emetti lista vuota
      return;
    }

    this.isSearching = true;

    this.userService.searchUsers({ body: { nickname: this.query } }).subscribe({
      next: (response) => {
        if (response.success && response.data) {
          this.onSearchResults.emit(response.data);
        } else {
          this.onSearchResults.emit([]);
        }
        this.isSearching = false;
      },
      error: () => {
        console.error('Errore nella ricerca utenti.');
        this.isSearching = false;
      },
    });
  }
}
