import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ProductElaboratedComponent } from '../list/product-elaborated.component';
import { ProductElaboratedDetailComponent } from '../detail/product-elaborated-detail.component';
import { ProductElaboratedUpdateComponent } from '../update/product-elaborated-update.component';
import { ProductElaboratedRoutingResolveService } from './product-elaborated-routing-resolve.service';

const productElaboratedRoute: Routes = [
  {
    path: '',
    component: ProductElaboratedComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ProductElaboratedDetailComponent,
    resolve: {
      productElaborated: ProductElaboratedRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ProductElaboratedUpdateComponent,
    resolve: {
      productElaborated: ProductElaboratedRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ProductElaboratedUpdateComponent,
    resolve: {
      productElaborated: ProductElaboratedRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(productElaboratedRoute)],
  exports: [RouterModule],
})
export class ProductElaboratedRoutingModule {}
