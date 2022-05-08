import { IEmployee } from 'app/entities/employee/employee.model';
import { IClient } from 'app/entities/client/client.model';
import { IDocumentType } from 'app/entities/document-type/document-type.model';

export interface IPerson {
  id?: number;
  name?: string;
  email?: string;
  adress?: string;
  employees?: IEmployee[] | null;
  clients?: IClient[] | null;
  documentType?: IDocumentType;
}

export class Person implements IPerson {
  constructor(
    public id?: number,
    public name?: string,
    public email?: string,
    public adress?: string,
    public employees?: IEmployee[] | null,
    public clients?: IClient[] | null,
    public documentType?: IDocumentType
  ) {}
}

export function getPersonIdentifier(person: IPerson): number | undefined {
  return person.id;
}
