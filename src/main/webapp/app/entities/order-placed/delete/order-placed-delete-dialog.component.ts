import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IOrderPlaced } from '../order-placed.model';
import { OrderPlacedService } from '../service/order-placed.service';

@Component({
  templateUrl: './order-placed-delete-dialog.component.html',
})
export class OrderPlacedDeleteDialogComponent {
  orderPlaced?: IOrderPlaced;

  constructor(protected orderPlacedService: OrderPlacedService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.orderPlacedService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
