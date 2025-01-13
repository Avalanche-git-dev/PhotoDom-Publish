import { ComponentFixture, TestBed } from '@angular/core/testing';

import { FeedGalleryComponent } from './feed-gallery.component';

describe('FeedGalleryComponent', () => {
  let component: FeedGalleryComponent;
  let fixture: ComponentFixture<FeedGalleryComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [FeedGalleryComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(FeedGalleryComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
