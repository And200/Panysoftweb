import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IRecip, getRecipIdentifier } from '../recip.model';

export type EntityResponseType = HttpResponse<IRecip>;
export type EntityArrayResponseType = HttpResponse<IRecip[]>;

@Injectable({ providedIn: 'root' })
export class RecipService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/recips');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(recip: IRecip): Observable<EntityResponseType> {
    return this.http.post<IRecip>(this.resourceUrl, recip, { observe: 'response' });
  }

  update(recip: IRecip): Observable<EntityResponseType> {
    return this.http.put<IRecip>(`${this.resourceUrl}/${getRecipIdentifier(recip) as number}`, recip, { observe: 'response' });
  }

  partialUpdate(recip: IRecip): Observable<EntityResponseType> {
    return this.http.patch<IRecip>(`${this.resourceUrl}/${getRecipIdentifier(recip) as number}`, recip, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IRecip>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IRecip[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addRecipToCollectionIfMissing(recipCollection: IRecip[], ...recipsToCheck: (IRecip | null | undefined)[]): IRecip[] {
    const recips: IRecip[] = recipsToCheck.filter(isPresent);
    if (recips.length > 0) {
      const recipCollectionIdentifiers = recipCollection.map(recipItem => getRecipIdentifier(recipItem)!);
      const recipsToAdd = recips.filter(recipItem => {
        const recipIdentifier = getRecipIdentifier(recipItem);
        if (recipIdentifier == null || recipCollectionIdentifiers.includes(recipIdentifier)) {
          return false;
        }
        recipCollectionIdentifiers.push(recipIdentifier);
        return true;
      });
      return [...recipsToAdd, ...recipCollection];
    }
    return recipCollection;
  }
}
