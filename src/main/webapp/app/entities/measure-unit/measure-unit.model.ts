import { IPresentation } from 'app/entities/presentation/presentation.model';

export interface IMeasureUnit {
  id?: number;
  nameUnit?: string;
  presentations?: IPresentation[] | null;
}

export class MeasureUnit implements IMeasureUnit {
  constructor(public id?: number, public nameUnit?: string, public presentations?: IPresentation[] | null) {}
}

export function getMeasureUnitIdentifier(measureUnit: IMeasureUnit): number | undefined {
  return measureUnit.id;
}
