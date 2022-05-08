import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { DetailOrderService } from '../service/detail-order.service';
import { IDetailOrder, DetailOrder } from '../detail-order.model';
import { IProvider } from 'app/entities/provider/provider.model';
import { ProviderService } from 'app/entities/provider/service/provider.service';
import { IOrderPlaced } from 'app/entities/order-placed/order-placed.model';
import { OrderPlacedService } from 'app/entities/order-placed/service/order-placed.service';

import { DetailOrderUpdateComponent } from './detail-order-update.component';

describe('DetailOrder Management Update Component', () => {
  let comp: DetailOrderUpdateComponent;
  let fixture: ComponentFixture<DetailOrderUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let detailOrderService: DetailOrderService;
  let providerService: ProviderService;
  let orderPlacedService: OrderPlacedService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [DetailOrderUpdateComponent],
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
      .overrideTemplate(DetailOrderUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(DetailOrderUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    detailOrderService = TestBed.inject(DetailOrderService);
    providerService = TestBed.inject(ProviderService);
    orderPlacedService = TestBed.inject(OrderPlacedService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Provider query and add missing value', () => {
      const detailOrder: IDetailOrder = { id: 456 };
      const provider: IProvider = { id: 35030 };
      detailOrder.provider = provider;

      const providerCollection: IProvider[] = [{ id: 40156 }];
      jest.spyOn(providerService, 'query').mockReturnValue(of(new HttpResponse({ body: providerCollection })));
      const additionalProviders = [provider];
      const expectedCollection: IProvider[] = [...additionalProviders, ...providerCollection];
      jest.spyOn(providerService, 'addProviderToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ detailOrder });
      comp.ngOnInit();

      expect(providerService.query).toHaveBeenCalled();
      expect(providerService.addProviderToCollectionIfMissing).toHaveBeenCalledWith(providerCollection, ...additionalProviders);
      expect(comp.providersSharedCollection).toEqual(expectedCollection);
    });

    it('Should call OrderPlaced query and add missing value', () => {
      const detailOrder: IDetailOrder = { id: 456 };
      const orderPlaced: IOrderPlaced = { id: 32780 };
      detailOrder.orderPlaced = orderPlaced;

      const orderPlacedCollection: IOrderPlaced[] = [{ id: 57071 }];
      jest.spyOn(orderPlacedService, 'query').mockReturnValue(of(new HttpResponse({ body: orderPlacedCollection })));
      const additionalOrderPlaceds = [orderPlaced];
      const expectedCollection: IOrderPlaced[] = [...additionalOrderPlaceds, ...orderPlacedCollection];
      jest.spyOn(orderPlacedService, 'addOrderPlacedToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ detailOrder });
      comp.ngOnInit();

      expect(orderPlacedService.query).toHaveBeenCalled();
      expect(orderPlacedService.addOrderPlacedToCollectionIfMissing).toHaveBeenCalledWith(orderPlacedCollection, ...additionalOrderPlaceds);
      expect(comp.orderPlacedsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const detailOrder: IDetailOrder = { id: 456 };
      const provider: IProvider = { id: 56273 };
      detailOrder.provider = provider;
      const orderPlaced: IOrderPlaced = { id: 1443 };
      detailOrder.orderPlaced = orderPlaced;

      activatedRoute.data = of({ detailOrder });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(detailOrder));
      expect(comp.providersSharedCollection).toContain(provider);
      expect(comp.orderPlacedsSharedCollection).toContain(orderPlaced);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<DetailOrder>>();
      const detailOrder = { id: 123 };
      jest.spyOn(detailOrderService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ detailOrder });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: detailOrder }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(detailOrderService.update).toHaveBeenCalledWith(detailOrder);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<DetailOrder>>();
      const detailOrder = new DetailOrder();
      jest.spyOn(detailOrderService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ detailOrder });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: detailOrder }));
      saveSubject.complete();

      // THEN
      expect(detailOrderService.create).toHaveBeenCalledWith(detailOrder);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<DetailOrder>>();
      const detailOrder = { id: 123 };
      jest.spyOn(detailOrderService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ detailOrder });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(detailOrderService.update).toHaveBeenCalledWith(detailOrder);
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

    describe('trackOrderPlacedById', () => {
      it('Should return tracked OrderPlaced primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackOrderPlacedById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });
});
