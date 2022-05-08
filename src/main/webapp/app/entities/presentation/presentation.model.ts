import { IDetailSale } from 'app/entities/detail-sale/detail-sale.model';
import { IProduct } from 'app/entities/product/product.model';
import { IMeasureUnit } from 'app/entities/measure-unit/measure-unit.model';

export interface IPresentation {
  id?: number;
  presentation?: string;
  detailSales?: IDetailSale[] | null;
  products?: IProduct[] | null;
  measureUnit?: IMeasureUnit;
}

export class Presentation implements IPresentation {
  constructor(
    public id?: number,
    public presentation?: string,
    public detailSales?: IDetailSale[] | null,
    public products?: IProduct[] | null,
    public measureUnit?: IMeasureUnit
  ) {}
}

export function getPresentationIdentifier(presentation: IPresentation): number | undefined {
  return presentation.id;
}
