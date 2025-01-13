import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ProfilePhotoCardComponent } from './profile-photo-card.component';

describe('ProfilePhotoCardComponent', () => {
  let component: ProfilePhotoCardComponent;
  let fixture: ComponentFixture<ProfilePhotoCardComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ProfilePhotoCardComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ProfilePhotoCardComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
