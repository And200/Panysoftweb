import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IInventory } from '../inventory.model';
import { InventoryService } from '../service/inventory.service';
import { InventoryDeleteDialogComponent } from '../delete/inventory-delete-dialog.component';

@Component({
  selector: 'panysoft-inventory',
  templateUrl: './inventory.component.html',
})
export class InventoryComponent implements OnInit {
  inventories?: IInventory[];
  isLoading = false;

  constructor(protected inventoryService: InventoryService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.inventoryService.query().subscribe({
      next: (res: HttpResponse<IInventory[]>) => {
        this.isLoading = false;
        this.inventories = res.body ?? [];
      },
      error: () => {
        this.isLoading = false;
      },
    });
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(_index: number, item: IInventory): number {
    return item.id!;
  }

  delete(inventory: IInventory): void {
    const modalRef = this.modalService.open(InventoryDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.inventory = inventory;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
