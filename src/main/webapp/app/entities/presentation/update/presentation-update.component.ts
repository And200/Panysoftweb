import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IPresentation, Presentation } from '../presentation.model';
import { PresentationService } from '../service/presentation.service';
import { IMeasureUnit } from 'app/entities/measure-unit/measure-unit.model';
import { MeasureUnitService } from 'app/entities/measure-unit/service/measure-unit.service';

@Component({
  selector: 'panysoft-presentation-update',
  templateUrl: './presentation-update.component.html',
})
export class PresentationUpdateComponent implements OnInit {
  isSaving = false;

  measureUnitsSharedCollection: IMeasureUnit[] = [];

  editForm = this.fb.group({
    id: [],
    presentation: [null, [Validators.required, Validators.maxLength(30)]],
    measureUnit: [null, Validators.required],
  });

  constructor(
    protected presentationService: PresentationService,
    protected measureUnitService: MeasureUnitService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ presentation }) => {
      this.updateForm(presentation);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const presentation = this.createFromForm();
    if (presentation.id !== undefined) {
      this.subscribeToSaveResponse(this.presentationService.update(presentation));
    } else {
      this.subscribeToSaveResponse(this.presentationService.create(presentation));
    }
  }

  trackMeasureUnitById(_index: number, item: IMeasureUnit): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPresentation>>): void {
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

  protected updateForm(presentation: IPresentation): void {
    this.editForm.patchValue({
      id: presentation.id,
      presentation: presentation.presentation,
      measureUnit: presentation.measureUnit,
    });

    this.measureUnitsSharedCollection = this.measureUnitService.addMeasureUnitToCollectionIfMissing(
      this.measureUnitsSharedCollection,
      presentation.measureUnit
    );
  }

  protected loadRelationshipsOptions(): void {
    this.measureUnitService
      .query()
      .pipe(map((res: HttpResponse<IMeasureUnit[]>) => res.body ?? []))
      .pipe(
        map((measureUnits: IMeasureUnit[]) =>
          this.measureUnitService.addMeasureUnitToCollectionIfMissing(measureUnits, this.editForm.get('measureUnit')!.value)
        )
      )
      .subscribe((measureUnits: IMeasureUnit[]) => (this.measureUnitsSharedCollection = measureUnits));
  }

  protected createFromForm(): IPresentation {
    return {
      ...new Presentation(),
      id: this.editForm.get(['id'])!.value,
      presentation: this.editForm.get(['presentation'])!.value,
      measureUnit: this.editForm.get(['measureUnit'])!.value,
    };
  }
}
