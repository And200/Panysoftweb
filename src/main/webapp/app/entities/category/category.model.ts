import { IProductElaborated } from 'app/entities/product-elaborated/product-elaborated.model';
import { IRecip } from 'app/entities/recip/recip.model';

export interface ICategory {
  id?: number;
  nameCategory?: string;
  productElaborateds?: IProductElaborated[] | null;
  recips?: IRecip[] | null;
}

export class Category implements ICategory {
  constructor(
    public id?: number,
    public nameCategory?: string,
    public productElaborateds?: IProductElaborated[] | null,
    public recips?: IRecip[] | null
  ) {}
}

export function getCategoryIdentifier(category: ICategory): number | undefined {
  return category.id;
}
