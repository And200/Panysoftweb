import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IRecip } from '../recip.model';
import { RecipService } from '../service/recip.service';

@Component({
  templateUrl: './recip-delete-dialog.component.html',
})
export class RecipDeleteDialogComponent {
  recip?: IRecip;

  constructor(protected recipService: RecipService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.recipService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
