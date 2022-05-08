import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IProductElaborated } from '../product-elaborated.model';
import { ProductElaboratedService } from '../service/product-elaborated.service';
import { ProductElaboratedDeleteDialogComponent } from '../delete/product-elaborated-delete-dialog.component';

@Component({
  selector: 'panysoft-product-elaborated',
  templateUrl: './product-elaborated.component.html',
})
export class ProductElaboratedComponent implements OnInit {
  productElaborateds?: IProductElaborated[];
  isLoading = false;

  constructor(protected productElaboratedService: ProductElaboratedService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.productElaboratedService.query().subscribe({
      next: (res: HttpResponse<IProductElaborated[]>) => {
        this.isLoading = false;
        this.productElaborateds = res.body ?? [];
      },
      error: () => {
        this.isLoading = false;
      },
    });
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(_index: number, item: IProductElaborated): number {
    return item.id!;
  }

  delete(productElaborated: IProductElaborated): void {
    const modalRef = this.modalService.open(ProductElaboratedDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.productElaborated = productElaborated;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
