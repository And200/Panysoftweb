import { IProduct } from 'app/entities/product/product.model';
import { IDetailOrder } from 'app/entities/detail-order/detail-order.model';

export interface IProvider {
  id?: number;
  email?: string;
  adress?: string;
  nit?: string;
  name?: string;
  phone?: string;
  products?: IProduct[] | null;
  detailOrders?: IDetailOrder[] | null;
}

export class Provider implements IProvider {
  constructor(
    public id?: number,
    public email?: string,
    public adress?: string,
    public nit?: string,
    public name?: string,
    public phone?: string,
    public products?: IProduct[] | null,
    public detailOrders?: IDetailOrder[] | null
  ) {}
}

export function getProviderIdentifier(provider: IProvider): number | undefined {
  return provider.id;
}
