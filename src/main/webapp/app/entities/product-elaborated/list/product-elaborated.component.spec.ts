import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { ProductElaboratedService } from '../service/product-elaborated.service';

import { ProductElaboratedComponent } from './product-elaborated.component';

describe('ProductElaborated Management Component', () => {
  let comp: ProductElaboratedComponent;
  let fixture: ComponentFixture<ProductElaboratedComponent>;
  let service: ProductElaboratedService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [ProductElaboratedComponent],
    })
      .overrideTemplate(ProductElaboratedComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ProductElaboratedComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(ProductElaboratedService);

    const headers = new HttpHeaders();
    jest.spyOn(service, 'query').mockReturnValue(
      of(
        new HttpResponse({
          body: [{ id: 123 }],
          headers,
        })
      )
    );
  });

  it('Should call load all on init', () => {
    // WHEN
    comp.ngOnInit();

    // THEN
    expect(service.query).toHaveBeenCalled();
    expect(comp.productElaborateds?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });
});
