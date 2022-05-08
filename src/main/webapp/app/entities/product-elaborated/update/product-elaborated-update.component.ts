import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IProductElaborated, ProductElaborated } from '../product-elaborated.model';
import { ProductElaboratedService } from '../service/product-elaborated.service';
import { ICategory } from 'app/entities/category/category.model';
import { CategoryService } from 'app/entities/category/service/category.service';

@Component({
  selector: 'panysoft-product-elaborated-update',
  templateUrl: './product-elaborated-update.component.html',
})
export class ProductElaboratedUpdateComponent implements OnInit {
  isSaving = false;

  categoriesSharedCollection: ICategory[] = [];

  editForm = this.fb.group({
    id: [],
    amountProduced: [null, [Validators.required]],
    productName: [null, [Validators.required, Validators.maxLength(30)]],
    buyPrice: [null, [Validators.required]],
    category: [null, Validators.required],
  });

  constructor(
    protected productElaboratedService: ProductElaboratedService,
    protected categoryService: CategoryService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ productElaborated }) => {
      this.updateForm(productElaborated);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const productElaborated = this.createFromForm();
    if (productElaborated.id !== undefined) {
      this.subscribeToSaveResponse(this.productElaboratedService.update(productElaborated));
    } else {
      this.subscribeToSaveResponse(this.productElaboratedService.create(productElaborated));
    }
  }

  trackCategoryById(_index: number, item: ICategory): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IProductElaborated>>): void {
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

  protected updateForm(productElaborated: IProductElaborated): void {
    this.editForm.patchValue({
      id: productElaborated.id,
      amountProduced: productElaborated.amountProduced,
      productName: productElaborated.productName,
      buyPrice: productElaborated.buyPrice,
      category: productElaborated.category,
    });

    this.categoriesSharedCollection = this.categoryService.addCategoryToCollectionIfMissing(
      this.categoriesSharedCollection,
      productElaborated.category
    );
  }

  protected loadRelationshipsOptions(): void {
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

  protected createFromForm(): IProductElaborated {
    return {
      ...new ProductElaborated(),
      id: this.editForm.get(['id'])!.value,
      amountProduced: this.editForm.get(['amountProduced'])!.value,
      productName: this.editForm.get(['productName'])!.value,
      buyPrice: this.editForm.get(['buyPrice'])!.value,
      category: this.editForm.get(['category'])!.value,
    };
  }
}
