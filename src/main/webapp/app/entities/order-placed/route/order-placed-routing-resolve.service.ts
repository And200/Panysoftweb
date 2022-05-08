import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IOrderPlaced, OrderPlaced } from '../order-placed.model';
import { OrderPlacedService } from '../service/order-placed.service';

@Injectable({ providedIn: 'root' })
export class OrderPlacedRoutingResolveService implements Resolve<IOrderPlaced> {
  constructor(protected service: OrderPlacedService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IOrderPlaced> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((orderPlaced: HttpResponse<OrderPlaced>) => {
          if (orderPlaced.body) {
            return of(orderPlaced.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new OrderPlaced());
  }
}
