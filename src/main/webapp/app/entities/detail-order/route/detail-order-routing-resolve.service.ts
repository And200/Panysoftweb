import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IDetailOrder, DetailOrder } from '../detail-order.model';
import { DetailOrderService } from '../service/detail-order.service';

@Injectable({ providedIn: 'root' })
export class DetailOrderRoutingResolveService implements Resolve<IDetailOrder> {
  constructor(protected service: DetailOrderService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IDetailOrder> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((detailOrder: HttpResponse<DetailOrder>) => {
          if (detailOrder.body) {
            return of(detailOrder.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new DetailOrder());
  }
}
