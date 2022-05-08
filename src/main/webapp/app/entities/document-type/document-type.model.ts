import { IPerson } from 'app/entities/person/person.model';
import { StateDocument } from 'app/entities/enumerations/state-document.model';

export interface IDocumentType {
  id?: number;
  documentName?: string;
  initials?: string;
  stateDocumentType?: StateDocument;
  people?: IPerson[] | null;
}

export class DocumentType implements IDocumentType {
  constructor(
    public id?: number,
    public documentName?: string,
    public initials?: string,
    public stateDocumentType?: StateDocument,
    public people?: IPerson[] | null
  ) {}
}

export function getDocumentTypeIdentifier(documentType: IDocumentType): number | undefined {
  return documentType.id;
}
