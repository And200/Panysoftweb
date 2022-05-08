import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { DetailSaleService } from '../service/detail-sale.service';

import { DetailSaleComponent } from './detail-sale.component';

describe('DetailSale Management Component', () => {
  let comp: DetailSaleComponent;
  let fixture: ComponentFixture<DetailSaleComponent>;
  let service: DetailSaleService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [DetailSaleComponent],
    })
      .overrideTemplate(DetailSaleComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(DetailSaleComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(DetailSaleService);

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
    expect(comp.detailSales?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });
});
