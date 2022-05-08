import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IDetailOrder } from '../detail-order.model';
import { DetailOrderService } from '../service/detail-order.service';

@Component({
  templateUrl: './detail-order-delete-dialog.component.html',
})
export class DetailOrderDeleteDialogComponent {
  detailOrder?: IDetailOrder;

  constructor(protected detailOrderService: DetailOrderService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.detailOrderService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
