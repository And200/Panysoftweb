import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { PresentationService } from '../service/presentation.service';

import { PresentationComponent } from './presentation.component';

describe('Presentation Management Component', () => {
  let comp: PresentationComponent;
  let fixture: ComponentFixture<PresentationComponent>;
  let service: PresentationService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [PresentationComponent],
    })
      .overrideTemplate(PresentationComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(PresentationComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(PresentationService);

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
    expect(comp.presentations?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });
});
