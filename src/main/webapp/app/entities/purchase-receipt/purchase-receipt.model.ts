import dayjs from 'dayjs/esm';
import { IEmployee } from 'app/entities/employee/employee.model';
import { IDetailSale } from 'app/entities/detail-sale/detail-sale.model';
import { IClient } from 'app/entities/client/client.model';

export interface IPurchaseReceipt {
  id?: number;
  date?: dayjs.Dayjs;
  totalSale?: number;
  employee?: IEmployee;
  detailSale?: IDetailSale;
  client?: IClient;
}

export class PurchaseReceipt implements IPurchaseReceipt {
  constructor(
    public id?: number,
    public date?: dayjs.Dayjs,
    public totalSale?: number,
    public employee?: IEmployee,
    public detailSale?: IDetailSale,
    public client?: IClient
  ) {}
}

export function getPurchaseReceiptIdentifier(purchaseReceipt: IPurchaseReceipt): number | undefined {
  return purchaseReceipt.id;
}
