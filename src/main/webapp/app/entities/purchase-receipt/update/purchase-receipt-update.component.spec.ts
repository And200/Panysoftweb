import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { PurchaseReceiptService } from '../service/purchase-receipt.service';
import { IPurchaseReceipt, PurchaseReceipt } from '../purchase-receipt.model';
import { IEmployee } from 'app/entities/employee/employee.model';
import { EmployeeService } from 'app/entities/employee/service/employee.service';
import { IDetailSale } from 'app/entities/detail-sale/detail-sale.model';
import { DetailSaleService } from 'app/entities/detail-sale/service/detail-sale.service';
import { IClient } from 'app/entities/client/client.model';
import { ClientService } from 'app/entities/client/service/client.service';

import { PurchaseReceiptUpdateComponent } from './purchase-receipt-update.component';

describe('PurchaseReceipt Management Update Component', () => {
  let comp: PurchaseReceiptUpdateComponent;
  let fixture: ComponentFixture<PurchaseReceiptUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let purchaseReceiptService: PurchaseReceiptService;
  let employeeService: EmployeeService;
  let detailSaleService: DetailSaleService;
  let clientService: ClientService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [PurchaseReceiptUpdateComponent],
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
      .overrideTemplate(PurchaseReceiptUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(PurchaseReceiptUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    purchaseReceiptService = TestBed.inject(PurchaseReceiptService);
    employeeService = TestBed.inject(EmployeeService);
    detailSaleService = TestBed.inject(DetailSaleService);
    clientService = TestBed.inject(ClientService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Employee query and add missing value', () => {
      const purchaseReceipt: IPurchaseReceipt = { id: 456 };
      const employee: IEmployee = { id: 11738 };
      purchaseReceipt.employee = employee;

      const employeeCollection: IEmployee[] = [{ id: 35256 }];
      jest.spyOn(employeeService, 'query').mockReturnValue(of(new HttpResponse({ body: employeeCollection })));
      const additionalEmployees = [employee];
      const expectedCollection: IEmployee[] = [...additionalEmployees, ...employeeCollection];
      jest.spyOn(employeeService, 'addEmployeeToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ purchaseReceipt });
      comp.ngOnInit();

      expect(employeeService.query).toHaveBeenCalled();
      expect(employeeService.addEmployeeToCollectionIfMissing).toHaveBeenCalledWith(employeeCollection, ...additionalEmployees);
      expect(comp.employeesSharedCollection).toEqual(expectedCollection);
    });

    it('Should call DetailSale query and add missing value', () => {
      const purchaseReceipt: IPurchaseReceipt = { id: 456 };
      const detailSale: IDetailSale = { id: 62999 };
      purchaseReceipt.detailSale = detailSale;

      const detailSaleCollection: IDetailSale[] = [{ id: 72672 }];
      jest.spyOn(detailSaleService, 'query').mockReturnValue(of(new HttpResponse({ body: detailSaleCollection })));
      const additionalDetailSales = [detailSale];
      const expectedCollection: IDetailSale[] = [...additionalDetailSales, ...detailSaleCollection];
      jest.spyOn(detailSaleService, 'addDetailSaleToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ purchaseReceipt });
      comp.ngOnInit();

      expect(detailSaleService.query).toHaveBeenCalled();
      expect(detailSaleService.addDetailSaleToCollectionIfMissing).toHaveBeenCalledWith(detailSaleCollection, ...additionalDetailSales);
      expect(comp.detailSalesSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Client query and add missing value', () => {
      const purchaseReceipt: IPurchaseReceipt = { id: 456 };
      const client: IClient = { id: 90114 };
      purchaseReceipt.client = client;

      const clientCollection: IClient[] = [{ id: 69816 }];
      jest.spyOn(clientService, 'query').mockReturnValue(of(new HttpResponse({ body: clientCollection })));
      const additionalClients = [client];
      const expectedCollection: IClient[] = [...additionalClients, ...clientCollection];
      jest.spyOn(clientService, 'addClientToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ purchaseReceipt });
      comp.ngOnInit();

      expect(clientService.query).toHaveBeenCalled();
      expect(clientService.addClientToCollectionIfMissing).toHaveBeenCalledWith(clientCollection, ...additionalClients);
      expect(comp.clientsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const purchaseReceipt: IPurchaseReceipt = { id: 456 };
      const employee: IEmployee = { id: 33766 };
      purchaseReceipt.employee = employee;
      const detailSale: IDetailSale = { id: 75350 };
      purchaseReceipt.detailSale = detailSale;
      const client: IClient = { id: 49681 };
      purchaseReceipt.client = client;

      activatedRoute.data = of({ purchaseReceipt });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(purchaseReceipt));
      expect(comp.employeesSharedCollection).toContain(employee);
      expect(comp.detailSalesSharedCollection).toContain(detailSale);
      expect(comp.clientsSharedCollection).toContain(client);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<PurchaseReceipt>>();
      const purchaseReceipt = { id: 123 };
      jest.spyOn(purchaseReceiptService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ purchaseReceipt });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: purchaseReceipt }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(purchaseReceiptService.update).toHaveBeenCalledWith(purchaseReceipt);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<PurchaseReceipt>>();
      const purchaseReceipt = new PurchaseReceipt();
      jest.spyOn(purchaseReceiptService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ purchaseReceipt });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: purchaseReceipt }));
      saveSubject.complete();

      // THEN
      expect(purchaseReceiptService.create).toHaveBeenCalledWith(purchaseReceipt);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<PurchaseReceipt>>();
      const purchaseReceipt = { id: 123 };
      jest.spyOn(purchaseReceiptService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ purchaseReceipt });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(purchaseReceiptService.update).toHaveBeenCalledWith(purchaseReceipt);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackEmployeeById', () => {
      it('Should return tracked Employee primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackEmployeeById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });

    describe('trackDetailSaleById', () => {
      it('Should return tracked DetailSale primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackDetailSaleById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });

    describe('trackClientById', () => {
      it('Should return tracked Client primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackClientById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });
});
