import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IPresentation } from '../presentation.model';

@Component({
  selector: 'panysoft-presentation-detail',
  templateUrl: './presentation-detail.component.html',
})
export class PresentationDetailComponent implements OnInit {
  presentation: IPresentation | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ presentation }) => {
      this.presentation = presentation;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
