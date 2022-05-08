import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IDetailSale } from '../detail-sale.model';
import { DetailSaleService } from '../service/detail-sale.service';
import { DetailSaleDeleteDialogComponent } from '../delete/detail-sale-delete-dialog.component';

@Component({
  selector: 'panysoft-detail-sale',
  templateUrl: './detail-sale.component.html',
})
export class DetailSaleComponent implements OnInit {
  detailSales?: IDetailSale[];
  isLoading = false;

  constructor(protected detailSaleService: DetailSaleService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.detailSaleService.query().subscribe({
      next: (res: HttpResponse<IDetailSale[]>) => {
        this.isLoading = false;
        this.detailSales = res.body ?? [];
      },
      error: () => {
        this.isLoading = false;
      },
    });
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(_index: number, item: IDetailSale): number {
    return item.id!;
  }

  delete(detailSale: IDetailSale): void {
    const modalRef = this.modalService.open(DetailSaleDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.detailSale = detailSale;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
