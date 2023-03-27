import { IUser } from 'app/entities/user/user.model';
import { IApprenant } from 'app/entities/apprenant/apprenant.model';

export interface IChefEtablissement {
  id?: number;
  nomPrenom?: string;
  user?: IUser | null;
  apprenants?: IApprenant[] | null;
}

export class ChefEtablissement implements IChefEtablissement {
  constructor(public id?: number, public nomPrenom?: string, public user?: IUser | null, public apprenants?: IApprenant[] | null) {}
}

export function getChefEtablissementIdentifier(chefEtablissement: IChefEtablissement): number | undefined {
  return chefEtablissement.id;
}
