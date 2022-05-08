import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IPresentation } from '../presentation.model';
import { PresentationService } from '../service/presentation.service';

@Component({
  templateUrl: './presentation-delete-dialog.component.html',
})
export class PresentationDeleteDialogComponent {
  presentation?: IPresentation;

  constructor(protected presentationService: PresentationService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.presentationService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
