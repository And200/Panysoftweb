import { IDetailOrder } from 'app/entities/detail-order/detail-order.model';
import { StateOrder } from 'app/entities/enumerations/state-order.model';

export interface IOrderPlaced {
  id?: number;
  orderPlacedState?: StateOrder;
  detailOrders?: IDetailOrder[] | null;
}

export class OrderPlaced implements IOrderPlaced {
  constructor(public id?: number, public orderPlacedState?: StateOrder, public detailOrders?: IDetailOrder[] | null) {}
}

export function getOrderPlacedIdentifier(orderPlaced: IOrderPlaced): number | undefined {
  return orderPlaced.id;
}
