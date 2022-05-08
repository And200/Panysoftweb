import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IMeasureUnit } from '../measure-unit.model';

@Component({
  selector: 'panysoft-measure-unit-detail',
  templateUrl: './measure-unit-detail.component.html',
})
export class MeasureUnitDetailComponent implements OnInit {
  measureUnit: IMeasureUnit | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ measureUnit }) => {
      this.measureUnit = measureUnit;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
