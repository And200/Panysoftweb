import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IProductElaborated, getProductElaboratedIdentifier } from '../product-elaborated.model';

export type EntityResponseType = HttpResponse<IProductElaborated>;
export type EntityArrayResponseType = HttpResponse<IProductElaborated[]>;

@Injectable({ providedIn: 'root' })
export class ProductElaboratedService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/product-elaborateds');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(productElaborated: IProductElaborated): Observable<EntityResponseType> {
    return this.http.post<IProductElaborated>(this.resourceUrl, productElaborated, { observe: 'response' });
  }

  update(productElaborated: IProductElaborated): Observable<EntityResponseType> {
    return this.http.put<IProductElaborated>(
      `${this.resourceUrl}/${getProductElaboratedIdentifier(productElaborated) as number}`,
      productElaborated,
      { observe: 'response' }
    );
  }

  partialUpdate(productElaborated: IProductElaborated): Observable<EntityResponseType> {
    return this.http.patch<IProductElaborated>(
      `${this.resourceUrl}/${getProductElaboratedIdentifier(productElaborated) as number}`,
      productElaborated,
      { observe: 'response' }
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IProductElaborated>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IProductElaborated[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addProductElaboratedToCollectionIfMissing(
    productElaboratedCollection: IProductElaborated[],
    ...productElaboratedsToCheck: (IProductElaborated | null | undefined)[]
  ): IProductElaborated[] {
    const productElaborateds: IProductElaborated[] = productElaboratedsToCheck.filter(isPresent);
    if (productElaborateds.length > 0) {
      const productElaboratedCollectionIdentifiers = productElaboratedCollection.map(
        productElaboratedItem => getProductElaboratedIdentifier(productElaboratedItem)!
      );
      const productElaboratedsToAdd = productElaborateds.filter(productElaboratedItem => {
        const productElaboratedIdentifier = getProductElaboratedIdentifier(productElaboratedItem);
        if (productElaboratedIdentifier == null || productElaboratedCollectionIdentifiers.includes(productElaboratedIdentifier)) {
          return false;
        }
        productElaboratedCollectionIdentifiers.push(productElaboratedIdentifier);
        return true;
      });
      return [...productElaboratedsToAdd, ...productElaboratedCollection];
    }
    return productElaboratedCollection;
  }
}
