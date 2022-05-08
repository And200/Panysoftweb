import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { MeasureUnitDetailComponent } from './measure-unit-detail.component';

describe('MeasureUnit Management Detail Component', () => {
  let comp: MeasureUnitDetailComponent;
  let fixture: ComponentFixture<MeasureUnitDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [MeasureUnitDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ measureUnit: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(MeasureUnitDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(MeasureUnitDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load measureUnit on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.measureUnit).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
