import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { ProductService } from '../service/product.service';
import { IProduct, Product } from '../product.model';
import { IProvider } from 'app/entities/provider/provider.model';
import { ProviderService } from 'app/entities/provider/service/provider.service';
import { IPresentation } from 'app/entities/presentation/presentation.model';
import { PresentationService } from 'app/entities/presentation/service/presentation.service';

import { ProductUpdateComponent } from './product-update.component';

describe('Product Management Update Component', () => {
  let comp: ProductUpdateComponent;
  let fixture: ComponentFixture<ProductUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let productService: ProductService;
  let providerService: ProviderService;
  let presentationService: PresentationService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [ProductUpdateComponent],
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
      .overrideTemplate(ProductUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ProductUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    productService = TestBed.inject(ProductService);
    providerService = TestBed.inject(ProviderService);
    presentationService = TestBed.inject(PresentationService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Provider query and add missing value', () => {
      const product: IProduct = { id: 456 };
      const provider: IProvider = { id: 52212 };
      product.provider = provider;

      const providerCollection: IProvider[] = [{ id: 42346 }];
      jest.spyOn(providerService, 'query').mockReturnValue(of(new HttpResponse({ body: providerCollection })));
      const additionalProviders = [provider];
      const expectedCollection: IProvider[] = [...additionalProviders, ...providerCollection];
      jest.spyOn(providerService, 'addProviderToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ product });
      comp.ngOnInit();

      expect(providerService.query).toHaveBeenCalled();
      expect(providerService.addProviderToCollectionIfMissing).toHaveBeenCalledWith(providerCollection, ...additionalProviders);
      expect(comp.providersSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Presentation query and add missing value', () => {
      const product: IProduct = { id: 456 };
      const presentation: IPresentation = { id: 20927 };
      product.presentation = presentation;

      const presentationCollection: IPresentation[] = [{ id: 65496 }];
      jest.spyOn(presentationService, 'query').mockReturnValue(of(new HttpResponse({ body: presentationCollection })));
      const additionalPresentations = [presentation];
      const expectedCollection: IPresentation[] = [...additionalPresentations, ...presentationCollection];
      jest.spyOn(presentationService, 'addPresentationToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ product });
      comp.ngOnInit();

      expect(presentationService.query).toHaveBeenCalled();
      expect(presentationService.addPresentationToCollectionIfMissing).toHaveBeenCalledWith(
        presentationCollection,
        ...additionalPresentations
      );
      expect(comp.presentationsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const product: IProduct = { id: 456 };
      const provider: IProvider = { id: 36032 };
      product.provider = provider;
      const presentation: IPresentation = { id: 32308 };
      product.presentation = presentation;

      activatedRoute.data = of({ product });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(product));
      expect(comp.providersSharedCollection).toContain(provider);
      expect(comp.presentationsSharedCollection).toContain(presentation);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Product>>();
      const product = { id: 123 };
      jest.spyOn(productService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ product });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: product }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(productService.update).toHaveBeenCalledWith(product);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Product>>();
      const product = new Product();
      jest.spyOn(productService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ product });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: product }));
      saveSubject.complete();

      // THEN
      expect(productService.create).toHaveBeenCalledWith(product);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Product>>();
      const product = { id: 123 };
      jest.spyOn(productService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ product });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(productService.update).toHaveBeenCalledWith(product);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackProviderById', () => {
      it('Should return tracked Provider primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackProviderById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });

    describe('trackPresentationById', () => {
      it('Should return tracked Presentation primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackPresentationById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });
});
