import { IPurchaseReceipt } from 'app/entities/purchase-receipt/purchase-receipt.model';
import { IProductElaborated } from 'app/entities/product-elaborated/product-elaborated.model';
import { IPresentation } from 'app/entities/presentation/presentation.model';

export interface IDetailSale {
  id?: number;
  productAmount?: number;
  purchaseReceipts?: IPurchaseReceipt[] | null;
  productElaborated?: IProductElaborated;
  presentation?: IPresentation;
}

export class DetailSale implements IDetailSale {
  constructor(
    public id?: number,
    public productAmount?: number,
    public purchaseReceipts?: IPurchaseReceipt[] | null,
    public productElaborated?: IProductElaborated,
    public presentation?: IPresentation
  ) {}
}

export function getDetailSaleIdentifier(detailSale: IDetailSale): number | undefined {
  return detailSale.id;
}
