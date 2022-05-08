import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IProvider } from '../provider.model';
import { ProviderService } from '../service/provider.service';
import { ProviderDeleteDialogComponent } from '../delete/provider-delete-dialog.component';

@Component({
  selector: 'panysoft-provider',
  templateUrl: './provider.component.html',
})
export class ProviderComponent implements OnInit {
  providers?: IProvider[];
  isLoading = false;

  constructor(protected providerService: ProviderService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.providerService.query().subscribe({
      next: (res: HttpResponse<IProvider[]>) => {
        this.isLoading = false;
        this.providers = res.body ?? [];
      },
      error: () => {
        this.isLoading = false;
      },
    });
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(_index: number, item: IProvider): number {
    return item.id!;
  }

  delete(provider: IProvider): void {
    const modalRef = this.modalService.open(ProviderDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.provider = provider;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
