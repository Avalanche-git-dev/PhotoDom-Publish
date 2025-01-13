import { Component, Input, OnChanges, OnDestroy, OnInit, SimpleChanges } from '@angular/core';
import { Subscription } from 'rxjs';
import { PhotoDto } from '../../../../../services/photo-service/models';
import { PhotoControllerService } from '../../../../../services/photo-service/services';

@Component({
  selector: 'app-profile-view-gallery',
  standalone: false,
  
  templateUrl: './profile-view-gallery.component.html',
  styleUrl: './profile-view-gallery.component.css'
})
export class ProfileViewGalleryComponent implements OnInit, OnDestroy, OnChanges {
  @Input() userId!: number; // ID dell'utente per cui caricare le foto
  posts: {
    id: number;
    photoUrl: string;
    ownerId: number;
    likeCount: number;
    commentCount: number;
    liked: boolean;
  }[] = [];

  currentPage: number = 0;
  pageSize: number = 15;
  isLoading: boolean = false;

  private subscriptions: Subscription[] = [];

  constructor(private photoService: PhotoControllerService) {}

  ngOnInit(): void {
    this.loadPhotos(this.currentPage);
  }

  ngOnDestroy(): void {
    this.subscriptions.forEach((sub) => sub.unsubscribe());
  }

   ngOnChanges(changes: SimpleChanges): void {
      if (changes['userId'] && changes['userId'].currentValue) {
        this.loadPhotos(this.currentPage); // Ricarica il profilo ogni volta che cambia l'userId
      }
    }
  

  private loadPhotos(page: number): void {
    if (this.isLoading || !this.userId) return;

    this.isLoading = true;
    this.photoService
      .getPhotosByUser({ userId: this.userId, page, size: this.pageSize })
      .subscribe({
        next: (photos: PhotoDto[]) => {
          this.posts = photos.map((photo) => ({
            id: photo.id!,
            photoUrl: `data:${photo.contentType};base64,${photo.imageBase64}`,
            ownerId: photo.userId!,
            likeCount: photo.likeCount || 0,
            commentCount: 0,
            liked: false,
          }));
          this.isLoading = false;
        },
        error: (err) => {
          console.error('Errore nel caricamento delle foto:', err);
          this.isLoading = false;
        },
      });
  }

  handleLikeToggle(post: { id: number; liked: boolean; likeCount: number }): void {
    const serviceCall = post.liked
      ? this.photoService.removeLike({ photoId: post.id })
      : this.photoService.addLike({ photoId: post.id });

    const sub = serviceCall.subscribe({
      next: () => {
        post.liked = !post.liked;
        post.likeCount += post.liked ? 1 : -1;
      },
      error: (err) => console.error('Errore nel toggle del like:', err),
    });

    this.subscriptions.push(sub);
  }

  handlePreviousPage(): void {
    if (this.currentPage > 0) {
      this.currentPage--;
      this.loadPhotos(this.currentPage);
    }
  }

  handleNextPage(): void {
    this.currentPage++;
    this.loadPhotos(this.currentPage);
  }


  removePost(photoId: number): void {
    this.posts = this.posts.filter((post) => post.id !== photoId);
  }
}