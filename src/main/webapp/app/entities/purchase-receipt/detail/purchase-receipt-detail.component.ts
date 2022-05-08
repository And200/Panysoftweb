import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IPurchaseReceipt } from '../purchase-receipt.model';

@Component({
  selector: 'panysoft-purchase-receipt-detail',
  templateUrl: './purchase-receipt-detail.component.html',
})
export class PurchaseReceiptDetailComponent implements OnInit {
  purchaseReceipt: IPurchaseReceipt | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ purchaseReceipt }) => {
      this.purchaseReceipt = purchaseReceipt;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
