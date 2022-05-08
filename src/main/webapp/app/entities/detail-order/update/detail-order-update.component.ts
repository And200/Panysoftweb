import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { IDetailOrder, DetailOrder } from '../detail-order.model';
import { DetailOrderService } from '../service/detail-order.service';
import { IProvider } from 'app/entities/provider/provider.model';
import { ProviderService } from 'app/entities/provider/service/provider.service';
import { IOrderPlaced } from 'app/entities/order-placed/order-placed.model';
import { OrderPlacedService } from 'app/entities/order-placed/service/order-placed.service';

@Component({
  selector: 'panysoft-detail-order-update',
  templateUrl: './detail-order-update.component.html',
})
export class DetailOrderUpdateComponent implements OnInit {
  isSaving = false;

  providersSharedCollection: IProvider[] = [];
  orderPlacedsSharedCollection: IOrderPlaced[] = [];

  editForm = this.fb.group({
    id: [],
    quantityOrdered: [null, [Validators.required]],
    date: [null, [Validators.required]],
    invoiceNumber: [null, [Validators.required, Validators.maxLength(30)]],
    productOrdered: [null, [Validators.required, Validators.maxLength(30)]],
    pricePayment: [null, [Validators.required]],
    provider: [null, Validators.required],
    orderPlaced: [null, Validators.required],
  });

  constructor(
    protected detailOrderService: DetailOrderService,
    protected providerService: ProviderService,
    protected orderPlacedService: OrderPlacedService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ detailOrder }) => {
      if (detailOrder.id === undefined) {
        const today = dayjs().startOf('day');
        detailOrder.date = today;
      }

      this.updateForm(detailOrder);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const detailOrder = this.createFromForm();
    if (detailOrder.id !== undefined) {
      this.subscribeToSaveResponse(this.detailOrderService.update(detailOrder));
    } else {
      this.subscribeToSaveResponse(this.detailOrderService.create(detailOrder));
    }
  }

  trackProviderById(_index: number, item: IProvider): number {
    return item.id!;
  }

  trackOrderPlacedById(_index: number, item: IOrderPlaced): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IDetailOrder>>): void {
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

  protected updateForm(detailOrder: IDetailOrder): void {
    this.editForm.patchValue({
      id: detailOrder.id,
      quantityOrdered: detailOrder.quantityOrdered,
      date: detailOrder.date ? detailOrder.date.format(DATE_TIME_FORMAT) : null,
      invoiceNumber: detailOrder.invoiceNumber,
      productOrdered: detailOrder.productOrdered,
      pricePayment: detailOrder.pricePayment,
      provider: detailOrder.provider,
      orderPlaced: detailOrder.orderPlaced,
    });

    this.providersSharedCollection = this.providerService.addProviderToCollectionIfMissing(
      this.providersSharedCollection,
      detailOrder.provider
    );
    this.orderPlacedsSharedCollection = this.orderPlacedService.addOrderPlacedToCollectionIfMissing(
      this.orderPlacedsSharedCollection,
      detailOrder.orderPlaced
    );
  }

  protected loadRelationshipsOptions(): void {
    this.providerService
      .query()
      .pipe(map((res: HttpResponse<IProvider[]>) => res.body ?? []))
      .pipe(
        map((providers: IProvider[]) =>
          this.providerService.addProviderToCollectionIfMissing(providers, this.editForm.get('provider')!.value)
        )
      )
      .subscribe((providers: IProvider[]) => (this.providersSharedCollection = providers));

    this.orderPlacedService
      .query()
      .pipe(map((res: HttpResponse<IOrderPlaced[]>) => res.body ?? []))
      .pipe(
        map((orderPlaceds: IOrderPlaced[]) =>
          this.orderPlacedService.addOrderPlacedToCollectionIfMissing(orderPlaceds, this.editForm.get('orderPlaced')!.value)
        )
      )
      .subscribe((orderPlaceds: IOrderPlaced[]) => (this.orderPlacedsSharedCollection = orderPlaceds));
  }

  protected createFromForm(): IDetailOrder {
    return {
      ...new DetailOrder(),
      id: this.editForm.get(['id'])!.value,
      quantityOrdered: this.editForm.get(['quantityOrdered'])!.value,
      date: this.editForm.get(['date'])!.value ? dayjs(this.editForm.get(['date'])!.value, DATE_TIME_FORMAT) : undefined,
      invoiceNumber: this.editForm.get(['invoiceNumber'])!.value,
      productOrdered: this.editForm.get(['productOrdered'])!.value,
      pricePayment: this.editForm.get(['pricePayment'])!.value,
      provider: this.editForm.get(['provider'])!.value,
      orderPlaced: this.editForm.get(['orderPlaced'])!.value,
    };
  }
}
