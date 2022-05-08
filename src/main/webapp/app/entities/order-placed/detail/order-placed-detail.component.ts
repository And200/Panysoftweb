import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IOrderPlaced } from '../order-placed.model';

@Component({
  selector: 'panysoft-order-placed-detail',
  templateUrl: './order-placed-detail.component.html',
})
export class OrderPlacedDetailComponent implements OnInit {
  orderPlaced: IOrderPlaced | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ orderPlaced }) => {
      this.orderPlaced = orderPlaced;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
