import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PhotoSelectionModalComponent } from './photo-selection-modal.component';

describe('PhotoSelectionModalComponent', () => {
  let component: PhotoSelectionModalComponent;
  let fixture: ComponentFixture<PhotoSelectionModalComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [PhotoSelectionModalComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(PhotoSelectionModalComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
