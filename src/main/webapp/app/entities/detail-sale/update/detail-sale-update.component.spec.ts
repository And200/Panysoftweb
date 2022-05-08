import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { DetailSaleService } from '../service/detail-sale.service';
import { IDetailSale, DetailSale } from '../detail-sale.model';
import { IProductElaborated } from 'app/entities/product-elaborated/product-elaborated.model';
import { ProductElaboratedService } from 'app/entities/product-elaborated/service/product-elaborated.service';
import { IPresentation } from 'app/entities/presentation/presentation.model';
import { PresentationService } from 'app/entities/presentation/service/presentation.service';

import { DetailSaleUpdateComponent } from './detail-sale-update.component';

describe('DetailSale Management Update Component', () => {
  let comp: DetailSaleUpdateComponent;
  let fixture: ComponentFixture<DetailSaleUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let detailSaleService: DetailSaleService;
  let productElaboratedService: ProductElaboratedService;
  let presentationService: PresentationService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [DetailSaleUpdateComponent],
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
      .overrideTemplate(DetailSaleUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(DetailSaleUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    detailSaleService = TestBed.inject(DetailSaleService);
    productElaboratedService = TestBed.inject(ProductElaboratedService);
    presentationService = TestBed.inject(PresentationService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call ProductElaborated query and add missing value', () => {
      const detailSale: IDetailSale = { id: 456 };
      const productElaborated: IProductElaborated = { id: 39592 };
      detailSale.productElaborated = productElaborated;

      const productElaboratedCollection: IProductElaborated[] = [{ id: 67737 }];
      jest.spyOn(productElaboratedService, 'query').mockReturnValue(of(new HttpResponse({ body: productElaboratedCollection })));
      const additionalProductElaborateds = [productElaborated];
      const expectedCollection: IProductElaborated[] = [...additionalProductElaborateds, ...productElaboratedCollection];
      jest.spyOn(productElaboratedService, 'addProductElaboratedToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ detailSale });
      comp.ngOnInit();

      expect(productElaboratedService.query).toHaveBeenCalled();
      expect(productElaboratedService.addProductElaboratedToCollectionIfMissing).toHaveBeenCalledWith(
        productElaboratedCollection,
        ...additionalProductElaborateds
      );
      expect(comp.productElaboratedsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Presentation query and add missing value', () => {
      const detailSale: IDetailSale = { id: 456 };
      const presentation: IPresentation = { id: 50678 };
      detailSale.presentation = presentation;

      const presentationCollection: IPresentation[] = [{ id: 47048 }];
      jest.spyOn(presentationService, 'query').mockReturnValue(of(new HttpResponse({ body: presentationCollection })));
      const additionalPresentations = [presentation];
      const expectedCollection: IPresentation[] = [...additionalPresentations, ...presentationCollection];
      jest.spyOn(presentationService, 'addPresentationToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ detailSale });
      comp.ngOnInit();

      expect(presentationService.query).toHaveBeenCalled();
      expect(presentationService.addPresentationToCollectionIfMissing).toHaveBeenCalledWith(
        presentationCollection,
        ...additionalPresentations
      );
      expect(comp.presentationsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const detailSale: IDetailSale = { id: 456 };
      const productElaborated: IProductElaborated = { id: 98837 };
      detailSale.productElaborated = productElaborated;
      const presentation: IPresentation = { id: 17170 };
      detailSale.presentation = presentation;

      activatedRoute.data = of({ detailSale });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(detailSale));
      expect(comp.productElaboratedsSharedCollection).toContain(productElaborated);
      expect(comp.presentationsSharedCollection).toContain(presentation);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<DetailSale>>();
      const detailSale = { id: 123 };
      jest.spyOn(detailSaleService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ detailSale });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: detailSale }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(detailSaleService.update).toHaveBeenCalledWith(detailSale);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<DetailSale>>();
      const detailSale = new DetailSale();
      jest.spyOn(detailSaleService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ detailSale });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: detailSale }));
      saveSubject.complete();

      // THEN
      expect(detailSaleService.create).toHaveBeenCalledWith(detailSale);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<DetailSale>>();
      const detailSale = { id: 123 };
      jest.spyOn(detailSaleService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ detailSale });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(detailSaleService.update).toHaveBeenCalledWith(detailSale);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackProductElaboratedById', () => {
      it('Should return tracked ProductElaborated primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackProductElaboratedById(0, entity);
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
