import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IMeasureUnit, MeasureUnit } from '../measure-unit.model';
import { MeasureUnitService } from '../service/measure-unit.service';

@Component({
  selector: 'panysoft-measure-unit-update',
  templateUrl: './measure-unit-update.component.html',
})
export class MeasureUnitUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    nameUnit: [null, [Validators.required, Validators.maxLength(30)]],
  });

  constructor(protected measureUnitService: MeasureUnitService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ measureUnit }) => {
      this.updateForm(measureUnit);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const measureUnit = this.createFromForm();
    if (measureUnit.id !== undefined) {
      this.subscribeToSaveResponse(this.measureUnitService.update(measureUnit));
    } else {
      this.subscribeToSaveResponse(this.measureUnitService.create(measureUnit));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IMeasureUnit>>): void {
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

  protected updateForm(measureUnit: IMeasureUnit): void {
    this.editForm.patchValue({
      id: measureUnit.id,
      nameUnit: measureUnit.nameUnit,
    });
  }

  protected createFromForm(): IMeasureUnit {
    return {
      ...new MeasureUnit(),
      id: this.editForm.get(['id'])!.value,
      nameUnit: this.editForm.get(['nameUnit'])!.value,
    };
  }
}
