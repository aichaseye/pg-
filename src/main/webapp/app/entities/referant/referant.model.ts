import { IUser } from 'app/entities/user/user.model';
import { IMatiere } from 'app/entities/matiere/matiere.model';

export interface IReferant {
  id?: number;
  nomPrenom?: string;
  user?: IUser | null;
  matieres?: IMatiere[] | null;
}

export class Referant implements IReferant {
  constructor(public id?: number, public nomPrenom?: string, public user?: IUser | null, public matieres?: IMatiere[] | null) {}
}

export function getReferantIdentifier(referant: IReferant): number | undefined {
  return referant.id;
}
