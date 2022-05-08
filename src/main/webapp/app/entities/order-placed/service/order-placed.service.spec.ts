import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { StateOrder } from 'app/entities/enumerations/state-order.model';
import { IOrderPlaced, OrderPlaced } from '../order-placed.model';

import { OrderPlacedService } from './order-placed.service';

describe('OrderPlaced Service', () => {
  let service: OrderPlacedService;
  let httpMock: HttpTestingController;
  let elemDefault: IOrderPlaced;
  let expectedResult: IOrderPlaced | IOrderPlaced[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(OrderPlacedService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      orderPlacedState: StateOrder.DELIVERED,
    };
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = Object.assign({}, elemDefault);

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(elemDefault);
    });

    it('should create a OrderPlaced', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new OrderPlaced()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a OrderPlaced', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          orderPlacedState: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a OrderPlaced', () => {
      const patchObject = Object.assign(
        {
          orderPlacedState: 'BBBBBB',
        },
        new OrderPlaced()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of OrderPlaced', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          orderPlacedState: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toContainEqual(expected);
    });

    it('should delete a OrderPlaced', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addOrderPlacedToCollectionIfMissing', () => {
      it('should add a OrderPlaced to an empty array', () => {
        const orderPlaced: IOrderPlaced = { id: 123 };
        expectedResult = service.addOrderPlacedToCollectionIfMissing([], orderPlaced);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(orderPlaced);
      });

      it('should not add a OrderPlaced to an array that contains it', () => {
        const orderPlaced: IOrderPlaced = { id: 123 };
        const orderPlacedCollection: IOrderPlaced[] = [
          {
            ...orderPlaced,
          },
          { id: 456 },
        ];
        expectedResult = service.addOrderPlacedToCollectionIfMissing(orderPlacedCollection, orderPlaced);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a OrderPlaced to an array that doesn't contain it", () => {
        const orderPlaced: IOrderPlaced = { id: 123 };
        const orderPlacedCollection: IOrderPlaced[] = [{ id: 456 }];
        expectedResult = service.addOrderPlacedToCollectionIfMissing(orderPlacedCollection, orderPlaced);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(orderPlaced);
      });

      it('should add only unique OrderPlaced to an array', () => {
        const orderPlacedArray: IOrderPlaced[] = [{ id: 123 }, { id: 456 }, { id: 21143 }];
        const orderPlacedCollection: IOrderPlaced[] = [{ id: 123 }];
        expectedResult = service.addOrderPlacedToCollectionIfMissing(orderPlacedCollection, ...orderPlacedArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const orderPlaced: IOrderPlaced = { id: 123 };
        const orderPlaced2: IOrderPlaced = { id: 456 };
        expectedResult = service.addOrderPlacedToCollectionIfMissing([], orderPlaced, orderPlaced2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(orderPlaced);
        expect(expectedResult).toContain(orderPlaced2);
      });

      it('should accept null and undefined values', () => {
        const orderPlaced: IOrderPlaced = { id: 123 };
        expectedResult = service.addOrderPlacedToCollectionIfMissing([], null, orderPlaced, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(orderPlaced);
      });

      it('should return initial array if no OrderPlaced is added', () => {
        const orderPlacedCollection: IOrderPlaced[] = [{ id: 123 }];
        expectedResult = service.addOrderPlacedToCollectionIfMissing(orderPlacedCollection, undefined, null);
        expect(expectedResult).toEqual(orderPlacedCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
