import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IProvider, Provider } from '../provider.model';

import { ProviderService } from './provider.service';

describe('Provider Service', () => {
  let service: ProviderService;
  let httpMock: HttpTestingController;
  let elemDefault: IProvider;
  let expectedResult: IProvider | IProvider[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(ProviderService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      email: 'AAAAAAA',
      adress: 'AAAAAAA',
      nit: 'AAAAAAA',
      name: 'AAAAAAA',
      phone: 'AAAAAAA',
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

    it('should create a Provider', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new Provider()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Provider', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          email: 'BBBBBB',
          adress: 'BBBBBB',
          nit: 'BBBBBB',
          name: 'BBBBBB',
          phone: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Provider', () => {
      const patchObject = Object.assign(
        {
          adress: 'BBBBBB',
        },
        new Provider()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Provider', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          email: 'BBBBBB',
          adress: 'BBBBBB',
          nit: 'BBBBBB',
          name: 'BBBBBB',
          phone: 'BBBBBB',
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

    it('should delete a Provider', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addProviderToCollectionIfMissing', () => {
      it('should add a Provider to an empty array', () => {
        const provider: IProvider = { id: 123 };
        expectedResult = service.addProviderToCollectionIfMissing([], provider);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(provider);
      });

      it('should not add a Provider to an array that contains it', () => {
        const provider: IProvider = { id: 123 };
        const providerCollection: IProvider[] = [
          {
            ...provider,
          },
          { id: 456 },
        ];
        expectedResult = service.addProviderToCollectionIfMissing(providerCollection, provider);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Provider to an array that doesn't contain it", () => {
        const provider: IProvider = { id: 123 };
        const providerCollection: IProvider[] = [{ id: 456 }];
        expectedResult = service.addProviderToCollectionIfMissing(providerCollection, provider);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(provider);
      });

      it('should add only unique Provider to an array', () => {
        const providerArray: IProvider[] = [{ id: 123 }, { id: 456 }, { id: 61285 }];
        const providerCollection: IProvider[] = [{ id: 123 }];
        expectedResult = service.addProviderToCollectionIfMissing(providerCollection, ...providerArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const provider: IProvider = { id: 123 };
        const provider2: IProvider = { id: 456 };
        expectedResult = service.addProviderToCollectionIfMissing([], provider, provider2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(provider);
        expect(expectedResult).toContain(provider2);
      });

      it('should accept null and undefined values', () => {
        const provider: IProvider = { id: 123 };
        expectedResult = service.addProviderToCollectionIfMissing([], null, provider, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(provider);
      });

      it('should return initial array if no Provider is added', () => {
        const providerCollection: IProvider[] = [{ id: 123 }];
        expectedResult = service.addProviderToCollectionIfMissing(providerCollection, undefined, null);
        expect(expectedResult).toEqual(providerCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
