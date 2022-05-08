import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IProvider, Provider } from '../provider.model';
import { ProviderService } from '../service/provider.service';

@Component({
  selector: 'panysoft-provider-update',
  templateUrl: './provider-update.component.html',
})
export class ProviderUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    email: [null, [Validators.required, Validators.maxLength(50)]],
    adress: [null, [Validators.required, Validators.maxLength(50)]],
    nit: [null, [Validators.required, Validators.maxLength(50)]],
    name: [null, [Validators.required, Validators.maxLength(50)]],
    phone: [null, [Validators.required, Validators.maxLength(30)]],
  });

  constructor(protected providerService: ProviderService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ provider }) => {
      this.updateForm(provider);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const provider = this.createFromForm();
    if (provider.id !== undefined) {
      this.subscribeToSaveResponse(this.providerService.update(provider));
    } else {
      this.subscribeToSaveResponse(this.providerService.create(provider));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IProvider>>): void {
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

  protected updateForm(provider: IProvider): void {
    this.editForm.patchValue({
      id: provider.id,
      email: provider.email,
      adress: provider.adress,
      nit: provider.nit,
      name: provider.name,
      phone: provider.phone,
    });
  }

  protected createFromForm(): IProvider {
    return {
      ...new Provider(),
      id: this.editForm.get(['id'])!.value,
      email: this.editForm.get(['email'])!.value,
      adress: this.editForm.get(['adress'])!.value,
      nit: this.editForm.get(['nit'])!.value,
      name: this.editForm.get(['name'])!.value,
      phone: this.editForm.get(['phone'])!.value,
    };
  }
}
