import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { MatToolbarModule } from '@angular/material/toolbar';
import { MatButtonModule } from '@angular/material/button';
import {MatIconModule} from '@angular/material/icon';
import { MatFormFieldModule } from '@angular/material/form-field';
import {MatCardModule} from '@angular/material/card';
import { MatInputModule } from '@angular/material/input';
import {MatSnackBarModule} from '@angular/material/snack-bar';
import {MatProgressSpinnerModule} from '@angular/material/progress-spinner';
import { MatDialogModule } from '@angular/material/dialog';
import {MatPaginatorModule} from '@angular/material/paginator';
import { MatSelectModule } from '@angular/material/select';
import {MatSidenavModule} from '@angular/material/sidenav';
import {MatListModule} from '@angular/material/list';



import { SocialRoutingModule } from './social-routing.module';
import { ProfileComponent } from './pages/profile/profile.component';
import { NavbarComponent } from './components/navbar/navbar.component';
import { PostCardComponent } from './components/post-card/post-card.component';
import { HomeComponent } from './pages/home/home.component';
import { FeedComponent } from './pages/feed/feed.component';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { ProfileCardComponent } from './components/profile-card/profile-card.component';
import { ProfileFormComponent } from './components/profile-form/profile-form.component';
import { PhotoGalleryComponent } from './components/photo-gallery/photo-gallery.component';
import { ChangePasswordFormComponent } from './components/change-password-form/change-password-form.component';
import { UploadPhotoModalComponent } from './components/upload-photo-modal/upload-photo-modal.component';
import { PhotoSelectionModalComponent } from './components/photo-selection-modal/photo-selection-modal.component';
import { ProfilePhotoCardComponent } from './components/profile-photo-card/profile-photo-card.component';
import { CommentCardComponent } from './components/comment-card/comment-card.component';
import { AddCommentComponent } from './components/add-comment/add-comment.component';
import { CommentListComponent } from './components/comment-list/comment-list.component';
import { AppHeaderComponent } from './components/app-header/app-header.component';
import { FeedGalleryComponent } from './components/feed-gallery/feed-gallery.component';
import { ProfileViewCardComponent } from './components/profile-view-card/profile-view-card.component';
import { ProfileViewComponent } from './pages/profile-view/profile-view.component';
import { SearchBarComponent } from './components/search-bar/search-bar.component';
import { ProfileViewGalleryComponent } from './components/profile-view-gallery/profile-view-gallery.component';
import { AdminComponent } from './pages/admin/admin.component';
import { ManageUsersComponent } from './components/manage-users/manage-users.component';
import { BannedUsersComponent } from './components/banned-users/banned-users.component';
import { InactiveUsersComponent } from './components/inactive-users/inactive-users.component';
import { UserCardComponent } from './components/user-card/user-card.component';
import { SearchUsersComponent } from './components/search-users/search-users.component';
import { UserDetailsModalComponent } from './components/user-details-modal/user-details-modal.component';


@NgModule({
  declarations: [
    ProfileComponent,
    NavbarComponent,
    PostCardComponent,
    HomeComponent,
    FeedComponent,
    ProfileCardComponent,
    ProfileFormComponent,
    PhotoGalleryComponent,
    ChangePasswordFormComponent,
    UploadPhotoModalComponent,
    PhotoSelectionModalComponent,
    ProfilePhotoCardComponent,
    CommentCardComponent,
    AddCommentComponent,
    CommentListComponent,
    AppHeaderComponent,
    FeedGalleryComponent,
    ProfileViewCardComponent,
    ProfileViewComponent,
    SearchBarComponent,
    ProfileViewGalleryComponent,
    AdminComponent,
    ManageUsersComponent,
    BannedUsersComponent,
    InactiveUsersComponent,
    UserCardComponent,
    SearchUsersComponent,
    UserDetailsModalComponent
  ],
  imports: [
    CommonModule,
    SocialRoutingModule,
    MatToolbarModule, 
    MatButtonModule, 
    MatIconModule,
    FormsModule, 
    MatFormFieldModule,
    MatInputModule,
    MatCardModule,
    MatSnackBarModule,
    ReactiveFormsModule,
    MatProgressSpinnerModule,
    MatDialogModule,
    MatPaginatorModule,
    MatSelectModule,
    MatSidenavModule,
    MatListModule
    
  ]
})
export class SocialModule { }
