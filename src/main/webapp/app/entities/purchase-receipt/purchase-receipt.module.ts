import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { PurchaseReceiptComponent } from './list/purchase-receipt.component';
import { PurchaseReceiptDetailComponent } from './detail/purchase-receipt-detail.component';
import { PurchaseReceiptUpdateComponent } from './update/purchase-receipt-update.component';
import { PurchaseReceiptDeleteDialogComponent } from './delete/purchase-receipt-delete-dialog.component';
import { PurchaseReceiptRoutingModule } from './route/purchase-receipt-routing.module';

@NgModule({
  imports: [SharedModule, PurchaseReceiptRoutingModule],
  declarations: [
    PurchaseReceiptComponent,
    PurchaseReceiptDetailComponent,
    PurchaseReceiptUpdateComponent,
    PurchaseReceiptDeleteDialogComponent,
  ],
  entryComponents: [PurchaseReceiptDeleteDialogComponent],
})
export class PurchaseReceiptModule {}
