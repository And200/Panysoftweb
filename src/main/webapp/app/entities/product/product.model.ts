import { IInventory } from 'app/entities/inventory/inventory.model';
import { IRecip } from 'app/entities/recip/recip.model';
import { IProvider } from 'app/entities/provider/provider.model';
import { IPresentation } from 'app/entities/presentation/presentation.model';

export interface IProduct {
  id?: number;
  productDetail?: string;
  productName?: string;
  inventories?: IInventory[] | null;
  recips?: IRecip[] | null;
  provider?: IProvider;
  presentation?: IPresentation;
}

export class Product implements IProduct {
  constructor(
    public id?: number,
    public productDetail?: string,
    public productName?: string,
    public inventories?: IInventory[] | null,
    public recips?: IRecip[] | null,
    public provider?: IProvider,
    public presentation?: IPresentation
  ) {}
}

export function getProductIdentifier(product: IProduct): number | undefined {
  return product.id;
}
