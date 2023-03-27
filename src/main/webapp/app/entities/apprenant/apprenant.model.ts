import { IDemande } from 'app/entities/demande/demande.model';
import { IChefEtablissement } from 'app/entities/chef-etablissement/chef-etablissement.model';
import { IEtablissement } from 'app/entities/etablissement/etablissement.model';
import { Sexe } from 'app/entities/enumerations/sexe.model';

export interface IApprenant {
  id?: number;
  matriculeApp?: string | null;
  nom?: string;
  prenom?: string;
  sexe?: Sexe;
  telephone?: string;
  email?: string;
  demandes?: IDemande[] | null;
  chefEtablissement?: IChefEtablissement | null;
  etablissement?: IEtablissement | null;
}

export class Apprenant implements IApprenant {
  constructor(
    public id?: number,
    public matriculeApp?: string | null,
    public nom?: string,
    public prenom?: string,
    public sexe?: Sexe,
    public telephone?: string,
    public email?: string,
    public demandes?: IDemande[] | null,
    public chefEtablissement?: IChefEtablissement | null,
    public etablissement?: IEtablissement | null
  ) {}
}

export function getApprenantIdentifier(apprenant: IApprenant): number | undefined {
  return apprenant.id;
}
