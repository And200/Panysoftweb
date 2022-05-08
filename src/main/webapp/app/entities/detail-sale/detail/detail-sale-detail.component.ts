import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IDetailSale } from '../detail-sale.model';

@Component({
  selector: 'panysoft-detail-sale-detail',
  templateUrl: './detail-sale-detail.component.html',
})
export class DetailSaleDetailComponent implements OnInit {
  detailSale: IDetailSale | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ detailSale }) => {
      this.detailSale = detailSale;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
