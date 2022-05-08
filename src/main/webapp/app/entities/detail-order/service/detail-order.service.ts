import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IDetailOrder, getDetailOrderIdentifier } from '../detail-order.model';

export type EntityResponseType = HttpResponse<IDetailOrder>;
export type EntityArrayResponseType = HttpResponse<IDetailOrder[]>;

@Injectable({ providedIn: 'root' })
export class DetailOrderService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/detail-orders');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(detailOrder: IDetailOrder): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(detailOrder);
    return this.http
      .post<IDetailOrder>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(detailOrder: IDetailOrder): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(detailOrder);
    return this.http
      .put<IDetailOrder>(`${this.resourceUrl}/${getDetailOrderIdentifier(detailOrder) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(detailOrder: IDetailOrder): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(detailOrder);
    return this.http
      .patch<IDetailOrder>(`${this.resourceUrl}/${getDetailOrderIdentifier(detailOrder) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IDetailOrder>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IDetailOrder[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addDetailOrderToCollectionIfMissing(
    detailOrderCollection: IDetailOrder[],
    ...detailOrdersToCheck: (IDetailOrder | null | undefined)[]
  ): IDetailOrder[] {
    const detailOrders: IDetailOrder[] = detailOrdersToCheck.filter(isPresent);
    if (detailOrders.length > 0) {
      const detailOrderCollectionIdentifiers = detailOrderCollection.map(detailOrderItem => getDetailOrderIdentifier(detailOrderItem)!);
      const detailOrdersToAdd = detailOrders.filter(detailOrderItem => {
        const detailOrderIdentifier = getDetailOrderIdentifier(detailOrderItem);
        if (detailOrderIdentifier == null || detailOrderCollectionIdentifiers.includes(detailOrderIdentifier)) {
          return false;
        }
        detailOrderCollectionIdentifiers.push(detailOrderIdentifier);
        return true;
      });
      return [...detailOrdersToAdd, ...detailOrderCollection];
    }
    return detailOrderCollection;
  }

  protected convertDateFromClient(detailOrder: IDetailOrder): IDetailOrder {
    return Object.assign({}, detailOrder, {
      date: detailOrder.date?.isValid() ? detailOrder.date.toJSON() : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.date = res.body.date ? dayjs(res.body.date) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((detailOrder: IDetailOrder) => {
        detailOrder.date = detailOrder.date ? dayjs(detailOrder.date) : undefined;
      });
    }
    return res;
  }
}
