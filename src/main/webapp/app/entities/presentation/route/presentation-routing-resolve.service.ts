import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IPresentation, Presentation } from '../presentation.model';
import { PresentationService } from '../service/presentation.service';

@Injectable({ providedIn: 'root' })
export class PresentationRoutingResolveService implements Resolve<IPresentation> {
  constructor(protected service: PresentationService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IPresentation> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((presentation: HttpResponse<Presentation>) => {
          if (presentation.body) {
            return of(presentation.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Presentation());
  }
}
