import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IProvider } from '../provider.model';
import { ProviderService } from '../service/provider.service';

@Component({
  templateUrl: './provider-delete-dialog.component.html',
})
export class ProviderDeleteDialogComponent {
  provider?: IProvider;

  constructor(protected providerService: ProviderService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.providerService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
