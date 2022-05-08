import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IPresentation, Presentation } from '../presentation.model';

import { PresentationService } from './presentation.service';

describe('Presentation Service', () => {
  let service: PresentationService;
  let httpMock: HttpTestingController;
  let elemDefault: IPresentation;
  let expectedResult: IPresentation | IPresentation[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(PresentationService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      presentation: 'AAAAAAA',
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

    it('should create a Presentation', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new Presentation()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Presentation', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          presentation: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Presentation', () => {
      const patchObject = Object.assign(
        {
          presentation: 'BBBBBB',
        },
        new Presentation()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Presentation', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          presentation: 'BBBBBB',
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

    it('should delete a Presentation', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addPresentationToCollectionIfMissing', () => {
      it('should add a Presentation to an empty array', () => {
        const presentation: IPresentation = { id: 123 };
        expectedResult = service.addPresentationToCollectionIfMissing([], presentation);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(presentation);
      });

      it('should not add a Presentation to an array that contains it', () => {
        const presentation: IPresentation = { id: 123 };
        const presentationCollection: IPresentation[] = [
          {
            ...presentation,
          },
          { id: 456 },
        ];
        expectedResult = service.addPresentationToCollectionIfMissing(presentationCollection, presentation);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Presentation to an array that doesn't contain it", () => {
        const presentation: IPresentation = { id: 123 };
        const presentationCollection: IPresentation[] = [{ id: 456 }];
        expectedResult = service.addPresentationToCollectionIfMissing(presentationCollection, presentation);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(presentation);
      });

      it('should add only unique Presentation to an array', () => {
        const presentationArray: IPresentation[] = [{ id: 123 }, { id: 456 }, { id: 85496 }];
        const presentationCollection: IPresentation[] = [{ id: 123 }];
        expectedResult = service.addPresentationToCollectionIfMissing(presentationCollection, ...presentationArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const presentation: IPresentation = { id: 123 };
        const presentation2: IPresentation = { id: 456 };
        expectedResult = service.addPresentationToCollectionIfMissing([], presentation, presentation2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(presentation);
        expect(expectedResult).toContain(presentation2);
      });

      it('should accept null and undefined values', () => {
        const presentation: IPresentation = { id: 123 };
        expectedResult = service.addPresentationToCollectionIfMissing([], null, presentation, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(presentation);
      });

      it('should return initial array if no Presentation is added', () => {
        const presentationCollection: IPresentation[] = [{ id: 123 }];
        expectedResult = service.addPresentationToCollectionIfMissing(presentationCollection, undefined, null);
        expect(expectedResult).toEqual(presentationCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
