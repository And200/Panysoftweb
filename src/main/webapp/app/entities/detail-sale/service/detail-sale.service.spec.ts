import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IDetailSale, DetailSale } from '../detail-sale.model';

import { DetailSaleService } from './detail-sale.service';

describe('DetailSale Service', () => {
  let service: DetailSaleService;
  let httpMock: HttpTestingController;
  let elemDefault: IDetailSale;
  let expectedResult: IDetailSale | IDetailSale[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(DetailSaleService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      productAmount: 0,
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

    it('should create a DetailSale', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new DetailSale()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a DetailSale', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          productAmount: 1,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a DetailSale', () => {
      const patchObject = Object.assign(
        {
          productAmount: 1,
        },
        new DetailSale()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of DetailSale', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          productAmount: 1,
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

    it('should delete a DetailSale', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addDetailSaleToCollectionIfMissing', () => {
      it('should add a DetailSale to an empty array', () => {
        const detailSale: IDetailSale = { id: 123 };
        expectedResult = service.addDetailSaleToCollectionIfMissing([], detailSale);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(detailSale);
      });

      it('should not add a DetailSale to an array that contains it', () => {
        const detailSale: IDetailSale = { id: 123 };
        const detailSaleCollection: IDetailSale[] = [
          {
            ...detailSale,
          },
          { id: 456 },
        ];
        expectedResult = service.addDetailSaleToCollectionIfMissing(detailSaleCollection, detailSale);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a DetailSale to an array that doesn't contain it", () => {
        const detailSale: IDetailSale = { id: 123 };
        const detailSaleCollection: IDetailSale[] = [{ id: 456 }];
        expectedResult = service.addDetailSaleToCollectionIfMissing(detailSaleCollection, detailSale);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(detailSale);
      });

      it('should add only unique DetailSale to an array', () => {
        const detailSaleArray: IDetailSale[] = [{ id: 123 }, { id: 456 }, { id: 66124 }];
        const detailSaleCollection: IDetailSale[] = [{ id: 123 }];
        expectedResult = service.addDetailSaleToCollectionIfMissing(detailSaleCollection, ...detailSaleArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const detailSale: IDetailSale = { id: 123 };
        const detailSale2: IDetailSale = { id: 456 };
        expectedResult = service.addDetailSaleToCollectionIfMissing([], detailSale, detailSale2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(detailSale);
        expect(expectedResult).toContain(detailSale2);
      });

      it('should accept null and undefined values', () => {
        const detailSale: IDetailSale = { id: 123 };
        expectedResult = service.addDetailSaleToCollectionIfMissing([], null, detailSale, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(detailSale);
      });

      it('should return initial array if no DetailSale is added', () => {
        const detailSaleCollection: IDetailSale[] = [{ id: 123 }];
        expectedResult = service.addDetailSaleToCollectionIfMissing(detailSaleCollection, undefined, null);
        expect(expectedResult).toEqual(detailSaleCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
