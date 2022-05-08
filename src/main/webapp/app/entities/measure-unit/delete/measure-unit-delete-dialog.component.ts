import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IMeasureUnit } from '../measure-unit.model';
import { MeasureUnitService } from '../service/measure-unit.service';

@Component({
  templateUrl: './measure-unit-delete-dialog.component.html',
})
export class MeasureUnitDeleteDialogComponent {
  measureUnit?: IMeasureUnit;

  constructor(protected measureUnitService: MeasureUnitService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.measureUnitService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
