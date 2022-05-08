import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IDetailSale, DetailSale } from '../detail-sale.model';
import { DetailSaleService } from '../service/detail-sale.service';

@Injectable({ providedIn: 'root' })
export class DetailSaleRoutingResolveService implements Resolve<IDetailSale> {
  constructor(protected service: DetailSaleService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IDetailSale> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((detailSale: HttpResponse<DetailSale>) => {
          if (detailSale.body) {
            return of(detailSale.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new DetailSale());
  }
}
