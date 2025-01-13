import { Component } from '@angular/core';


@Component({
  selector: 'app-login-carousel',
  standalone: false,
  
  templateUrl: './login-carousel.component.html',
  styleUrl: './login-carousel.component.css'
})
export class LoginCarouselComponent {
  currentSlide = 0;

  // Array delle immagini
  slides = [
    { id: 1, image: 'assets/images/photo-dom.webp' },
    { id: 2, image: 'assets/images/what-is-photodom.webp' },
    { id: 3, image: 'assets/images/secure-fast.webp' }
  ];

  // Navigazione alle slide
  nextSlide(): void {
    this.currentSlide = (this.currentSlide + 1) % this.slides.length;
  }

  prevSlide(): void {
    this.currentSlide = (this.currentSlide - 1 + this.slides.length) % this.slides.length;
  }
  


}
