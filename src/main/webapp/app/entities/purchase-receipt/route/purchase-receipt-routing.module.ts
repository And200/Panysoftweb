import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { PurchaseReceiptComponent } from '../list/purchase-receipt.component';
import { PurchaseReceiptDetailComponent } from '../detail/purchase-receipt-detail.component';
import { PurchaseReceiptUpdateComponent } from '../update/purchase-receipt-update.component';
import { PurchaseReceiptRoutingResolveService } from './purchase-receipt-routing-resolve.service';

const purchaseReceiptRoute: Routes = [
  {
    path: '',
    component: PurchaseReceiptComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: PurchaseReceiptDetailComponent,
    resolve: {
      purchaseReceipt: PurchaseReceiptRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: PurchaseReceiptUpdateComponent,
    resolve: {
      purchaseReceipt: PurchaseReceiptRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: PurchaseReceiptUpdateComponent,
    resolve: {
      purchaseReceipt: PurchaseReceiptRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(purchaseReceiptRoute)],
  exports: [RouterModule],
})
export class PurchaseReceiptRoutingModule {}
