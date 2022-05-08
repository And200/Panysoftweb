import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import dayjs from 'dayjs/esm';

import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IPurchaseReceipt, PurchaseReceipt } from '../purchase-receipt.model';

import { PurchaseReceiptService } from './purchase-receipt.service';

describe('PurchaseReceipt Service', () => {
  let service: PurchaseReceiptService;
  let httpMock: HttpTestingController;
  let elemDefault: IPurchaseReceipt;
  let expectedResult: IPurchaseReceipt | IPurchaseReceipt[] | boolean | null;
  let currentDate: dayjs.Dayjs;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(PurchaseReceiptService);
    httpMock = TestBed.inject(HttpTestingController);
    currentDate = dayjs();

    elemDefault = {
      id: 0,
      date: currentDate,
      totalSale: 0,
    };
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = Object.assign(
        {
          date: currentDate.format(DATE_TIME_FORMAT),
        },
        elemDefault
      );

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(elemDefault);
    });

    it('should create a PurchaseReceipt', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
          date: currentDate.format(DATE_TIME_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          date: currentDate,
        },
        returnedFromService
      );

      service.create(new PurchaseReceipt()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a PurchaseReceipt', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          date: currentDate.format(DATE_TIME_FORMAT),
          totalSale: 1,
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          date: currentDate,
        },
        returnedFromService
      );

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a PurchaseReceipt', () => {
      const patchObject = Object.assign(
        {
          totalSale: 1,
        },
        new PurchaseReceipt()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign(
        {
          date: currentDate,
        },
        returnedFromService
      );

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of PurchaseReceipt', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          date: currentDate.format(DATE_TIME_FORMAT),
          totalSale: 1,
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          date: currentDate,
        },
        returnedFromService
      );

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toContainEqual(expected);
    });

    it('should delete a PurchaseReceipt', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addPurchaseReceiptToCollectionIfMissing', () => {
      it('should add a PurchaseReceipt to an empty array', () => {
        const purchaseReceipt: IPurchaseReceipt = { id: 123 };
        expectedResult = service.addPurchaseReceiptToCollectionIfMissing([], purchaseReceipt);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(purchaseReceipt);
      });

      it('should not add a PurchaseReceipt to an array that contains it', () => {
        const purchaseReceipt: IPurchaseReceipt = { id: 123 };
        const purchaseReceiptCollection: IPurchaseReceipt[] = [
          {
            ...purchaseReceipt,
          },
          { id: 456 },
        ];
        expectedResult = service.addPurchaseReceiptToCollectionIfMissing(purchaseReceiptCollection, purchaseReceipt);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a PurchaseReceipt to an array that doesn't contain it", () => {
        const purchaseReceipt: IPurchaseReceipt = { id: 123 };
        const purchaseReceiptCollection: IPurchaseReceipt[] = [{ id: 456 }];
        expectedResult = service.addPurchaseReceiptToCollectionIfMissing(purchaseReceiptCollection, purchaseReceipt);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(purchaseReceipt);
      });

      it('should add only unique PurchaseReceipt to an array', () => {
        const purchaseReceiptArray: IPurchaseReceipt[] = [{ id: 123 }, { id: 456 }, { id: 43592 }];
        const purchaseReceiptCollection: IPurchaseReceipt[] = [{ id: 123 }];
        expectedResult = service.addPurchaseReceiptToCollectionIfMissing(purchaseReceiptCollection, ...purchaseReceiptArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const purchaseReceipt: IPurchaseReceipt = { id: 123 };
        const purchaseReceipt2: IPurchaseReceipt = { id: 456 };
        expectedResult = service.addPurchaseReceiptToCollectionIfMissing([], purchaseReceipt, purchaseReceipt2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(purchaseReceipt);
        expect(expectedResult).toContain(purchaseReceipt2);
      });

      it('should accept null and undefined values', () => {
        const purchaseReceipt: IPurchaseReceipt = { id: 123 };
        expectedResult = service.addPurchaseReceiptToCollectionIfMissing([], null, purchaseReceipt, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(purchaseReceipt);
      });

      it('should return initial array if no PurchaseReceipt is added', () => {
        const purchaseReceiptCollection: IPurchaseReceipt[] = [{ id: 123 }];
        expectedResult = service.addPurchaseReceiptToCollectionIfMissing(purchaseReceiptCollection, undefined, null);
        expect(expectedResult).toEqual(purchaseReceiptCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
