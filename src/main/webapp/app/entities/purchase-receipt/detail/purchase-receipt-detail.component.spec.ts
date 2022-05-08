import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { PurchaseReceiptDetailComponent } from './purchase-receipt-detail.component';

describe('PurchaseReceipt Management Detail Component', () => {
  let comp: PurchaseReceiptDetailComponent;
  let fixture: ComponentFixture<PurchaseReceiptDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [PurchaseReceiptDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ purchaseReceipt: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(PurchaseReceiptDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(PurchaseReceiptDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load purchaseReceipt on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.purchaseReceipt).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
