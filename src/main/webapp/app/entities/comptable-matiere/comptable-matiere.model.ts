import { IUser } from 'app/entities/user/user.model';
import { IMatiere } from 'app/entities/matiere/matiere.model';

export interface IComptableMatiere {
  id?: number;
  nomPrenom?: string;
  user?: IUser | null;
  matieres?: IMatiere[] | null;
}

export class ComptableMatiere implements IComptableMatiere {
  constructor(public id?: number, public nomPrenom?: string, public user?: IUser | null, public matieres?: IMatiere[] | null) {}
}

export function getComptableMatiereIdentifier(comptableMatiere: IComptableMatiere): number | undefined {
  return comptableMatiere.id;
}
