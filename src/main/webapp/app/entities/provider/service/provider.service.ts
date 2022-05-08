import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IProvider, getProviderIdentifier } from '../provider.model';

export type EntityResponseType = HttpResponse<IProvider>;
export type EntityArrayResponseType = HttpResponse<IProvider[]>;

@Injectable({ providedIn: 'root' })
export class ProviderService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/providers');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(provider: IProvider): Observable<EntityResponseType> {
    return this.http.post<IProvider>(this.resourceUrl, provider, { observe: 'response' });
  }

  update(provider: IProvider): Observable<EntityResponseType> {
    return this.http.put<IProvider>(`${this.resourceUrl}/${getProviderIdentifier(provider) as number}`, provider, { observe: 'response' });
  }

  partialUpdate(provider: IProvider): Observable<EntityResponseType> {
    return this.http.patch<IProvider>(`${this.resourceUrl}/${getProviderIdentifier(provider) as number}`, provider, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IProvider>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IProvider[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addProviderToCollectionIfMissing(providerCollection: IProvider[], ...providersToCheck: (IProvider | null | undefined)[]): IProvider[] {
    const providers: IProvider[] = providersToCheck.filter(isPresent);
    if (providers.length > 0) {
      const providerCollectionIdentifiers = providerCollection.map(providerItem => getProviderIdentifier(providerItem)!);
      const providersToAdd = providers.filter(providerItem => {
        const providerIdentifier = getProviderIdentifier(providerItem);
        if (providerIdentifier == null || providerCollectionIdentifiers.includes(providerIdentifier)) {
          return false;
        }
        providerCollectionIdentifiers.push(providerIdentifier);
        return true;
      });
      return [...providersToAdd, ...providerCollection];
    }
    return providerCollection;
  }
}
