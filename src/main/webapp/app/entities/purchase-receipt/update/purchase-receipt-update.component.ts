import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { IPurchaseReceipt, PurchaseReceipt } from '../purchase-receipt.model';
import { PurchaseReceiptService } from '../service/purchase-receipt.service';
import { IEmployee } from 'app/entities/employee/employee.model';
import { EmployeeService } from 'app/entities/employee/service/employee.service';
import { IDetailSale } from 'app/entities/detail-sale/detail-sale.model';
import { DetailSaleService } from 'app/entities/detail-sale/service/detail-sale.service';
import { IClient } from 'app/entities/client/client.model';
import { ClientService } from 'app/entities/client/service/client.service';

@Component({
  selector: 'panysoft-purchase-receipt-update',
  templateUrl: './purchase-receipt-update.component.html',
})
export class PurchaseReceiptUpdateComponent implements OnInit {
  isSaving = false;

  employeesSharedCollection: IEmployee[] = [];
  detailSalesSharedCollection: IDetailSale[] = [];
  clientsSharedCollection: IClient[] = [];

  editForm = this.fb.group({
    id: [],
    date: [null, [Validators.required]],
    totalSale: [null, [Validators.required]],
    employee: [null, Validators.required],
    detailSale: [null, Validators.required],
    client: [null, Validators.required],
  });

  constructor(
    protected purchaseReceiptService: PurchaseReceiptService,
    protected employeeService: EmployeeService,
    protected detailSaleService: DetailSaleService,
    protected clientService: ClientService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ purchaseReceipt }) => {
      if (purchaseReceipt.id === undefined) {
        const today = dayjs().startOf('day');
        purchaseReceipt.date = today;
      }

      this.updateForm(purchaseReceipt);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const purchaseReceipt = this.createFromForm();
    if (purchaseReceipt.id !== undefined) {
      this.subscribeToSaveResponse(this.purchaseReceiptService.update(purchaseReceipt));
    } else {
      this.subscribeToSaveResponse(this.purchaseReceiptService.create(purchaseReceipt));
    }
  }

  trackEmployeeById(_index: number, item: IEmployee): number {
    return item.id!;
  }

  trackDetailSaleById(_index: number, item: IDetailSale): number {
    return item.id!;
  }

  trackClientById(_index: number, item: IClient): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPurchaseReceipt>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(purchaseReceipt: IPurchaseReceipt): void {
    this.editForm.patchValue({
      id: purchaseReceipt.id,
      date: purchaseReceipt.date ? purchaseReceipt.date.format(DATE_TIME_FORMAT) : null,
      totalSale: purchaseReceipt.totalSale,
      employee: purchaseReceipt.employee,
      detailSale: purchaseReceipt.detailSale,
      client: purchaseReceipt.client,
    });

    this.employeesSharedCollection = this.employeeService.addEmployeeToCollectionIfMissing(
      this.employeesSharedCollection,
      purchaseReceipt.employee
    );
    this.detailSalesSharedCollection = this.detailSaleService.addDetailSaleToCollectionIfMissing(
      this.detailSalesSharedCollection,
      purchaseReceipt.detailSale
    );
    this.clientsSharedCollection = this.clientService.addClientToCollectionIfMissing(this.clientsSharedCollection, purchaseReceipt.client);
  }

  protected loadRelationshipsOptions(): void {
    this.employeeService
      .query()
      .pipe(map((res: HttpResponse<IEmployee[]>) => res.body ?? []))
      .pipe(
        map((employees: IEmployee[]) =>
          this.employeeService.addEmployeeToCollectionIfMissing(employees, this.editForm.get('employee')!.value)
        )
      )
      .subscribe((employees: IEmployee[]) => (this.employeesSharedCollection = employees));

    this.detailSaleService
      .query()
      .pipe(map((res: HttpResponse<IDetailSale[]>) => res.body ?? []))
      .pipe(
        map((detailSales: IDetailSale[]) =>
          this.detailSaleService.addDetailSaleToCollectionIfMissing(detailSales, this.editForm.get('detailSale')!.value)
        )
      )
      .subscribe((detailSales: IDetailSale[]) => (this.detailSalesSharedCollection = detailSales));

    this.clientService
      .query()
      .pipe(map((res: HttpResponse<IClient[]>) => res.body ?? []))
      .pipe(map((clients: IClient[]) => this.clientService.addClientToCollectionIfMissing(clients, this.editForm.get('client')!.value)))
      .subscribe((clients: IClient[]) => (this.clientsSharedCollection = clients));
  }

  protected createFromForm(): IPurchaseReceipt {
    return {
      ...new PurchaseReceipt(),
      id: this.editForm.get(['id'])!.value,
      date: this.editForm.get(['date'])!.value ? dayjs(this.editForm.get(['date'])!.value, DATE_TIME_FORMAT) : undefined,
      totalSale: this.editForm.get(['totalSale'])!.value,
      employee: this.editForm.get(['employee'])!.value,
      detailSale: this.editForm.get(['detailSale'])!.value,
      client: this.editForm.get(['client'])!.value,
    };
  }
}
