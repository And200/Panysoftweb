import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IRecip } from '../recip.model';
import { RecipService } from '../service/recip.service';
import { RecipDeleteDialogComponent } from '../delete/recip-delete-dialog.component';

@Component({
  selector: 'panysoft-recip',
  templateUrl: './recip.component.html',
})
export class RecipComponent implements OnInit {
  recips?: IRecip[];
  isLoading = false;

  constructor(protected recipService: RecipService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.recipService.query().subscribe({
      next: (res: HttpResponse<IRecip[]>) => {
        this.isLoading = false;
        this.recips = res.body ?? [];
      },
      error: () => {
        this.isLoading = false;
      },
    });
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(_index: number, item: IRecip): number {
    return item.id!;
  }

  delete(recip: IRecip): void {
    const modalRef = this.modalService.open(RecipDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.recip = recip;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
