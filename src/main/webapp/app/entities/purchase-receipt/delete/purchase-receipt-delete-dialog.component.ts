import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IPurchaseReceipt } from '../purchase-receipt.model';
import { PurchaseReceiptService } from '../service/purchase-receipt.service';

@Component({
  templateUrl: './purchase-receipt-delete-dialog.component.html',
})
export class PurchaseReceiptDeleteDialogComponent {
  purchaseReceipt?: IPurchaseReceipt;

  constructor(protected purchaseReceiptService: PurchaseReceiptService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.purchaseReceiptService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
