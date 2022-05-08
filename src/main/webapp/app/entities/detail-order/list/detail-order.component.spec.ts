import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { DetailOrderService } from '../service/detail-order.service';

import { DetailOrderComponent } from './detail-order.component';

describe('DetailOrder Management Component', () => {
  let comp: DetailOrderComponent;
  let fixture: ComponentFixture<DetailOrderComponent>;
  let service: DetailOrderService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [DetailOrderComponent],
    })
      .overrideTemplate(DetailOrderComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(DetailOrderComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(DetailOrderService);

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
    expect(comp.detailOrders?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });
});
