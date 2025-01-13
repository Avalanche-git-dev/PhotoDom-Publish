import { Component, Inject, OnDestroy, OnInit } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { UserDto } from '../../../../../services/user-service/models';
import { PhotoControllerService } from '../../../../../services/photo-service/services';
import { NavigationStart, Router } from '@angular/router';
import { Subscription } from 'rxjs';

@Component({
  selector: 'app-user-details-modal',
  standalone: false,
  
  templateUrl: './user-details-modal.component.html',
  styleUrl: './user-details-modal.component.css'
})
export class UserDetailsModalComponent implements OnInit, OnDestroy {




  profileImage: string | null = null; // Immagine di default
  private routeSubscription!: Subscription;

  constructor(
    @Inject(MAT_DIALOG_DATA) public user: UserDto,
    private dialogRef: MatDialogRef<UserDetailsModalComponent>,
    private router: Router,
    private photoService: PhotoControllerService
  ) {
    // Ascolta i cambiamenti di rotta
    this.routeSubscription = this.router.events.subscribe((event) => {
      if (event instanceof NavigationStart) {
        this.dialogRef.close(); // Chiudi il modale
      }
    });
  }

  ngOnDestroy(): void {
    this.routeSubscription.unsubscribe(); // Disiscrive dall'osservabile
  }

  ngOnInit(): void {
    this.loadProfilePhoto();
  }

  private loadProfilePhoto(): void {
    if (this.user.photoProfileId) {
      this.photoService.getPhotoById({ photoId: this.user.photoProfileId! }).subscribe({
        next: (response) => {
          if (response.success && response.data) {
            this.profileImage = `data:${response.data.contentType};base64,${response.data.imageBase64}`;
          }
        },
        error: () => {
          this.profileImage = null;
        },
      });
    }
  }

  viewProfile(): void {
    this.router.navigate([`/social/profile-view/${this.user.id}`]);
  }

}
