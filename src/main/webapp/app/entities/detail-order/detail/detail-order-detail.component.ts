import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IDetailOrder } from '../detail-order.model';

@Component({
  selector: 'panysoft-detail-order-detail',
  templateUrl: './detail-order-detail.component.html',
})
export class DetailOrderDetailComponent implements OnInit {
  detailOrder: IDetailOrder | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ detailOrder }) => {
      this.detailOrder = detailOrder;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
