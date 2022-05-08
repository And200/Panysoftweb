import { IProduct } from 'app/entities/product/product.model';
import { ICategory } from 'app/entities/category/category.model';

export interface IRecip {
  id?: number;
  nameRecip?: string;
  estimatedPricePreparation?: number;
  amountProductUsed?: number;
  product?: IProduct;
  category?: ICategory;
}

export class Recip implements IRecip {
  constructor(
    public id?: number,
    public nameRecip?: string,
    public estimatedPricePreparation?: number,
    public amountProductUsed?: number,
    public product?: IProduct,
    public category?: ICategory
  ) {}
}

export function getRecipIdentifier(recip: IRecip): number | undefined {
  return recip.id;
}
