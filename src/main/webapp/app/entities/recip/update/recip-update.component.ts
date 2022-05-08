import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IRecip, Recip } from '../recip.model';
import { RecipService } from '../service/recip.service';
import { IProduct } from 'app/entities/product/product.model';
import { ProductService } from 'app/entities/product/service/product.service';
import { ICategory } from 'app/entities/category/category.model';
import { CategoryService } from 'app/entities/category/service/category.service';

@Component({
  selector: 'panysoft-recip-update',
  templateUrl: './recip-update.component.html',
})
export class RecipUpdateComponent implements OnInit {
  isSaving = false;

  productsSharedCollection: IProduct[] = [];
  categoriesSharedCollection: ICategory[] = [];

  editForm = this.fb.group({
    id: [],
    nameRecip: [null, [Validators.required, Validators.maxLength(30)]],
    estimatedPricePreparation: [null, [Validators.required]],
    amountProductUsed: [null, [Validators.required]],
    product: [null, Validators.required],
    category: [null, Validators.required],
  });

  constructor(
    protected recipService: RecipService,
    protected productService: ProductService,
    protected categoryService: CategoryService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ recip }) => {
      this.updateForm(recip);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const recip = this.createFromForm();
    if (recip.id !== undefined) {
      this.subscribeToSaveResponse(this.recipService.update(recip));
    } else {
      this.subscribeToSaveResponse(this.recipService.create(recip));
    }
  }

  trackProductById(_index: number, item: IProduct): number {
    return item.id!;
  }

  trackCategoryById(_index: number, item: ICategory): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IRecip>>): void {
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

  protected updateForm(recip: IRecip): void {
    this.editForm.patchValue({
      id: recip.id,
      nameRecip: recip.nameRecip,
      estimatedPricePreparation: recip.estimatedPricePreparation,
      amountProductUsed: recip.amountProductUsed,
      product: recip.product,
      category: recip.category,
    });

    this.productsSharedCollection = this.productService.addProductToCollectionIfMissing(this.productsSharedCollection, recip.product);
    this.categoriesSharedCollection = this.categoryService.addCategoryToCollectionIfMissing(
      this.categoriesSharedCollection,
      recip.category
    );
  }

  protected loadRelationshipsOptions(): void {
    this.productService
      .query()
      .pipe(map((res: HttpResponse<IProduct[]>) => res.body ?? []))
      .pipe(
        map((products: IProduct[]) => this.productService.addProductToCollectionIfMissing(products, this.editForm.get('product')!.value))
      )
      .subscribe((products: IProduct[]) => (this.productsSharedCollection = products));

    this.categoryService
      .query()
      .pipe(map((res: HttpResponse<ICategory[]>) => res.body ?? []))
      .pipe(
        map((categories: ICategory[]) =>
          this.categoryService.addCategoryToCollectionIfMissing(categories, this.editForm.get('category')!.value)
        )
      )
      .subscribe((categories: ICategory[]) => (this.categoriesSharedCollection = categories));
  }

  protected createFromForm(): IRecip {
    return {
      ...new Recip(),
      id: this.editForm.get(['id'])!.value,
      nameRecip: this.editForm.get(['nameRecip'])!.value,
      estimatedPricePreparation: this.editForm.get(['estimatedPricePreparation'])!.value,
      amountProductUsed: this.editForm.get(['amountProductUsed'])!.value,
      product: this.editForm.get(['product'])!.value,
      category: this.editForm.get(['category'])!.value,
    };
  }
}
