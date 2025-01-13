import { Component, EventEmitter, Output } from '@angular/core';
import { ProfileView, UserFilter } from '../../../../../services/user-service/models';
import { UserControllerService } from '../../../../../services/user-service/services';
import { Router } from '@angular/router';
import { PhotoControllerService } from '../../../../../services/photo-service/services';

@Component({
  selector: 'app-search-bar',
  standalone: false,
  
  templateUrl: './search-bar.component.html',
  styleUrl: './search-bar.component.css'
})
export class SearchBarComponent {
  searchResults: ProfileViewWithImage[] = [];
  isSearching: boolean = false;
  hasSearched: boolean = false;
  selectedFilter: keyof ProfileView = 'nickname';

  constructor(
    private userService: UserControllerService,
    private photoService: PhotoControllerService,
    private router: Router
  ) {}

  onSearch(event: Event): void {
    const input = event.target as HTMLInputElement;
    const query = input.value.trim();

    if (query) {
      this.isSearching = true;
      this.hasSearched = true;

      const filter: Partial<ProfileView> = { [this.selectedFilter]: query };

      this.userService.searchUsers({ body: filter }).subscribe({
        next: (response) => {
          if (response.success && response.data) {
            this.searchResults = response.data.map((profile) => ({
              ...profile,
              profileImage: null,
            }));
            this.searchResults.forEach((profile) => this.loadProfilePhoto(profile));
          } else {
            this.searchResults = [];
          }
          this.isSearching = false;
        },
        error: (err) => {
          console.error('Errore nella ricerca:', err);
          this.searchResults = [];
          this.isSearching = false;
        },
      });
    } else {
      this.searchResults = [];
      this.hasSearched = false;
    }
  }



 
  

  private loadProfilePhoto(profile: ProfileViewWithImage): void {
    if (profile.photoProfileId) {
      this.photoService.getPhotoById({ photoId: profile.photoProfileId }).subscribe({
        next: (response) => {
          if (response.success && response.data) {
            profile.profileImage = `data:${response.data.contentType};base64,${response.data.imageBase64}`;
          } else {
            profile.profileImage = null; // Immagine predefinita
          }
        },
        error: () => {
          profile.profileImage = null; // Immagine predefinita
        },
      });
    } else {
      profile.profileImage = null; // Immagine predefinita
    }
  }
  



  viewProfile(userId: number | undefined): void {
    if (userId !== undefined) {
      this.searchResults = []; // Nasconde i risultati
      this.hasSearched = false;
      this.isSearching = false;
  
      // Naviga al profilo
      this.router.navigate(['/social/profile-view', userId]).then(() => {
        // Dopo la navigazione, resetta il campo di input
        const inputElement = document.querySelector('.search-input') as HTMLInputElement;
        if (inputElement) {
          inputElement.value = '';
          inputElement.blur(); // Rimuove il focus dal campo
        }
      });
    }
  }
  

}

interface ProfileViewWithImage extends ProfileView {
  profileImage: string | null;
}