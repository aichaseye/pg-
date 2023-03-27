import dayjs from 'dayjs/esm';
import { IApprenant } from 'app/entities/apprenant/apprenant.model';
import { IEnseignant } from 'app/entities/enseignant/enseignant.model';
import { IMatiere } from 'app/entities/matiere/matiere.model';
import { IDemande } from 'app/entities/demande/demande.model';
import { IBFPA } from 'app/entities/bfpa/bfpa.model';
import { NomReg } from 'app/entities/enumerations/nom-reg.model';
import { NomDep } from 'app/entities/enumerations/nom-dep.model';
import { NomEtab } from 'app/entities/enumerations/nom-etab.model';
import { CodeIA } from 'app/entities/enumerations/code-ia.model';
import { TypeEtab } from 'app/entities/enumerations/type-etab.model';
import { StatutEtab } from 'app/entities/enumerations/statut-etab.model';
import { TypeInspection } from 'app/entities/enumerations/type-inspection.model';

export interface IEtablissement {
  id?: number;
  matriculeEtab?: string | null;
  region?: NomReg;
  departement?: NomDep;
  autreRegion?: string | null;
  autreDep?: string | null;
  nomEtab?: NomEtab | null;
  codeIA?: CodeIA;
  autreCodeIA?: string | null;
  autreNomeEtab?: string | null;
  typeEtab?: TypeEtab;
  anneeCreation?: dayjs.Dayjs;
  statut?: StatutEtab;
  emailEtab?: string;
  typeInsp?: TypeInspection | null;
  apprenants?: IApprenant[] | null;
  enseignants?: IEnseignant[] | null;
  matieres?: IMatiere[] | null;
  demandes?: IDemande[] | null;
  bFPA?: IBFPA | null;
}

export class Etablissement implements IEtablissement {
  constructor(
    public id?: number,
    public matriculeEtab?: string | null,
    public region?: NomReg,
    public departement?: NomDep,
    public autreRegion?: string | null,
    public autreDep?: string | null,
    public nomEtab?: NomEtab | null,
    public codeIA?: CodeIA,
    public autreCodeIA?: string | null,
    public autreNomeEtab?: string | null,
    public typeEtab?: TypeEtab,
    public anneeCreation?: dayjs.Dayjs,
    public statut?: StatutEtab,
    public emailEtab?: string,
    public typeInsp?: TypeInspection | null,
    public apprenants?: IApprenant[] | null,
    public enseignants?: IEnseignant[] | null,
    public matieres?: IMatiere[] | null,
    public demandes?: IDemande[] | null,
    public bFPA?: IBFPA | null
  ) {}
}

export function getEtablissementIdentifier(etablissement: IEtablissement): number | undefined {
  return etablissement.id;
}
