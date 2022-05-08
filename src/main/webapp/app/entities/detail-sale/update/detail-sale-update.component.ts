import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IDetailSale, DetailSale } from '../detail-sale.model';
import { DetailSaleService } from '../service/detail-sale.service';
import { IProductElaborated } from 'app/entities/product-elaborated/product-elaborated.model';
import { ProductElaboratedService } from 'app/entities/product-elaborated/service/product-elaborated.service';
import { IPresentation } from 'app/entities/presentation/presentation.model';
import { PresentationService } from 'app/entities/presentation/service/presentation.service';

@Component({
  selector: 'panysoft-detail-sale-update',
  templateUrl: './detail-sale-update.component.html',
})
export class DetailSaleUpdateComponent implements OnInit {
  isSaving = false;

  productElaboratedsSharedCollection: IProductElaborated[] = [];
  presentationsSharedCollection: IPresentation[] = [];

  editForm = this.fb.group({
    id: [],
    productAmount: [null, [Validators.required]],
    productElaborated: [null, Validators.required],
    presentation: [null, Validators.required],
  });

  constructor(
    protected detailSaleService: DetailSaleService,
    protected productElaboratedService: ProductElaboratedService,
    protected presentationService: PresentationService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ detailSale }) => {
      this.updateForm(detailSale);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const detailSale = this.createFromForm();
    if (detailSale.id !== undefined) {
      this.subscribeToSaveResponse(this.detailSaleService.update(detailSale));
    } else {
      this.subscribeToSaveResponse(this.detailSaleService.create(detailSale));
    }
  }

  trackProductElaboratedById(_index: number, item: IProductElaborated): number {
    return item.id!;
  }

  trackPresentationById(_index: number, item: IPresentation): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IDetailSale>>): void {
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

  protected updateForm(detailSale: IDetailSale): void {
    this.editForm.patchValue({
      id: detailSale.id,
      productAmount: detailSale.productAmount,
      productElaborated: detailSale.productElaborated,
      presentation: detailSale.presentation,
    });

    this.productElaboratedsSharedCollection = this.productElaboratedService.addProductElaboratedToCollectionIfMissing(
      this.productElaboratedsSharedCollection,
      detailSale.productElaborated
    );
    this.presentationsSharedCollection = this.presentationService.addPresentationToCollectionIfMissing(
      this.presentationsSharedCollection,
      detailSale.presentation
    );
  }

  protected loadRelationshipsOptions(): void {
    this.productElaboratedService
      .query()
      .pipe(map((res: HttpResponse<IProductElaborated[]>) => res.body ?? []))
      .pipe(
        map((productElaborateds: IProductElaborated[]) =>
          this.productElaboratedService.addProductElaboratedToCollectionIfMissing(
            productElaborateds,
            this.editForm.get('productElaborated')!.value
          )
        )
      )
      .subscribe((productElaborateds: IProductElaborated[]) => (this.productElaboratedsSharedCollection = productElaborateds));

    this.presentationService
      .query()
      .pipe(map((res: HttpResponse<IPresentation[]>) => res.body ?? []))
      .pipe(
        map((presentations: IPresentation[]) =>
          this.presentationService.addPresentationToCollectionIfMissing(presentations, this.editForm.get('presentation')!.value)
        )
      )
      .subscribe((presentations: IPresentation[]) => (this.presentationsSharedCollection = presentations));
  }

  protected createFromForm(): IDetailSale {
    return {
      ...new DetailSale(),
      id: this.editForm.get(['id'])!.value,
      productAmount: this.editForm.get(['productAmount'])!.value,
      productElaborated: this.editForm.get(['productElaborated'])!.value,
      presentation: this.editForm.get(['presentation'])!.value,
    };
  }
}
