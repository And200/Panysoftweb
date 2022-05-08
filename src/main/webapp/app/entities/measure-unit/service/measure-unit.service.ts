import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IMeasureUnit, getMeasureUnitIdentifier } from '../measure-unit.model';

export type EntityResponseType = HttpResponse<IMeasureUnit>;
export type EntityArrayResponseType = HttpResponse<IMeasureUnit[]>;

@Injectable({ providedIn: 'root' })
export class MeasureUnitService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/measure-units');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(measureUnit: IMeasureUnit): Observable<EntityResponseType> {
    return this.http.post<IMeasureUnit>(this.resourceUrl, measureUnit, { observe: 'response' });
  }

  update(measureUnit: IMeasureUnit): Observable<EntityResponseType> {
    return this.http.put<IMeasureUnit>(`${this.resourceUrl}/${getMeasureUnitIdentifier(measureUnit) as number}`, measureUnit, {
      observe: 'response',
    });
  }

  partialUpdate(measureUnit: IMeasureUnit): Observable<EntityResponseType> {
    return this.http.patch<IMeasureUnit>(`${this.resourceUrl}/${getMeasureUnitIdentifier(measureUnit) as number}`, measureUnit, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IMeasureUnit>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IMeasureUnit[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addMeasureUnitToCollectionIfMissing(
    measureUnitCollection: IMeasureUnit[],
    ...measureUnitsToCheck: (IMeasureUnit | null | undefined)[]
  ): IMeasureUnit[] {
    const measureUnits: IMeasureUnit[] = measureUnitsToCheck.filter(isPresent);
    if (measureUnits.length > 0) {
      const measureUnitCollectionIdentifiers = measureUnitCollection.map(measureUnitItem => getMeasureUnitIdentifier(measureUnitItem)!);
      const measureUnitsToAdd = measureUnits.filter(measureUnitItem => {
        const measureUnitIdentifier = getMeasureUnitIdentifier(measureUnitItem);
        if (measureUnitIdentifier == null || measureUnitCollectionIdentifiers.includes(measureUnitIdentifier)) {
          return false;
        }
        measureUnitCollectionIdentifiers.push(measureUnitIdentifier);
        return true;
      });
      return [...measureUnitsToAdd, ...measureUnitCollection];
    }
    return measureUnitCollection;
  }
}
