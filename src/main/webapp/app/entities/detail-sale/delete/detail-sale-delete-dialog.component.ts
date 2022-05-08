import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IDetailSale } from '../detail-sale.model';
import { DetailSaleService } from '../service/detail-sale.service';

@Component({
  templateUrl: './detail-sale-delete-dialog.component.html',
})
export class DetailSaleDeleteDialogComponent {
  detailSale?: IDetailSale;

  constructor(protected detailSaleService: DetailSaleService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.detailSaleService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
