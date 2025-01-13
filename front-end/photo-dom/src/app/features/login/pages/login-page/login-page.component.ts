import { Component } from '@angular/core';

@Component({
  selector: 'app-login-page',
  standalone: false,
  
  templateUrl: './login-page.component.html',
  styleUrl: './login-page.component.css'
})
export class LoginPageComponent {


  showRegisterForm = false;

  toggleForm(): void {
    this.showRegisterForm = !this.showRegisterForm;
  }



}
