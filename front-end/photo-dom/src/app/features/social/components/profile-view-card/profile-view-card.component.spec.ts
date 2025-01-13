import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ProfileViewCardComponent } from './profile-view-card.component';

describe('ProfileViewCardComponent', () => {
  let component: ProfileViewCardComponent;
  let fixture: ComponentFixture<ProfileViewCardComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ProfileViewCardComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ProfileViewCardComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
