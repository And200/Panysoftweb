import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { ProductElaboratedDetailComponent } from './product-elaborated-detail.component';

describe('ProductElaborated Management Detail Component', () => {
  let comp: ProductElaboratedDetailComponent;
  let fixture: ComponentFixture<ProductElaboratedDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [ProductElaboratedDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ productElaborated: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(ProductElaboratedDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(ProductElaboratedDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load productElaborated on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.productElaborated).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
