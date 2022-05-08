import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { DetailOrderDetailComponent } from './detail-order-detail.component';

describe('DetailOrder Management Detail Component', () => {
  let comp: DetailOrderDetailComponent;
  let fixture: ComponentFixture<DetailOrderDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [DetailOrderDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ detailOrder: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(DetailOrderDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(DetailOrderDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load detailOrder on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.detailOrder).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
