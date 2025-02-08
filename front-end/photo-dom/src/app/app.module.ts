import { ErrorHandler, NgModule } from '@angular/core';
import { BrowserModule, provideClientHydration, withEventReplay } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { provideAnimationsAsync } from '@angular/platform-browser/animations/async';


// Servizi
import { ApiModule as UserApiModule } from '../services/user-service/api.module';
import { ApiModule as PhotoApiModule } from '../services/photo-service/api.module';
import { ApiModule as CommentApiModule } from '../services/comment-service/api.module';
import { AuthModule } from '../services/auth-service/auth.module';
import { HTTP_INTERCEPTORS, provideHttpClient, withInterceptorsFromDi } from '@angular/common/http';
import { AuthService } from '../services/auth-service/auth.service';
import { AuthGuard } from '../services/auth-guard/auth.guard';
import { AuthInterceptor } from '../services/auth-interceptor/auth.interceptor';
import { GlobalErrorHandler } from '../services/error-handler/global-error-handler';
import { SocialModule } from './features/social/social.module';
import { AdminGuard } from '../services/admin-guard/admin.guard';

@NgModule({
  declarations: [
    AppComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    UserApiModule.forRoot({ rootUrl: 'http://localhost:8080' }),
    PhotoApiModule.forRoot({ rootUrl: 'http://localhost:8080' }),
    CommentApiModule.forRoot({ rootUrl: 'http://localhost:8080' }),
    AuthModule,
    SocialModule
    
  ],
  providers: [
    provideClientHydration(withEventReplay()),
    provideAnimationsAsync(),
    
    provideHttpClient(withInterceptorsFromDi()),
    AuthService,
    AuthGuard,
    AdminGuard,
    { provide: HTTP_INTERCEPTORS, useClass: AuthInterceptor, multi: true },
    { provide: ErrorHandler, useClass: GlobalErrorHandler }
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
