import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { ProviderService } from '../service/provider.service';

import { ProviderComponent } from './provider.component';

describe('Provider Management Component', () => {
  let comp: ProviderComponent;
  let fixture: ComponentFixture<ProviderComponent>;
  let service: ProviderService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [ProviderComponent],
    })
      .overrideTemplate(ProviderComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ProviderComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(ProviderService);

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
    expect(comp.providers?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });
});
