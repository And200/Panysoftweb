import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { MeasureUnitService } from '../service/measure-unit.service';
import { IMeasureUnit, MeasureUnit } from '../measure-unit.model';

import { MeasureUnitUpdateComponent } from './measure-unit-update.component';

describe('MeasureUnit Management Update Component', () => {
  let comp: MeasureUnitUpdateComponent;
  let fixture: ComponentFixture<MeasureUnitUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let measureUnitService: MeasureUnitService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [MeasureUnitUpdateComponent],
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
      .overrideTemplate(MeasureUnitUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(MeasureUnitUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    measureUnitService = TestBed.inject(MeasureUnitService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const measureUnit: IMeasureUnit = { id: 456 };

      activatedRoute.data = of({ measureUnit });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(measureUnit));
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<MeasureUnit>>();
      const measureUnit = { id: 123 };
      jest.spyOn(measureUnitService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ measureUnit });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: measureUnit }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(measureUnitService.update).toHaveBeenCalledWith(measureUnit);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<MeasureUnit>>();
      const measureUnit = new MeasureUnit();
      jest.spyOn(measureUnitService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ measureUnit });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: measureUnit }));
      saveSubject.complete();

      // THEN
      expect(measureUnitService.create).toHaveBeenCalledWith(measureUnit);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<MeasureUnit>>();
      const measureUnit = { id: 123 };
      jest.spyOn(measureUnitService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ measureUnit });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(measureUnitService.update).toHaveBeenCalledWith(measureUnit);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
