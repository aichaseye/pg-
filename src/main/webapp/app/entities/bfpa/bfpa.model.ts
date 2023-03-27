import { IUser } from 'app/entities/user/user.model';
import { IEtablissement } from 'app/entities/etablissement/etablissement.model';
import { IEnseignant } from 'app/entities/enseignant/enseignant.model';

export interface IBFPA {
  id?: number;
  nomPrenom?: string;
  user?: IUser | null;
  etablissements?: IEtablissement[] | null;
  enseignants?: IEnseignant[] | null;
}

export class BFPA implements IBFPA {
  constructor(
    public id?: number,
    public nomPrenom?: string,
    public user?: IUser | null,
    public etablissements?: IEtablissement[] | null,
    public enseignants?: IEnseignant[] | null
  ) {}
}

export function getBFPAIdentifier(bFPA: IBFPA): number | undefined {
  return bFPA.id;
}
