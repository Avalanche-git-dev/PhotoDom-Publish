import { Component } from '@angular/core';


@Component({
  selector: 'app-login-carousel',
  standalone: false,
  
  templateUrl: './login-carousel.component.html',
  styleUrl: './login-carousel.component.css'
})
export class LoginCarouselComponent {
 
  

  slides = [
    { id: 1, image: 'assets/images/photo-dom.webp' },
    { id: 2, image: 'assets/images/what-is-photodom.webp' },
    { id: 3, image: 'assets/images/secure-fast.webp' }
  ];

  


}
