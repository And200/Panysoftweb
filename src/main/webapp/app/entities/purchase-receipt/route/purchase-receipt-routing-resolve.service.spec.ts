import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, ActivatedRoute, Router, convertToParamMap } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { IPurchaseReceipt, PurchaseReceipt } from '../purchase-receipt.model';
import { PurchaseReceiptService } from '../service/purchase-receipt.service';

import { PurchaseReceiptRoutingResolveService } from './purchase-receipt-routing-resolve.service';

describe('PurchaseReceipt routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: PurchaseReceiptRoutingResolveService;
  let service: PurchaseReceiptService;
  let resultPurchaseReceipt: IPurchaseReceipt | undefined;

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
    routingResolveService = TestBed.inject(PurchaseReceiptRoutingResolveService);
    service = TestBed.inject(PurchaseReceiptService);
    resultPurchaseReceipt = undefined;
  });

  describe('resolve', () => {
    it('should return IPurchaseReceipt returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultPurchaseReceipt = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultPurchaseReceipt).toEqual({ id: 123 });
    });

    it('should return new IPurchaseReceipt if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultPurchaseReceipt = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultPurchaseReceipt).toEqual(new PurchaseReceipt());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as PurchaseReceipt })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultPurchaseReceipt = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultPurchaseReceipt).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
