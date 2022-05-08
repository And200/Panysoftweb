import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IOrderPlaced } from '../order-placed.model';
import { OrderPlacedService } from '../service/order-placed.service';
import { OrderPlacedDeleteDialogComponent } from '../delete/order-placed-delete-dialog.component';

@Component({
  selector: 'panysoft-order-placed',
  templateUrl: './order-placed.component.html',
})
export class OrderPlacedComponent implements OnInit {
  orderPlaceds?: IOrderPlaced[];
  isLoading = false;

  constructor(protected orderPlacedService: OrderPlacedService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.orderPlacedService.query().subscribe({
      next: (res: HttpResponse<IOrderPlaced[]>) => {
        this.isLoading = false;
        this.orderPlaceds = res.body ?? [];
      },
      error: () => {
        this.isLoading = false;
      },
    });
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(_index: number, item: IOrderPlaced): number {
    return item.id!;
  }

  delete(orderPlaced: IOrderPlaced): void {
    const modalRef = this.modalService.open(OrderPlacedDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.orderPlaced = orderPlaced;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
