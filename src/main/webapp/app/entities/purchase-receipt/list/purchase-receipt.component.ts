import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IPurchaseReceipt } from '../purchase-receipt.model';
import { PurchaseReceiptService } from '../service/purchase-receipt.service';
import { PurchaseReceiptDeleteDialogComponent } from '../delete/purchase-receipt-delete-dialog.component';

@Component({
  selector: 'panysoft-purchase-receipt',
  templateUrl: './purchase-receipt.component.html',
})
export class PurchaseReceiptComponent implements OnInit {
  purchaseReceipts?: IPurchaseReceipt[];
  isLoading = false;

  constructor(protected purchaseReceiptService: PurchaseReceiptService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.purchaseReceiptService.query().subscribe({
      next: (res: HttpResponse<IPurchaseReceipt[]>) => {
        this.isLoading = false;
        this.purchaseReceipts = res.body ?? [];
      },
      error: () => {
        this.isLoading = false;
      },
    });
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(_index: number, item: IPurchaseReceipt): number {
    return item.id!;
  }

  delete(purchaseReceipt: IPurchaseReceipt): void {
    const modalRef = this.modalService.open(PurchaseReceiptDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.purchaseReceipt = purchaseReceipt;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
