import { IPurchaseReceipt } from 'app/entities/purchase-receipt/purchase-receipt.model';
import { IPerson } from 'app/entities/person/person.model';

export interface IClient {
  id?: number;
  purchaseReceipts?: IPurchaseReceipt[] | null;
  person?: IPerson;
}

export class Client implements IClient {
  constructor(public id?: number, public purchaseReceipts?: IPurchaseReceipt[] | null, public person?: IPerson) {}
}

export function getClientIdentifier(client: IClient): number | undefined {
  return client.id;
}
