import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IPurchaseReceipt, getPurchaseReceiptIdentifier } from '../purchase-receipt.model';

export type EntityResponseType = HttpResponse<IPurchaseReceipt>;
export type EntityArrayResponseType = HttpResponse<IPurchaseReceipt[]>;

@Injectable({ providedIn: 'root' })
export class PurchaseReceiptService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/purchase-receipts');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(purchaseReceipt: IPurchaseReceipt): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(purchaseReceipt);
    return this.http
      .post<IPurchaseReceipt>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(purchaseReceipt: IPurchaseReceipt): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(purchaseReceipt);
    return this.http
      .put<IPurchaseReceipt>(`${this.resourceUrl}/${getPurchaseReceiptIdentifier(purchaseReceipt) as number}`, copy, {
        observe: 'response',
      })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(purchaseReceipt: IPurchaseReceipt): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(purchaseReceipt);
    return this.http
      .patch<IPurchaseReceipt>(`${this.resourceUrl}/${getPurchaseReceiptIdentifier(purchaseReceipt) as number}`, copy, {
        observe: 'response',
      })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IPurchaseReceipt>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IPurchaseReceipt[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addPurchaseReceiptToCollectionIfMissing(
    purchaseReceiptCollection: IPurchaseReceipt[],
    ...purchaseReceiptsToCheck: (IPurchaseReceipt | null | undefined)[]
  ): IPurchaseReceipt[] {
    const purchaseReceipts: IPurchaseReceipt[] = purchaseReceiptsToCheck.filter(isPresent);
    if (purchaseReceipts.length > 0) {
      const purchaseReceiptCollectionIdentifiers = purchaseReceiptCollection.map(
        purchaseReceiptItem => getPurchaseReceiptIdentifier(purchaseReceiptItem)!
      );
      const purchaseReceiptsToAdd = purchaseReceipts.filter(purchaseReceiptItem => {
        const purchaseReceiptIdentifier = getPurchaseReceiptIdentifier(purchaseReceiptItem);
        if (purchaseReceiptIdentifier == null || purchaseReceiptCollectionIdentifiers.includes(purchaseReceiptIdentifier)) {
          return false;
        }
        purchaseReceiptCollectionIdentifiers.push(purchaseReceiptIdentifier);
        return true;
      });
      return [...purchaseReceiptsToAdd, ...purchaseReceiptCollection];
    }
    return purchaseReceiptCollection;
  }

  protected convertDateFromClient(purchaseReceipt: IPurchaseReceipt): IPurchaseReceipt {
    return Object.assign({}, purchaseReceipt, {
      date: purchaseReceipt.date?.isValid() ? purchaseReceipt.date.toJSON() : undefined,
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
      res.body.forEach((purchaseReceipt: IPurchaseReceipt) => {
        purchaseReceipt.date = purchaseReceipt.date ? dayjs(purchaseReceipt.date) : undefined;
      });
    }
    return res;
  }
}
