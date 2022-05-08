import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IPurchaseReceipt, PurchaseReceipt } from '../purchase-receipt.model';
import { PurchaseReceiptService } from '../service/purchase-receipt.service';

@Injectable({ providedIn: 'root' })
export class PurchaseReceiptRoutingResolveService implements Resolve<IPurchaseReceipt> {
  constructor(protected service: PurchaseReceiptService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IPurchaseReceipt> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((purchaseReceipt: HttpResponse<PurchaseReceipt>) => {
          if (purchaseReceipt.body) {
            return of(purchaseReceipt.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new PurchaseReceipt());
  }
}
