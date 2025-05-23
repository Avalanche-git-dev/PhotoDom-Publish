import { Component, OnInit, OnDestroy } from '@angular/core';
import { Subscription } from 'rxjs';
import { PhotoDto } from '../../../../../services/photo-service/models';
import { PhotoControllerService } from '../../../../../services/photo-service/services';
import { LikeStatusWebSocketService } from '../../../../socket-service/like-status-websocket.service';
import { PhotoWebSocketService } from '../../../../socket-service/photo-websocket-service';

@Component({
  selector: 'app-feed-gallery',
  templateUrl: './feed-gallery.component.html',
  styleUrls: ['./feed-gallery.component.css'],
  standalone: false,
})
export class FeedGalleryComponent implements OnInit, OnDestroy {
  posts: {
    id: number;
    photoUrl: string;
    ownerId: number;
    likeCount: number;
    commentCount: number;
    liked: boolean;
  }[] = [];

  currentPage: number = 0;
  pageSize: number = 15; // Numero di foto per pagina
  isLoading: boolean = false;

  private likeStatusSubscription!: Subscription;
  private photoNotificationSubscription!: Subscription;

  constructor(
    private photoService: PhotoControllerService,
   
    private photoWebSocketService: PhotoWebSocketService
  ) {}

  ngOnInit(): void {
    this.connectWebSockets();
    this.loadPhotos(this.currentPage);
  }

  ngOnDestroy(): void {
    this.likeStatusSubscription?.unsubscribe();
    // this.likeStatusWebSocketService.disconnect();
    this.photoNotificationSubscription?.unsubscribe();
    this.photoWebSocketService.disconnect();
  }

  private connectWebSockets(): void {
    // this.likeStatusWebSocketService.connect();
    this.photoWebSocketService.connect();

    // this.likeStatusSubscription = this.likeStatusWebSocketService
    //   .getLikeStatusUpdates()
    //   .subscribe({
    //     next: (update) => this.updateLikeStatus(update),
    //     error: (err) => console.error('WebSocket LikeStatus error:', err),
    //   });

    this.photoNotificationSubscription = this.photoWebSocketService
      .getNotifications()
      .subscribe({
        next: (photoId: string) => this.handleNewPhotoNotification(photoId),
        error: (err) => console.error('WebSocket Photo error:', err),
      });
  }

  private loadPhotos(page: number): void {
    if (this.isLoading) return;

    this.isLoading = true;
    this.posts = [];

    this.photoService.getPhotosBatch({ page, size: this.pageSize }).subscribe({
      next: (photos: PhotoDto[]) => {
        const newPosts = photos.map((photo) => ({
          id: photo.id!,
          photoUrl: `data:${photo.contentType};base64,${photo.imageBase64}`,
          ownerId: photo.userId!,
          likeCount: photo.likeCount || 0,
          commentCount: 0,
          liked: false,
        }));

        this.posts = newPosts;
        this.posts.forEach((post) => this.loadLikeStatus(post));
        this.isLoading = false;
      },
      error: (err) => {
        console.error('Error loading feed photos:', err);
        this.isLoading = false;
      },
    });
  }

  private loadLikeStatus(post: { id: number; liked: boolean }): void {
    this.photoService.getLikeStatus({ photoId: post.id }).subscribe({
      next: (response) => {
        if (response.success) {
          post.liked = true; // Like esiste
        } else {
          post.liked = false; // Like non esiste
          console.log(`Like non trovato per photoId=${post.id}: ${response.message}`);
        }
      },
      error: (err) => {
        console.error(`Errore nel caricamento dello stato del like per photoId=${post.id}:`, err);
      },
    });
  }

  private updateLikeStatus(update: { photoId: number; liked: boolean }): void {
    const post = this.posts.find((p) => p.id === update.photoId);
    if (post) {
      post.liked = update.liked;
      post.likeCount += update.liked ? 1 : -1;
    }
  }

  private handleNewPhotoNotification(photoId: string): void {
    const parsedPhotoId = parseInt(photoId, 10);
    if (isNaN(parsedPhotoId)) {
      console.error(`ID foto non valido: ${photoId}`);
      return;
    }

    if (this.posts.some((post) => post.id === parsedPhotoId)) {
      console.log(`Foto con ID ${parsedPhotoId} già presente nella galleria.`);
      return;
    }

    this.photoService.getPhotoById({ photoId: parsedPhotoId }).subscribe({
      next: (photo) => {
        if (!photo.data) {
          console.error(`Foto con ID ${parsedPhotoId} non trovata.`);
          return;
        }

        const newPost = {
          id: photo.data.id!,
          photoUrl: `data:${photo.data.contentType};base64,${photo.data.imageBase64}`,
          ownerId: photo.data.userId!,
          likeCount: photo.data.likeCount || 0,
          commentCount: 0,
          liked: false,
        };

        this.posts = [newPost, ...this.posts];
      },
      error: (err) => {
        console.error(`Errore nel caricamento della foto con ID ${parsedPhotoId}:`, err);
      },
    });
  }

  handleLikeToggle(post: { id: number; liked: boolean; likeCount: number }): void {
    const serviceCall = post.liked
      ? this.photoService.removeLike({ photoId: post.id })
      : this.photoService.addLike({ photoId: post.id });

    serviceCall.subscribe({
      next: () => {
        post.liked = !post.liked;
        post.likeCount += post.liked ? 1 : -1;
      },
      error: (err) => console.error('Errore nel toggle del like:', err),
    });
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
