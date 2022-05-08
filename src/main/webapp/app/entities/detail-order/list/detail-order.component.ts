import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IDetailOrder } from '../detail-order.model';
import { DetailOrderService } from '../service/detail-order.service';
import { DetailOrderDeleteDialogComponent } from '../delete/detail-order-delete-dialog.component';

@Component({
  selector: 'panysoft-detail-order',
  templateUrl: './detail-order.component.html',
})
export class DetailOrderComponent implements OnInit {
  detailOrders?: IDetailOrder[];
  isLoading = false;

  constructor(protected detailOrderService: DetailOrderService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.detailOrderService.query().subscribe({
      next: (res: HttpResponse<IDetailOrder[]>) => {
        this.isLoading = false;
        this.detailOrders = res.body ?? [];
      },
      error: () => {
        this.isLoading = false;
      },
    });
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(_index: number, item: IDetailOrder): number {
    return item.id!;
  }

  delete(detailOrder: IDetailOrder): void {
    const modalRef = this.modalService.open(DetailOrderDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.detailOrder = detailOrder;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
