import { IProduct } from 'app/entities/product/product.model';

export interface IInventory {
  id?: number;
  stocks?: number;
  buyPrice?: number;
  product?: IProduct;
}

export class Inventory implements IInventory {
  constructor(public id?: number, public stocks?: number, public buyPrice?: number, public product?: IProduct) {}
}

export function getInventoryIdentifier(inventory: IInventory): number | undefined {
  return inventory.id;
}
