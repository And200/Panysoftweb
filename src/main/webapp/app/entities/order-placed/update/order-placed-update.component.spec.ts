import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { OrderPlacedService } from '../service/order-placed.service';
import { IOrderPlaced, OrderPlaced } from '../order-placed.model';

import { OrderPlacedUpdateComponent } from './order-placed-update.component';

describe('OrderPlaced Management Update Component', () => {
  let comp: OrderPlacedUpdateComponent;
  let fixture: ComponentFixture<OrderPlacedUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let orderPlacedService: OrderPlacedService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [OrderPlacedUpdateComponent],
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
      .overrideTemplate(OrderPlacedUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(OrderPlacedUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    orderPlacedService = TestBed.inject(OrderPlacedService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const orderPlaced: IOrderPlaced = { id: 456 };

      activatedRoute.data = of({ orderPlaced });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(orderPlaced));
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<OrderPlaced>>();
      const orderPlaced = { id: 123 };
      jest.spyOn(orderPlacedService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ orderPlaced });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: orderPlaced }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(orderPlacedService.update).toHaveBeenCalledWith(orderPlaced);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<OrderPlaced>>();
      const orderPlaced = new OrderPlaced();
      jest.spyOn(orderPlacedService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ orderPlaced });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: orderPlaced }));
      saveSubject.complete();

      // THEN
      expect(orderPlacedService.create).toHaveBeenCalledWith(orderPlaced);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<OrderPlaced>>();
      const orderPlaced = { id: 123 };
      jest.spyOn(orderPlacedService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ orderPlaced });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(orderPlacedService.update).toHaveBeenCalledWith(orderPlaced);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
