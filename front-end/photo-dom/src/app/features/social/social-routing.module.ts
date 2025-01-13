import { Component, NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { HomeComponent } from './pages/home/home.component';
import { AuthGuard } from '../../../services/auth-guard/auth.guard';
import { FeedComponent } from './pages/feed/feed.component';
import { ProfileComponent } from './pages/profile/profile.component';
import { ProfileViewComponent } from './pages/profile-view/profile-view.component';
import { AdminGuard } from '../../../services/admin-guard/admin.guard';
import { AdminComponent } from './pages/admin/admin.component';
import { BannedUsersComponent } from './components/banned-users/banned-users.component';
import { InactiveUsersComponent } from './components/inactive-users/inactive-users.component';
import { ManageUsersComponent } from './components/manage-users/manage-users.component';


const routes: Routes = [
  { path: '', component: HomeComponent, canActivate: [AuthGuard] ,
    canActivateChild: [AuthGuard],
    children :[
     { path: '', component: FeedComponent },
     { path: 'profile', component: ProfileComponent },
     { path: 'profile-view/:id', component: ProfileViewComponent },
    //  { path: 'admin-dashboard', component: AdminComponent, canActivate: [AdminGuard] },
    {
      path: 'admin-dashboard',
      component: AdminComponent,
      canActivate: [AdminGuard],
      children: [
        { path: 'manage-users', component: ManageUsersComponent },
        { path: 'banned-users', component: BannedUsersComponent },
        { path: 'inactive-users', component: InactiveUsersComponent },
        { path: '', redirectTo: 'manage-users', pathMatch: 'full' },
      ],
    },
  
    ]
  },
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class SocialRoutingModule { }
