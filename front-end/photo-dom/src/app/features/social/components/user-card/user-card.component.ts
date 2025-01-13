import { Component, EventEmitter, Input, Output } from '@angular/core';
import { PhotoControllerService } from '../../../../../services/photo-service/services';
import { UserDto } from '../../../../../services/user-service/models';
import { UserDetailsModalComponent } from '../user-details-modal/user-details-modal.component';
import { MatDialog } from '@angular/material/dialog';

@Component({
  selector: 'app-user-card',
  standalone: false,
  
  templateUrl: './user-card.component.html',
  styleUrl: './user-card.component.css'
})
export class UserCardComponent {
  @Input() user!: UserDto;
  @Output() onBan = new EventEmitter<number>();
  @Output() onUnban = new EventEmitter<number>();
  @Output() onPromote = new EventEmitter<number>();

  profileImage: string | null = null; 

  constructor(private photoService: PhotoControllerService,private dialog: MatDialog) {}

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
          this.profileImage = 'assets/default-avatar.png';
        },
      });
    }
  }

  handleBan(): void {
    this.onBan.emit(this.user.id);
  }

  handleUnban(): void {
    this.onUnban.emit(this.user.id);
  }

  handlePromote(): void {
    this.onPromote.emit(this.user.id);
  }
  
  openDetails(): void {
    this.dialog.open(UserDetailsModalComponent, {
      width: '400px',
      data: this.user,
    });
  }

  
}
