import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ProfileViewGalleryComponent } from './profile-view-gallery.component';

describe('ProfileViewGalleryComponent', () => {
  let component: ProfileViewGalleryComponent;
  let fixture: ComponentFixture<ProfileViewGalleryComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ProfileViewGalleryComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ProfileViewGalleryComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
