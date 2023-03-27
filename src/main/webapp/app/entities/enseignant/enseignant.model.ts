import { IDemande } from 'app/entities/demande/demande.model';
import { IBFPA } from 'app/entities/bfpa/bfpa.model';
import { IEtablissement } from 'app/entities/etablissement/etablissement.model';
import { NomReg } from 'app/entities/enumerations/nom-reg.model';
import { NomDep } from 'app/entities/enumerations/nom-dep.model';
import { CodeIA } from 'app/entities/enumerations/code-ia.model';
import { Sexe } from 'app/entities/enumerations/sexe.model';

export interface IEnseignant {
  id?: number;
  matriculeEns?: string | null;
  nom?: string;
  prenom?: string;
  numCNI?: number;
  anneeDentree?: number;
  region?: NomReg;
  departement?: NomDep;
  autreDep?: string | null;
  autreRegion?: string | null;
  codeIA?: CodeIA;
  autreCodeIA?: string | null;
  sexe?: Sexe | null;
  email?: string;
  demandes?: IDemande[] | null;
  bFPA?: IBFPA | null;
  etablissement?: IEtablissement | null;
}

export class Enseignant implements IEnseignant {
  constructor(
    public id?: number,
    public matriculeEns?: string | null,
    public nom?: string,
    public prenom?: string,
    public numCNI?: number,
    public anneeDentree?: number,
    public region?: NomReg,
    public departement?: NomDep,
    public autreDep?: string | null,
    public autreRegion?: string | null,
    public codeIA?: CodeIA,
    public autreCodeIA?: string | null,
    public sexe?: Sexe | null,
    public email?: string,
    public demandes?: IDemande[] | null,
    public bFPA?: IBFPA | null,
    public etablissement?: IEtablissement | null
  ) {}
}

export function getEnseignantIdentifier(enseignant: IEnseignant): number | undefined {
  return enseignant.id;
}
