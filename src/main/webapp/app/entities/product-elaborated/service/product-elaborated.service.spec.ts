import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IProductElaborated, ProductElaborated } from '../product-elaborated.model';

import { ProductElaboratedService } from './product-elaborated.service';

describe('ProductElaborated Service', () => {
  let service: ProductElaboratedService;
  let httpMock: HttpTestingController;
  let elemDefault: IProductElaborated;
  let expectedResult: IProductElaborated | IProductElaborated[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(ProductElaboratedService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      amountProduced: 0,
      productName: 'AAAAAAA',
      buyPrice: 0,
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

    it('should create a ProductElaborated', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new ProductElaborated()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a ProductElaborated', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          amountProduced: 1,
          productName: 'BBBBBB',
          buyPrice: 1,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a ProductElaborated', () => {
      const patchObject = Object.assign(
        {
          productName: 'BBBBBB',
          buyPrice: 1,
        },
        new ProductElaborated()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of ProductElaborated', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          amountProduced: 1,
          productName: 'BBBBBB',
          buyPrice: 1,
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

    it('should delete a ProductElaborated', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addProductElaboratedToCollectionIfMissing', () => {
      it('should add a ProductElaborated to an empty array', () => {
        const productElaborated: IProductElaborated = { id: 123 };
        expectedResult = service.addProductElaboratedToCollectionIfMissing([], productElaborated);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(productElaborated);
      });

      it('should not add a ProductElaborated to an array that contains it', () => {
        const productElaborated: IProductElaborated = { id: 123 };
        const productElaboratedCollection: IProductElaborated[] = [
          {
            ...productElaborated,
          },
          { id: 456 },
        ];
        expectedResult = service.addProductElaboratedToCollectionIfMissing(productElaboratedCollection, productElaborated);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a ProductElaborated to an array that doesn't contain it", () => {
        const productElaborated: IProductElaborated = { id: 123 };
        const productElaboratedCollection: IProductElaborated[] = [{ id: 456 }];
        expectedResult = service.addProductElaboratedToCollectionIfMissing(productElaboratedCollection, productElaborated);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(productElaborated);
      });

      it('should add only unique ProductElaborated to an array', () => {
        const productElaboratedArray: IProductElaborated[] = [{ id: 123 }, { id: 456 }, { id: 98427 }];
        const productElaboratedCollection: IProductElaborated[] = [{ id: 123 }];
        expectedResult = service.addProductElaboratedToCollectionIfMissing(productElaboratedCollection, ...productElaboratedArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const productElaborated: IProductElaborated = { id: 123 };
        const productElaborated2: IProductElaborated = { id: 456 };
        expectedResult = service.addProductElaboratedToCollectionIfMissing([], productElaborated, productElaborated2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(productElaborated);
        expect(expectedResult).toContain(productElaborated2);
      });

      it('should accept null and undefined values', () => {
        const productElaborated: IProductElaborated = { id: 123 };
        expectedResult = service.addProductElaboratedToCollectionIfMissing([], null, productElaborated, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(productElaborated);
      });

      it('should return initial array if no ProductElaborated is added', () => {
        const productElaboratedCollection: IProductElaborated[] = [{ id: 123 }];
        expectedResult = service.addProductElaboratedToCollectionIfMissing(productElaboratedCollection, undefined, null);
        expect(expectedResult).toEqual(productElaboratedCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
