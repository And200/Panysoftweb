import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { MeasureUnitService } from '../service/measure-unit.service';

import { MeasureUnitComponent } from './measure-unit.component';

describe('MeasureUnit Management Component', () => {
  let comp: MeasureUnitComponent;
  let fixture: ComponentFixture<MeasureUnitComponent>;
  let service: MeasureUnitService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [MeasureUnitComponent],
    })
      .overrideTemplate(MeasureUnitComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(MeasureUnitComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(MeasureUnitService);

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
    expect(comp.measureUnits?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });
});
