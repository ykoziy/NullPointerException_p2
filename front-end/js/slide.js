'use strict';

class Carousel {
  constructor(slideCount, slides, navDots) {
    this.currentSlide = 0;
    this.slideCount = slideCount;
    this.slides = slides;
    this.navDots = navDots;
    this.slideContainer = this.slides[0].parentNode;
    this.initializeCarousel();
    this.initialzieNavDots();
  }

  initializeCarousel() {
    for (let i = 0; i < this.slides.length; i++) {
      this.slides[i].style.transform = `translateX(${100 * i}%)`;
    }
    this.slideContainer.addEventListener('click', (e) => {
      this.next();
    });
  }

  initialzieNavDots() {
    this.navDots.forEach((dot, index) => {
      dot.addEventListener('click', (e) => {
        //using arrow function so that we are using 'this' from this class
        // if we use regular function, 'this' will point to the element that fired the event
        this.moveToSlide(index);
      });
    });
  }

  next() {
    if (this.currentSlide === this.slideCount - 1) {
      this.currentSlide = 0;
    } else {
      this.currentSlide++;
    }
    this.#setActiveDot(this.currentSlide);
    for (let i = 0; i < this.slides.length; i++) {
      this.slides[i].style.transform = `translateX(${
        100 * (i - this.currentSlide)
      }%)`;
    }
  }

  moveToSlide(index) {
    this.#setActiveDot(index);
    for (let i = 0; i < this.slides.length; i++) {
      this.slides[i].style.transform = `translateX(${100 * (i - index)}%)`;
    }
  }

  autoSlide(milliseconds) {
    this.interval = setInterval(() => this.next(), milliseconds);
  }

  #setActiveDot(index) {
    this.navDots.forEach((dot, i) => {
      if (i == index) {
        dot.classList.add('active-dot');
      } else {
        dot.classList.remove('active-dot');
      }
    });
  }
}

// below is the code of how to set up the carousel
// and use it
let slides = document.querySelectorAll('.img-slide');
let navDots = document.querySelectorAll('.carousel-nav .carousel-dot');
const imgGallery = new Carousel(4, slides, navDots);
imgGallery.autoSlide(4000);
