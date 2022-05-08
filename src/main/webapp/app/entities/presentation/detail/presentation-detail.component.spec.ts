import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { PresentationDetailComponent } from './presentation-detail.component';

describe('Presentation Management Detail Component', () => {
  let comp: PresentationDetailComponent;
  let fixture: ComponentFixture<PresentationDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [PresentationDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ presentation: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(PresentationDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(PresentationDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load presentation on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.presentation).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
