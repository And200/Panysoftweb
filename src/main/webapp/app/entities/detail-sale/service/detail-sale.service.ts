import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IDetailSale, getDetailSaleIdentifier } from '../detail-sale.model';

export type EntityResponseType = HttpResponse<IDetailSale>;
export type EntityArrayResponseType = HttpResponse<IDetailSale[]>;

@Injectable({ providedIn: 'root' })
export class DetailSaleService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/detail-sales');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(detailSale: IDetailSale): Observable<EntityResponseType> {
    return this.http.post<IDetailSale>(this.resourceUrl, detailSale, { observe: 'response' });
  }

  update(detailSale: IDetailSale): Observable<EntityResponseType> {
    return this.http.put<IDetailSale>(`${this.resourceUrl}/${getDetailSaleIdentifier(detailSale) as number}`, detailSale, {
      observe: 'response',
    });
  }

  partialUpdate(detailSale: IDetailSale): Observable<EntityResponseType> {
    return this.http.patch<IDetailSale>(`${this.resourceUrl}/${getDetailSaleIdentifier(detailSale) as number}`, detailSale, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IDetailSale>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IDetailSale[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addDetailSaleToCollectionIfMissing(
    detailSaleCollection: IDetailSale[],
    ...detailSalesToCheck: (IDetailSale | null | undefined)[]
  ): IDetailSale[] {
    const detailSales: IDetailSale[] = detailSalesToCheck.filter(isPresent);
    if (detailSales.length > 0) {
      const detailSaleCollectionIdentifiers = detailSaleCollection.map(detailSaleItem => getDetailSaleIdentifier(detailSaleItem)!);
      const detailSalesToAdd = detailSales.filter(detailSaleItem => {
        const detailSaleIdentifier = getDetailSaleIdentifier(detailSaleItem);
        if (detailSaleIdentifier == null || detailSaleCollectionIdentifiers.includes(detailSaleIdentifier)) {
          return false;
        }
        detailSaleCollectionIdentifiers.push(detailSaleIdentifier);
        return true;
      });
      return [...detailSalesToAdd, ...detailSaleCollection];
    }
    return detailSaleCollection;
  }
}
