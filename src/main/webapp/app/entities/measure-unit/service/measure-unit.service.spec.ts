import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IMeasureUnit, MeasureUnit } from '../measure-unit.model';

import { MeasureUnitService } from './measure-unit.service';

describe('MeasureUnit Service', () => {
  let service: MeasureUnitService;
  let httpMock: HttpTestingController;
  let elemDefault: IMeasureUnit;
  let expectedResult: IMeasureUnit | IMeasureUnit[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(MeasureUnitService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      nameUnit: 'AAAAAAA',
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

    it('should create a MeasureUnit', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new MeasureUnit()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a MeasureUnit', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          nameUnit: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a MeasureUnit', () => {
      const patchObject = Object.assign({}, new MeasureUnit());

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of MeasureUnit', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          nameUnit: 'BBBBBB',
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

    it('should delete a MeasureUnit', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addMeasureUnitToCollectionIfMissing', () => {
      it('should add a MeasureUnit to an empty array', () => {
        const measureUnit: IMeasureUnit = { id: 123 };
        expectedResult = service.addMeasureUnitToCollectionIfMissing([], measureUnit);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(measureUnit);
      });

      it('should not add a MeasureUnit to an array that contains it', () => {
        const measureUnit: IMeasureUnit = { id: 123 };
        const measureUnitCollection: IMeasureUnit[] = [
          {
            ...measureUnit,
          },
          { id: 456 },
        ];
        expectedResult = service.addMeasureUnitToCollectionIfMissing(measureUnitCollection, measureUnit);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a MeasureUnit to an array that doesn't contain it", () => {
        const measureUnit: IMeasureUnit = { id: 123 };
        const measureUnitCollection: IMeasureUnit[] = [{ id: 456 }];
        expectedResult = service.addMeasureUnitToCollectionIfMissing(measureUnitCollection, measureUnit);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(measureUnit);
      });

      it('should add only unique MeasureUnit to an array', () => {
        const measureUnitArray: IMeasureUnit[] = [{ id: 123 }, { id: 456 }, { id: 4097 }];
        const measureUnitCollection: IMeasureUnit[] = [{ id: 123 }];
        expectedResult = service.addMeasureUnitToCollectionIfMissing(measureUnitCollection, ...measureUnitArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const measureUnit: IMeasureUnit = { id: 123 };
        const measureUnit2: IMeasureUnit = { id: 456 };
        expectedResult = service.addMeasureUnitToCollectionIfMissing([], measureUnit, measureUnit2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(measureUnit);
        expect(expectedResult).toContain(measureUnit2);
      });

      it('should accept null and undefined values', () => {
        const measureUnit: IMeasureUnit = { id: 123 };
        expectedResult = service.addMeasureUnitToCollectionIfMissing([], null, measureUnit, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(measureUnit);
      });

      it('should return initial array if no MeasureUnit is added', () => {
        const measureUnitCollection: IMeasureUnit[] = [{ id: 123 }];
        expectedResult = service.addMeasureUnitToCollectionIfMissing(measureUnitCollection, undefined, null);
        expect(expectedResult).toEqual(measureUnitCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
