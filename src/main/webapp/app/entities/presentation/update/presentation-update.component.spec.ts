import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { PresentationService } from '../service/presentation.service';
import { IPresentation, Presentation } from '../presentation.model';
import { IMeasureUnit } from 'app/entities/measure-unit/measure-unit.model';
import { MeasureUnitService } from 'app/entities/measure-unit/service/measure-unit.service';

import { PresentationUpdateComponent } from './presentation-update.component';

describe('Presentation Management Update Component', () => {
  let comp: PresentationUpdateComponent;
  let fixture: ComponentFixture<PresentationUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let presentationService: PresentationService;
  let measureUnitService: MeasureUnitService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [PresentationUpdateComponent],
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
      .overrideTemplate(PresentationUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(PresentationUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    presentationService = TestBed.inject(PresentationService);
    measureUnitService = TestBed.inject(MeasureUnitService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call MeasureUnit query and add missing value', () => {
      const presentation: IPresentation = { id: 456 };
      const measureUnit: IMeasureUnit = { id: 97694 };
      presentation.measureUnit = measureUnit;

      const measureUnitCollection: IMeasureUnit[] = [{ id: 70831 }];
      jest.spyOn(measureUnitService, 'query').mockReturnValue(of(new HttpResponse({ body: measureUnitCollection })));
      const additionalMeasureUnits = [measureUnit];
      const expectedCollection: IMeasureUnit[] = [...additionalMeasureUnits, ...measureUnitCollection];
      jest.spyOn(measureUnitService, 'addMeasureUnitToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ presentation });
      comp.ngOnInit();

      expect(measureUnitService.query).toHaveBeenCalled();
      expect(measureUnitService.addMeasureUnitToCollectionIfMissing).toHaveBeenCalledWith(measureUnitCollection, ...additionalMeasureUnits);
      expect(comp.measureUnitsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const presentation: IPresentation = { id: 456 };
      const measureUnit: IMeasureUnit = { id: 34924 };
      presentation.measureUnit = measureUnit;

      activatedRoute.data = of({ presentation });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(presentation));
      expect(comp.measureUnitsSharedCollection).toContain(measureUnit);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Presentation>>();
      const presentation = { id: 123 };
      jest.spyOn(presentationService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ presentation });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: presentation }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(presentationService.update).toHaveBeenCalledWith(presentation);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Presentation>>();
      const presentation = new Presentation();
      jest.spyOn(presentationService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ presentation });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: presentation }));
      saveSubject.complete();

      // THEN
      expect(presentationService.create).toHaveBeenCalledWith(presentation);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Presentation>>();
      const presentation = { id: 123 };
      jest.spyOn(presentationService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ presentation });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(presentationService.update).toHaveBeenCalledWith(presentation);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackMeasureUnitById', () => {
      it('Should return tracked MeasureUnit primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackMeasureUnitById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });
});
