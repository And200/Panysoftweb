import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IPresentation } from '../presentation.model';
import { PresentationService } from '../service/presentation.service';
import { PresentationDeleteDialogComponent } from '../delete/presentation-delete-dialog.component';

@Component({
  selector: 'panysoft-presentation',
  templateUrl: './presentation.component.html',
})
export class PresentationComponent implements OnInit {
  presentations?: IPresentation[];
  isLoading = false;

  constructor(protected presentationService: PresentationService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.presentationService.query().subscribe({
      next: (res: HttpResponse<IPresentation[]>) => {
        this.isLoading = false;
        this.presentations = res.body ?? [];
      },
      error: () => {
        this.isLoading = false;
      },
    });
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(_index: number, item: IPresentation): number {
    return item.id!;
  }

  delete(presentation: IPresentation): void {
    const modalRef = this.modalService.open(PresentationDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.presentation = presentation;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
