import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IRecip } from '../recip.model';

@Component({
  selector: 'panysoft-recip-detail',
  templateUrl: './recip-detail.component.html',
})
export class RecipDetailComponent implements OnInit {
  recip: IRecip | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ recip }) => {
      this.recip = recip;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
