import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { DetailSaleDetailComponent } from './detail-sale-detail.component';

describe('DetailSale Management Detail Component', () => {
  let comp: DetailSaleDetailComponent;
  let fixture: ComponentFixture<DetailSaleDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [DetailSaleDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ detailSale: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(DetailSaleDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(DetailSaleDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load detailSale on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.detailSale).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
