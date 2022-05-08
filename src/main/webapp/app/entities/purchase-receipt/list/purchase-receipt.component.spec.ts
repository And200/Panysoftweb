import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { PurchaseReceiptService } from '../service/purchase-receipt.service';

import { PurchaseReceiptComponent } from './purchase-receipt.component';

describe('PurchaseReceipt Management Component', () => {
  let comp: PurchaseReceiptComponent;
  let fixture: ComponentFixture<PurchaseReceiptComponent>;
  let service: PurchaseReceiptService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [PurchaseReceiptComponent],
    })
      .overrideTemplate(PurchaseReceiptComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(PurchaseReceiptComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(PurchaseReceiptService);

    const headers = new HttpHeaders();
    jest.spyOn(service, 'query').mockReturnValue(
      of(
        new HttpResponse({
          body: [{ id: 123 }],
          headers,
        })
      )
    );
  });

  it('Should call load all on init', () => {
    // WHEN
    comp.ngOnInit();

    // THEN
    expect(service.query).toHaveBeenCalled();
    expect(comp.purchaseReceipts?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });
});
