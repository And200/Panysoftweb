import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IMeasureUnit } from '../measure-unit.model';
import { MeasureUnitService } from '../service/measure-unit.service';
import { MeasureUnitDeleteDialogComponent } from '../delete/measure-unit-delete-dialog.component';

@Component({
  selector: 'panysoft-measure-unit',
  templateUrl: './measure-unit.component.html',
})
export class MeasureUnitComponent implements OnInit {
  measureUnits?: IMeasureUnit[];
  isLoading = false;

  constructor(protected measureUnitService: MeasureUnitService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.measureUnitService.query().subscribe({
      next: (res: HttpResponse<IMeasureUnit[]>) => {
        this.isLoading = false;
        this.measureUnits = res.body ?? [];
      },
      error: () => {
        this.isLoading = false;
      },
    });
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(_index: number, item: IMeasureUnit): number {
    return item.id!;
  }

  delete(measureUnit: IMeasureUnit): void {
    const modalRef = this.modalService.open(MeasureUnitDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.measureUnit = measureUnit;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
