import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, ActivatedRoute, Router, convertToParamMap } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { IProductElaborated, ProductElaborated } from '../product-elaborated.model';
import { ProductElaboratedService } from '../service/product-elaborated.service';

import { ProductElaboratedRoutingResolveService } from './product-elaborated-routing-resolve.service';

describe('ProductElaborated routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: ProductElaboratedRoutingResolveService;
  let service: ProductElaboratedService;
  let resultProductElaborated: IProductElaborated | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: {
            snapshot: {
              paramMap: convertToParamMap({}),
            },
          },
        },
      ],
    });
    mockRouter = TestBed.inject(Router);
    jest.spyOn(mockRouter, 'navigate').mockImplementation(() => Promise.resolve(true));
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRoute).snapshot;
    routingResolveService = TestBed.inject(ProductElaboratedRoutingResolveService);
    service = TestBed.inject(ProductElaboratedService);
    resultProductElaborated = undefined;
  });

  describe('resolve', () => {
    it('should return IProductElaborated returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultProductElaborated = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultProductElaborated).toEqual({ id: 123 });
    });

    it('should return new IProductElaborated if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultProductElaborated = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultProductElaborated).toEqual(new ProductElaborated());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as ProductElaborated })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultProductElaborated = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultProductElaborated).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
