import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { RecipComponent } from '../list/recip.component';
import { RecipDetailComponent } from '../detail/recip-detail.component';
import { RecipUpdateComponent } from '../update/recip-update.component';
import { RecipRoutingResolveService } from './recip-routing-resolve.service';

const recipRoute: Routes = [
  {
    path: '',
    component: RecipComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: RecipDetailComponent,
    resolve: {
      recip: RecipRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: RecipUpdateComponent,
    resolve: {
      recip: RecipRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: RecipUpdateComponent,
    resolve: {
      recip: RecipRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(recipRoute)],
  exports: [RouterModule],
})
export class RecipRoutingModule {}
