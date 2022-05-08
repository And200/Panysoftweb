import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IProduct, Product } from '../product.model';
import { ProductService } from '../service/product.service';
import { IProvider } from 'app/entities/provider/provider.model';
import { ProviderService } from 'app/entities/provider/service/provider.service';
import { IPresentation } from 'app/entities/presentation/presentation.model';
import { PresentationService } from 'app/entities/presentation/service/presentation.service';

@Component({
  selector: 'panysoft-product-update',
  templateUrl: './product-update.component.html',
})
export class ProductUpdateComponent implements OnInit {
  isSaving = false;

  providersSharedCollection: IProvider[] = [];
  presentationsSharedCollection: IPresentation[] = [];

  editForm = this.fb.group({
    id: [],
    productDetail: [null, [Validators.required, Validators.maxLength(50)]],
    productName: [null, [Validators.required, Validators.maxLength(50)]],
    provider: [null, Validators.required],
    presentation: [null, Validators.required],
  });

  constructor(
    protected productService: ProductService,
    protected providerService: ProviderService,
    protected presentationService: PresentationService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ product }) => {
      this.updateForm(product);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const product = this.createFromForm();
    if (product.id !== undefined) {
      this.subscribeToSaveResponse(this.productService.update(product));
    } else {
      this.subscribeToSaveResponse(this.productService.create(product));
    }
  }

  trackProviderById(_index: number, item: IProvider): number {
    return item.id!;
  }

  trackPresentationById(_index: number, item: IPresentation): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IProduct>>): void {
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

  protected updateForm(product: IProduct): void {
    this.editForm.patchValue({
      id: product.id,
      productDetail: product.productDetail,
      productName: product.productName,
      provider: product.provider,
      presentation: product.presentation,
    });

    this.providersSharedCollection = this.providerService.addProviderToCollectionIfMissing(
      this.providersSharedCollection,
      product.provider
    );
    this.presentationsSharedCollection = this.presentationService.addPresentationToCollectionIfMissing(
      this.presentationsSharedCollection,
      product.presentation
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

  protected createFromForm(): IProduct {
    return {
      ...new Product(),
      id: this.editForm.get(['id'])!.value,
      productDetail: this.editForm.get(['productDetail'])!.value,
      productName: this.editForm.get(['productName'])!.value,
      provider: this.editForm.get(['provider'])!.value,
      presentation: this.editForm.get(['presentation'])!.value,
    };
  }
}
