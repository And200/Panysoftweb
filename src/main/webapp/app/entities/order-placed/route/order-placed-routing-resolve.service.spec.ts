import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, ActivatedRoute, Router, convertToParamMap } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { IOrderPlaced, OrderPlaced } from '../order-placed.model';
import { OrderPlacedService } from '../service/order-placed.service';

import { OrderPlacedRoutingResolveService } from './order-placed-routing-resolve.service';

describe('OrderPlaced routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: OrderPlacedRoutingResolveService;
  let service: OrderPlacedService;
  let resultOrderPlaced: IOrderPlaced | undefined;

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
    routingResolveService = TestBed.inject(OrderPlacedRoutingResolveService);
    service = TestBed.inject(OrderPlacedService);
    resultOrderPlaced = undefined;
  });

  describe('resolve', () => {
    it('should return IOrderPlaced returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultOrderPlaced = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultOrderPlaced).toEqual({ id: 123 });
    });

    it('should return new IOrderPlaced if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultOrderPlaced = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultOrderPlaced).toEqual(new OrderPlaced());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as OrderPlaced })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultOrderPlaced = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultOrderPlaced).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
