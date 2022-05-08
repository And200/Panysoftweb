import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { ProviderService } from '../service/provider.service';
import { IProvider, Provider } from '../provider.model';

import { ProviderUpdateComponent } from './provider-update.component';

describe('Provider Management Update Component', () => {
  let comp: ProviderUpdateComponent;
  let fixture: ComponentFixture<ProviderUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let providerService: ProviderService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [ProviderUpdateComponent],
      providers: [
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(ProviderUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ProviderUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    providerService = TestBed.inject(ProviderService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const provider: IProvider = { id: 456 };

      activatedRoute.data = of({ provider });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(provider));
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Provider>>();
      const provider = { id: 123 };
      jest.spyOn(providerService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ provider });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: provider }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(providerService.update).toHaveBeenCalledWith(provider);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Provider>>();
      const provider = new Provider();
      jest.spyOn(providerService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ provider });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: provider }));
      saveSubject.complete();

      // THEN
      expect(providerService.create).toHaveBeenCalledWith(provider);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Provider>>();
      const provider = { id: 123 };
      jest.spyOn(providerService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ provider });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(providerService.update).toHaveBeenCalledWith(provider);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
