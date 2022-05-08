import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { OrderPlacedService } from '../service/order-placed.service';

import { OrderPlacedComponent } from './order-placed.component';

describe('OrderPlaced Management Component', () => {
  let comp: OrderPlacedComponent;
  let fixture: ComponentFixture<OrderPlacedComponent>;
  let service: OrderPlacedService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [OrderPlacedComponent],
    })
      .overrideTemplate(OrderPlacedComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(OrderPlacedComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(OrderPlacedService);

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
    expect(comp.orderPlaceds?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });
});
