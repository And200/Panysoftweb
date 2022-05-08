import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IProductElaborated } from '../product-elaborated.model';

@Component({
  selector: 'panysoft-product-elaborated-detail',
  templateUrl: './product-elaborated-detail.component.html',
})
export class ProductElaboratedDetailComponent implements OnInit {
  productElaborated: IProductElaborated | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ productElaborated }) => {
      this.productElaborated = productElaborated;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
