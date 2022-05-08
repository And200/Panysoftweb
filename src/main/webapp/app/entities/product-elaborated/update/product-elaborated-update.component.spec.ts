import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { ProductElaboratedService } from '../service/product-elaborated.service';
import { IProductElaborated, ProductElaborated } from '../product-elaborated.model';
import { ICategory } from 'app/entities/category/category.model';
import { CategoryService } from 'app/entities/category/service/category.service';

import { ProductElaboratedUpdateComponent } from './product-elaborated-update.component';

describe('ProductElaborated Management Update Component', () => {
  let comp: ProductElaboratedUpdateComponent;
  let fixture: ComponentFixture<ProductElaboratedUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let productElaboratedService: ProductElaboratedService;
  let categoryService: CategoryService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [ProductElaboratedUpdateComponent],
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
      .overrideTemplate(ProductElaboratedUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ProductElaboratedUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    productElaboratedService = TestBed.inject(ProductElaboratedService);
    categoryService = TestBed.inject(CategoryService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Category query and add missing value', () => {
      const productElaborated: IProductElaborated = { id: 456 };
      const category: ICategory = { id: 54388 };
      productElaborated.category = category;

      const categoryCollection: ICategory[] = [{ id: 95972 }];
      jest.spyOn(categoryService, 'query').mockReturnValue(of(new HttpResponse({ body: categoryCollection })));
      const additionalCategories = [category];
      const expectedCollection: ICategory[] = [...additionalCategories, ...categoryCollection];
      jest.spyOn(categoryService, 'addCategoryToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ productElaborated });
      comp.ngOnInit();

      expect(categoryService.query).toHaveBeenCalled();
      expect(categoryService.addCategoryToCollectionIfMissing).toHaveBeenCalledWith(categoryCollection, ...additionalCategories);
      expect(comp.categoriesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const productElaborated: IProductElaborated = { id: 456 };
      const category: ICategory = { id: 53101 };
      productElaborated.category = category;

      activatedRoute.data = of({ productElaborated });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(productElaborated));
      expect(comp.categoriesSharedCollection).toContain(category);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ProductElaborated>>();
      const productElaborated = { id: 123 };
      jest.spyOn(productElaboratedService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ productElaborated });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: productElaborated }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(productElaboratedService.update).toHaveBeenCalledWith(productElaborated);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ProductElaborated>>();
      const productElaborated = new ProductElaborated();
      jest.spyOn(productElaboratedService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ productElaborated });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: productElaborated }));
      saveSubject.complete();

      // THEN
      expect(productElaboratedService.create).toHaveBeenCalledWith(productElaborated);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ProductElaborated>>();
      const productElaborated = { id: 123 };
      jest.spyOn(productElaboratedService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ productElaborated });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(productElaboratedService.update).toHaveBeenCalledWith(productElaborated);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackCategoryById', () => {
      it('Should return tracked Category primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackCategoryById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });
});
