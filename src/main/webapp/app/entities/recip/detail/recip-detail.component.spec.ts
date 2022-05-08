import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { RecipDetailComponent } from './recip-detail.component';

describe('Recip Management Detail Component', () => {
  let comp: RecipDetailComponent;
  let fixture: ComponentFixture<RecipDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [RecipDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ recip: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(RecipDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(RecipDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load recip on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.recip).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
