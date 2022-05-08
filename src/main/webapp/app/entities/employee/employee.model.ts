import { IUser } from 'app/entities/user/user.model';
import { IPurchaseReceipt } from 'app/entities/purchase-receipt/purchase-receipt.model';
import { IPerson } from 'app/entities/person/person.model';

export interface IEmployee {
  id?: number;
  user?: IUser;
  purchaseReceipts?: IPurchaseReceipt[] | null;
  person?: IPerson;
}

export class Employee implements IEmployee {
  constructor(public id?: number, public user?: IUser, public purchaseReceipts?: IPurchaseReceipt[] | null, public person?: IPerson) {}
}

export function getEmployeeIdentifier(employee: IEmployee): number | undefined {
  return employee.id;
}
