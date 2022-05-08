import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IOrderPlaced, getOrderPlacedIdentifier } from '../order-placed.model';

export type EntityResponseType = HttpResponse<IOrderPlaced>;
export type EntityArrayResponseType = HttpResponse<IOrderPlaced[]>;

@Injectable({ providedIn: 'root' })
export class OrderPlacedService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/order-placeds');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(orderPlaced: IOrderPlaced): Observable<EntityResponseType> {
    return this.http.post<IOrderPlaced>(this.resourceUrl, orderPlaced, { observe: 'response' });
  }

  update(orderPlaced: IOrderPlaced): Observable<EntityResponseType> {
    return this.http.put<IOrderPlaced>(`${this.resourceUrl}/${getOrderPlacedIdentifier(orderPlaced) as number}`, orderPlaced, {
      observe: 'response',
    });
  }

  partialUpdate(orderPlaced: IOrderPlaced): Observable<EntityResponseType> {
    return this.http.patch<IOrderPlaced>(`${this.resourceUrl}/${getOrderPlacedIdentifier(orderPlaced) as number}`, orderPlaced, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IOrderPlaced>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IOrderPlaced[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addOrderPlacedToCollectionIfMissing(
    orderPlacedCollection: IOrderPlaced[],
    ...orderPlacedsToCheck: (IOrderPlaced | null | undefined)[]
  ): IOrderPlaced[] {
    const orderPlaceds: IOrderPlaced[] = orderPlacedsToCheck.filter(isPresent);
    if (orderPlaceds.length > 0) {
      const orderPlacedCollectionIdentifiers = orderPlacedCollection.map(orderPlacedItem => getOrderPlacedIdentifier(orderPlacedItem)!);
      const orderPlacedsToAdd = orderPlaceds.filter(orderPlacedItem => {
        const orderPlacedIdentifier = getOrderPlacedIdentifier(orderPlacedItem);
        if (orderPlacedIdentifier == null || orderPlacedCollectionIdentifiers.includes(orderPlacedIdentifier)) {
          return false;
        }
        orderPlacedCollectionIdentifiers.push(orderPlacedIdentifier);
        return true;
      });
      return [...orderPlacedsToAdd, ...orderPlacedCollection];
    }
    return orderPlacedCollection;
  }
}
