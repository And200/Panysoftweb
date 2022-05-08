import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IPresentation, getPresentationIdentifier } from '../presentation.model';

export type EntityResponseType = HttpResponse<IPresentation>;
export type EntityArrayResponseType = HttpResponse<IPresentation[]>;

@Injectable({ providedIn: 'root' })
export class PresentationService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/presentations');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(presentation: IPresentation): Observable<EntityResponseType> {
    return this.http.post<IPresentation>(this.resourceUrl, presentation, { observe: 'response' });
  }

  update(presentation: IPresentation): Observable<EntityResponseType> {
    return this.http.put<IPresentation>(`${this.resourceUrl}/${getPresentationIdentifier(presentation) as number}`, presentation, {
      observe: 'response',
    });
  }

  partialUpdate(presentation: IPresentation): Observable<EntityResponseType> {
    return this.http.patch<IPresentation>(`${this.resourceUrl}/${getPresentationIdentifier(presentation) as number}`, presentation, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IPresentation>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IPresentation[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addPresentationToCollectionIfMissing(
    presentationCollection: IPresentation[],
    ...presentationsToCheck: (IPresentation | null | undefined)[]
  ): IPresentation[] {
    const presentations: IPresentation[] = presentationsToCheck.filter(isPresent);
    if (presentations.length > 0) {
      const presentationCollectionIdentifiers = presentationCollection.map(
        presentationItem => getPresentationIdentifier(presentationItem)!
      );
      const presentationsToAdd = presentations.filter(presentationItem => {
        const presentationIdentifier = getPresentationIdentifier(presentationItem);
        if (presentationIdentifier == null || presentationCollectionIdentifiers.includes(presentationIdentifier)) {
          return false;
        }
        presentationCollectionIdentifiers.push(presentationIdentifier);
        return true;
      });
      return [...presentationsToAdd, ...presentationCollection];
    }
    return presentationCollection;
  }
}
