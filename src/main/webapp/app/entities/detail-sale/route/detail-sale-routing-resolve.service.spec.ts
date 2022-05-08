import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, ActivatedRoute, Router, convertToParamMap } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { IDetailSale, DetailSale } from '../detail-sale.model';
import { DetailSaleService } from '../service/detail-sale.service';

import { DetailSaleRoutingResolveService } from './detail-sale-routing-resolve.service';

describe('DetailSale routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: DetailSaleRoutingResolveService;
  let service: DetailSaleService;
  let resultDetailSale: IDetailSale | undefined;

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
    routingResolveService = TestBed.inject(DetailSaleRoutingResolveService);
    service = TestBed.inject(DetailSaleService);
    resultDetailSale = undefined;
  });

  describe('resolve', () => {
    it('should return IDetailSale returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultDetailSale = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultDetailSale).toEqual({ id: 123 });
    });

    it('should return new IDetailSale if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultDetailSale = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultDetailSale).toEqual(new DetailSale());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as DetailSale })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultDetailSale = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultDetailSale).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
