import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IRecip, Recip } from '../recip.model';

import { RecipService } from './recip.service';

describe('Recip Service', () => {
  let service: RecipService;
  let httpMock: HttpTestingController;
  let elemDefault: IRecip;
  let expectedResult: IRecip | IRecip[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(RecipService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      nameRecip: 'AAAAAAA',
      estimatedPricePreparation: 0,
      amountProductUsed: 0,
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

    it('should create a Recip', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new Recip()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Recip', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          nameRecip: 'BBBBBB',
          estimatedPricePreparation: 1,
          amountProductUsed: 1,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Recip', () => {
      const patchObject = Object.assign({}, new Recip());

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Recip', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          nameRecip: 'BBBBBB',
          estimatedPricePreparation: 1,
          amountProductUsed: 1,
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

    it('should delete a Recip', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addRecipToCollectionIfMissing', () => {
      it('should add a Recip to an empty array', () => {
        const recip: IRecip = { id: 123 };
        expectedResult = service.addRecipToCollectionIfMissing([], recip);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(recip);
      });

      it('should not add a Recip to an array that contains it', () => {
        const recip: IRecip = { id: 123 };
        const recipCollection: IRecip[] = [
          {
            ...recip,
          },
          { id: 456 },
        ];
        expectedResult = service.addRecipToCollectionIfMissing(recipCollection, recip);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Recip to an array that doesn't contain it", () => {
        const recip: IRecip = { id: 123 };
        const recipCollection: IRecip[] = [{ id: 456 }];
        expectedResult = service.addRecipToCollectionIfMissing(recipCollection, recip);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(recip);
      });

      it('should add only unique Recip to an array', () => {
        const recipArray: IRecip[] = [{ id: 123 }, { id: 456 }, { id: 70630 }];
        const recipCollection: IRecip[] = [{ id: 123 }];
        expectedResult = service.addRecipToCollectionIfMissing(recipCollection, ...recipArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const recip: IRecip = { id: 123 };
        const recip2: IRecip = { id: 456 };
        expectedResult = service.addRecipToCollectionIfMissing([], recip, recip2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(recip);
        expect(expectedResult).toContain(recip2);
      });

      it('should accept null and undefined values', () => {
        const recip: IRecip = { id: 123 };
        expectedResult = service.addRecipToCollectionIfMissing([], null, recip, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(recip);
      });

      it('should return initial array if no Recip is added', () => {
        const recipCollection: IRecip[] = [{ id: 123 }];
        expectedResult = service.addRecipToCollectionIfMissing(recipCollection, undefined, null);
        expect(expectedResult).toEqual(recipCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
