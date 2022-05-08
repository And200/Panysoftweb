import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IRecip, Recip } from '../recip.model';
import { RecipService } from '../service/recip.service';

@Injectable({ providedIn: 'root' })
export class RecipRoutingResolveService implements Resolve<IRecip> {
  constructor(protected service: RecipService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IRecip> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((recip: HttpResponse<Recip>) => {
          if (recip.body) {
            return of(recip.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Recip());
  }
}
