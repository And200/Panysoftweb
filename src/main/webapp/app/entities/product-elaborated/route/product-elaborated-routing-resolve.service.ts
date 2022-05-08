import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IProductElaborated, ProductElaborated } from '../product-elaborated.model';
import { ProductElaboratedService } from '../service/product-elaborated.service';

@Injectable({ providedIn: 'root' })
export class ProductElaboratedRoutingResolveService implements Resolve<IProductElaborated> {
  constructor(protected service: ProductElaboratedService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IProductElaborated> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((productElaborated: HttpResponse<ProductElaborated>) => {
          if (productElaborated.body) {
            return of(productElaborated.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new ProductElaborated());
  }
}
