import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IOrderPlaced, OrderPlaced } from '../order-placed.model';
import { OrderPlacedService } from '../service/order-placed.service';
import { StateOrder } from 'app/entities/enumerations/state-order.model';

@Component({
  selector: 'panysoft-order-placed-update',
  templateUrl: './order-placed-update.component.html',
})
export class OrderPlacedUpdateComponent implements OnInit {
  isSaving = false;
  stateOrderValues = Object.keys(StateOrder);

  editForm = this.fb.group({
    id: [],
    orderPlacedState: [null, [Validators.required]],
  });

  constructor(protected orderPlacedService: OrderPlacedService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ orderPlaced }) => {
      this.updateForm(orderPlaced);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const orderPlaced = this.createFromForm();
    if (orderPlaced.id !== undefined) {
      this.subscribeToSaveResponse(this.orderPlacedService.update(orderPlaced));
    } else {
      this.subscribeToSaveResponse(this.orderPlacedService.create(orderPlaced));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IOrderPlaced>>): void {
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

  protected updateForm(orderPlaced: IOrderPlaced): void {
    this.editForm.patchValue({
      id: orderPlaced.id,
      orderPlacedState: orderPlaced.orderPlacedState,
    });
  }

  protected createFromForm(): IOrderPlaced {
    return {
      ...new OrderPlaced(),
      id: this.editForm.get(['id'])!.value,
      orderPlacedState: this.editForm.get(['orderPlacedState'])!.value,
    };
  }
}
