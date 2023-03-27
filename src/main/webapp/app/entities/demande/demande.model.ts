import { IEnseignant } from 'app/entities/enseignant/enseignant.model';
import { IApprenant } from 'app/entities/apprenant/apprenant.model';
import { IEtablissement } from 'app/entities/etablissement/etablissement.model';
import { Motif } from 'app/entities/enumerations/motif.model';
import { TypeDemandeur } from 'app/entities/enumerations/type-demandeur.model';
import { NomEtab } from 'app/entities/enumerations/nom-etab.model';

export interface IDemande {
  id?: number;
  motif?: Motif;
  typeDemandeur?: TypeDemandeur;
  nom?: string;
  prenom?: string;
  nomEtab?: NomEtab | null;
  autreNomEtab?: string | null;
  email?: string;
  enseignant?: IEnseignant | null;
  apprenant?: IApprenant | null;
  etablissement?: IEtablissement | null;
}

export class Demande implements IDemande {
  constructor(
    public id?: number,
    public motif?: Motif,
    public typeDemandeur?: TypeDemandeur,
    public nom?: string,
    public prenom?: string,
    public nomEtab?: NomEtab | null,
    public autreNomEtab?: string | null,
    public email?: string,
    public enseignant?: IEnseignant | null,
    public apprenant?: IApprenant | null,
    public etablissement?: IEtablissement | null
  ) {}
}

export function getDemandeIdentifier(demande: IDemande): number | undefined {
  return demande.id;
}
