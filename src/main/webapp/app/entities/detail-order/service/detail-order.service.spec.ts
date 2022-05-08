import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import dayjs from 'dayjs/esm';

import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IDetailOrder, DetailOrder } from '../detail-order.model';

import { DetailOrderService } from './detail-order.service';

describe('DetailOrder Service', () => {
  let service: DetailOrderService;
  let httpMock: HttpTestingController;
  let elemDefault: IDetailOrder;
  let expectedResult: IDetailOrder | IDetailOrder[] | boolean | null;
  let currentDate: dayjs.Dayjs;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(DetailOrderService);
    httpMock = TestBed.inject(HttpTestingController);
    currentDate = dayjs();

    elemDefault = {
      id: 0,
      quantityOrdered: 0,
      date: currentDate,
      invoiceNumber: 'AAAAAAA',
      productOrdered: 'AAAAAAA',
      pricePayment: 0,
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

    it('should create a DetailOrder', () => {
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

      service.create(new DetailOrder()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a DetailOrder', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          quantityOrdered: 1,
          date: currentDate.format(DATE_TIME_FORMAT),
          invoiceNumber: 'BBBBBB',
          productOrdered: 'BBBBBB',
          pricePayment: 1,
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

    it('should partial update a DetailOrder', () => {
      const patchObject = Object.assign(
        {
          quantityOrdered: 1,
          date: currentDate.format(DATE_TIME_FORMAT),
          invoiceNumber: 'BBBBBB',
        },
        new DetailOrder()
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

    it('should return a list of DetailOrder', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          quantityOrdered: 1,
          date: currentDate.format(DATE_TIME_FORMAT),
          invoiceNumber: 'BBBBBB',
          productOrdered: 'BBBBBB',
          pricePayment: 1,
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

    it('should delete a DetailOrder', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addDetailOrderToCollectionIfMissing', () => {
      it('should add a DetailOrder to an empty array', () => {
        const detailOrder: IDetailOrder = { id: 123 };
        expectedResult = service.addDetailOrderToCollectionIfMissing([], detailOrder);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(detailOrder);
      });

      it('should not add a DetailOrder to an array that contains it', () => {
        const detailOrder: IDetailOrder = { id: 123 };
        const detailOrderCollection: IDetailOrder[] = [
          {
            ...detailOrder,
          },
          { id: 456 },
        ];
        expectedResult = service.addDetailOrderToCollectionIfMissing(detailOrderCollection, detailOrder);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a DetailOrder to an array that doesn't contain it", () => {
        const detailOrder: IDetailOrder = { id: 123 };
        const detailOrderCollection: IDetailOrder[] = [{ id: 456 }];
        expectedResult = service.addDetailOrderToCollectionIfMissing(detailOrderCollection, detailOrder);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(detailOrder);
      });

      it('should add only unique DetailOrder to an array', () => {
        const detailOrderArray: IDetailOrder[] = [{ id: 123 }, { id: 456 }, { id: 49925 }];
        const detailOrderCollection: IDetailOrder[] = [{ id: 123 }];
        expectedResult = service.addDetailOrderToCollectionIfMissing(detailOrderCollection, ...detailOrderArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const detailOrder: IDetailOrder = { id: 123 };
        const detailOrder2: IDetailOrder = { id: 456 };
        expectedResult = service.addDetailOrderToCollectionIfMissing([], detailOrder, detailOrder2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(detailOrder);
        expect(expectedResult).toContain(detailOrder2);
      });

      it('should accept null and undefined values', () => {
        const detailOrder: IDetailOrder = { id: 123 };
        expectedResult = service.addDetailOrderToCollectionIfMissing([], null, detailOrder, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(detailOrder);
      });

      it('should return initial array if no DetailOrder is added', () => {
        const detailOrderCollection: IDetailOrder[] = [{ id: 123 }];
        expectedResult = service.addDetailOrderToCollectionIfMissing(detailOrderCollection, undefined, null);
        expect(expectedResult).toEqual(detailOrderCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
