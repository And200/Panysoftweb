import { IDetailSale } from 'app/entities/detail-sale/detail-sale.model';
import { ICategory } from 'app/entities/category/category.model';

export interface IProductElaborated {
  id?: number;
  amountProduced?: number;
  productName?: string;
  buyPrice?: number;
  detailSales?: IDetailSale[] | null;
  category?: ICategory;
}

export class ProductElaborated implements IProductElaborated {
  constructor(
    public id?: number,
    public amountProduced?: number,
    public productName?: string,
    public buyPrice?: number,
    public detailSales?: IDetailSale[] | null,
    public category?: ICategory
  ) {}
}

export function getProductElaboratedIdentifier(productElaborated: IProductElaborated): number | undefined {
  return productElaborated.id;
}
