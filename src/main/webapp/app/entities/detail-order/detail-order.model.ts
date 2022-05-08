import dayjs from 'dayjs/esm';
import { IProvider } from 'app/entities/provider/provider.model';
import { IOrderPlaced } from 'app/entities/order-placed/order-placed.model';

export interface IDetailOrder {
  id?: number;
  quantityOrdered?: number;
  date?: dayjs.Dayjs;
  invoiceNumber?: string;
  productOrdered?: string;
  pricePayment?: number;
  provider?: IProvider;
  orderPlaced?: IOrderPlaced;
}

export class DetailOrder implements IDetailOrder {
  constructor(
    public id?: number,
    public quantityOrdered?: number,
    public date?: dayjs.Dayjs,
    public invoiceNumber?: string,
    public productOrdered?: string,
    public pricePayment?: number,
    public provider?: IProvider,
    public orderPlaced?: IOrderPlaced
  ) {}
}

export function getDetailOrderIdentifier(detailOrder: IDetailOrder): number | undefined {
  return detailOrder.id;
}
