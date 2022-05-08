import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IProductElaborated } from '../product-elaborated.model';
import { ProductElaboratedService } from '../service/product-elaborated.service';

@Component({
  templateUrl: './product-elaborated-delete-dialog.component.html',
})
export class ProductElaboratedDeleteDialogComponent {
  productElaborated?: IProductElaborated;

  constructor(protected productElaboratedService: ProductElaboratedService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.productElaboratedService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
