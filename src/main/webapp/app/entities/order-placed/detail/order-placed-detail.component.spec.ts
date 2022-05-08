import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { OrderPlacedDetailComponent } from './order-placed-detail.component';

describe('OrderPlaced Management Detail Component', () => {
  let comp: OrderPlacedDetailComponent;
  let fixture: ComponentFixture<OrderPlacedDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [OrderPlacedDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ orderPlaced: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(OrderPlacedDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(OrderPlacedDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load orderPlaced on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.orderPlaced).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
